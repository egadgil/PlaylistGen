package login;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Testing {

    @LocalServerPort
    int port;

        private static final String BASE_URL = "http://localhost:8080/";

    @Before
    public void setUp(){
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

    }

        @Test
        public void testProcessPayment() {
           // PaymentInfo paymentInfo = new PaymentInfo();
            String username = "testUser";

            given()
                    .contentType(ContentType.JSON)
                    .body("{\"cardNumber\":\"123456789\",\"securityCode\":\"666\",\"expiryDate\":\"10\\/9\",\"zipcode\":\"50101\",\"fullName\":\"meg\",\"reoccuring\":\"true\"}")
                    //.param("username", username)
                    .when()
                    .post("/payments/post_payment?username=testUser")
                    .then()
                    .statusCode(200)
                    .body(equalTo("Payment successful"));

            // Additional assertions if needed
        }

        @Test
        public void testProcessPaymentUserNotFound() {
           // PaymentInfo paymentInfo = new PaymentInfo();
            String username = "nonexistentUser";

            given()
                    .contentType(ContentType.JSON)
                    .body("{\"cardNumber\":\"123456789\",\"securityCode\":\"666\",\"expiryDate\":\"10\\/9\",\"zipcode\":\"50101\",\"fullName\":\"meg\",\"reoccuring\":\"true\"}")
                    //.param("username", username)
                    .when()
                    .post("/payments/post_payment?username=nonexistentUser")
                    .then()
                    .statusCode(200)
                    .body(equalTo("user not found"));

            // Additional assertions if needed
        }



        @Test
        public void testRegisterUser() {
            //random unique username and email
            byte[] array = new byte[7]; // length is bounded by 7
            new Random().nextBytes(array);
            String username = new String(array, Charset.forName("UTF-8"));

            byte[] arrayEmail = new byte[7]; // length is bounded by 7
            new Random().nextBytes(arrayEmail);
            String email = new String(array, Charset.forName("UTF-8"))+"@gmail.com";

            JSONObject builder = new JSONObject();
            builder.put("username",username);
            builder.put("password", "pass");
            builder.put("email",email);

            // Define the request payload
            String requestBody = builder.toString();

            // Perform the POST request to the register endpoint
            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/users/register")
                    .then()
                    .statusCode(200)
                    .body(equalTo("User registered successfully"));

        }


        @Test
        public void testLoginUser() {
            // Define the request payload
            String requestBody = "{\"username\":\"testuser\",\"password\":\"password123\"}";

            // Perform the POST request to the login endpoint
            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/users/login")
                    .then()
                    .statusCode(200)
                    .body(equalTo("Login successful"));
        }

        @Test
        public void testSetUserProfile() {
            // Define the request parameters
            String username = "testuser";
            String profilePic = "profile.jpg";

            // Perform the POST request to set_profile endpoint
            given()
                    .param("username", username)
                    .param("profile_pic", profilePic)
                    .when()
                    .post("/api/users/set_profile")
                    .then()
                    .statusCode(200)
                    .body("profilePicture", equalTo(profilePic));
        }

        @Test
        public void testSetMembershipType(){
            String username = "testuser";

            given()
                    .param("username", username)
                    .param("membershipType", "Paid member")
                    .when()
                    .get("/api/users/get_membership_type")
                    .then()
                    .statusCode(200)
                    .body(equalTo("Paid Member")); // assuming a paid membership type is "standard"
        }

        @Test
        public void testSetUI(){
            String username = "testuser";
            //String profilePic = "profile.jpg";

            // Perform the POST request to set_profile endpoint
            Response response = RestAssured
                    .with()
                    .param("username", username)
                    .param("uiTheme", "moss")
                    .request("POST","/api/users/set_ui");
            String resp = response.body().asString();
            JSONObject json = new JSONObject(resp);
            String ui = json.getString("uiThemeColor");
            assertEquals("moss",ui);

        }

        // Add more tests for other endpoints following a similar structure

        // Example: Test to get user membership type
        @Test
        public void testGetUserMembershipType() {
            String username = "testuser";

            given()
                    .param("username", username)
                    .when()
                    .get("/api/users/get_membership_type")
                    .then()
                    .statusCode(200)
                    .body(equalTo("Paid Member")); // assuming a paid membership type is "standard"
        }

        @Test
        public void testGetProfilePicture(){
            String username = "testuser";
            given()
                    .param("username",username)
                    .get("/api/users/get_profile_picture")
                    .then()
                    .statusCode(200)
                    .body(equalTo("profile.jpg"));

        }

        // Example: Test to get user UI theme
        @Test
        public void testGetUserUITheme() {
            String username = "testuser";

            given()
                    .param("username", username)
                    .when()
                    .get("/api/users/get_ui_theme")
                    .then()
                    .statusCode(200)
                    .body(equalTo("moss")); // assuming a default UI theme is "light"
        }

        // Add more tests for other endpoints following a similar structure

}
