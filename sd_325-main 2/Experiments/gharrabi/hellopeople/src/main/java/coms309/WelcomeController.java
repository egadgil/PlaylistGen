package coms309;

import org.springframework.web.bind.annotation.*;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Vivek Bengre
 */

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/{name}")
    public String welcomeUser(@PathVariable String name) {
        return "Hello and Welcome to 309 " + name + "!";
    }
//add
    @PostMapping("/add/{name}")
    public String addUser(@PathVariable String name) {
        return "Added User: " + name;
    }
//edit
    @PutMapping("/edit/{name}")
    public String editUser(@PathVariable String name) {
        return "Edited User: " + name;
    }


    @PostMapping("/add/{name}")
    public String addPetName(@PathVariable String name) {
        return "Added User: " + name;
    }
}
