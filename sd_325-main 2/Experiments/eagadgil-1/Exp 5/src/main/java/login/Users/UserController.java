package login.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, BindingResult bindingResult) {
        // Validation: Check if the provided user data is valid
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors");
        }

        // Check if the username or email is already in use
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        // Perform user registration
        userService.registerUser(user);

        return ResponseEntity.ok("User registered successfully");
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        // Implement login logic here, including checking credentials
        // You can use the userRepository to verify the user's credentials

        User existingUser = (User) userRepository.findByUsername(user.getUsername());
        if (existingUser == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect password");
        }

        // Successfully logged in
        return ResponseEntity.ok("Login successful");
    }

    // Retrieve UI Theme and Profile Picture endpoint
    @GetMapping("/profile/{username}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable String username) {
        // Find the user by their username
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Create a profile object containing UI theme and profile picture
        UserProfile userProfile = new UserProfile();
        userProfile.setUiThemeColor(user.getUiThemeColor());
        userProfile.setProfilePicture(user.getProfilePicture());

        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/updatePlaylist/{username}")
    public ResponseEntity<?> updateUserPlaylist(
            @PathVariable String username,
            @RequestBody List<String> playlist) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setPlaylist(playlist);
        userRepository.save(user);

        return ResponseEntity.ok("Playlist updated successfully");
    }
    @GetMapping("/getPlaylist/{username}")
    public ResponseEntity<?> getUserPlaylist(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> playlist = user.getPlaylist();

        return ResponseEntity.ok(playlist);
    }

}
