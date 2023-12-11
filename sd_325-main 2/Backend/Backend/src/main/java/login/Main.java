package login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * The main application class for the WebSocket server.
 *
 * This class uses the `@ComponentScan` annotation to specify the base package
 * for component scanning. It scans the "com.example.demo.websocket" package
 * and its subpackages for Spring components, such as WebSocket endpoints and
 * controllers.
 *
 * Include this annotation as per your project structure needs
 * common case:
 * websocket-related files placed in a sub-directory
 * all HTTP controllers work except the websocket ones
 */
@SpringBootApplication
@EnableSwagger2
//@ComponentScan(basePackages = {"com.example.demo.websocket"})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    /* This Bean creates the documentation for all the APIs Which is exactly
       what is needed.
     */
    @Bean
    public Docket getAPIdocs() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
