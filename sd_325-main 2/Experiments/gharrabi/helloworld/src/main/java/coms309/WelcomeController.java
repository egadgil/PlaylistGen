package coms309;

import org.springframework.web.bind.annotation.*;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/home/{name}")
    public String test(@PathVariable String name) {
        return "This is " + name + "'s Test";

    }
    @PutMapping("/home/{name}/project")
    public String project(@PathVariable String name) {
        return "This is " + name + "'s Project";
    }
    @PostMapping("/home")
    public String goodbye() {
        return "This is a goodbye from COMS 309";
    }
    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome to COMS 309: " + name;
    }


    @GetMapping ("/home/text/{name}")
    public String text(@PathVariable String name){
        return "this is " + name + " path";}
    }

