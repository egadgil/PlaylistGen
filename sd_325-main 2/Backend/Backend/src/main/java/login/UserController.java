package login;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Api(tags = "User Controller", description = "Operations related to user management")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @ApiOperation("User registration-Esha Gadgil")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User registered successfully"),
            @ApiResponse(code = 400, message = "Validation errors or Username/Email already in use")
    })
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
    @ApiOperation("User login-Esha Gadgil")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login successful"),
            @ApiResponse(code = 400, message = "User not found, Incorrect password, or User banned")
    })
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

        if(existingUser.getBlocked() != null){
            if(existingUser.getBlocked().equals("true")){
                return ResponseEntity.badRequest().body("User banned");

            }
        }
        // Successfully logged in
        return ResponseEntity.ok("Login successful");
    }

    // Retrieve UI Theme and Profile Picture endpoint
    @ApiOperation("sets profile picture for user - Megan Boyd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "profile profile picture set for user"),
            @ApiResponse(code = 400, message = "user not found")
    })
    @PostMapping("/set_profile")
    public ResponseEntity<?> setUserProfile
    (@RequestParam String username, @RequestParam String profile_pic){

        User user = (User) userRepository.findByUsername(username);

        if(user == null){

            return ResponseEntity.badRequest().body("User not found");
        }

        user.setProfilePicture(profile_pic);
        userRepository.save(user);

        return ResponseEntity.ok(user);


    }

    @ApiOperation("sets ui theme for user - Megan Boyd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ui theme set for user"),
            @ApiResponse(code = 400, message = "user not found")
    })
    @PostMapping("/set_ui")
    public ResponseEntity<?> setUserUiTheme
            (@RequestParam String username, @RequestParam String uiTheme){


        User user = (User) userRepository.findByUsername(username);

        if(user == null){

            return ResponseEntity.badRequest().body("User not found");
        }

        user.setUiThemeColor(uiTheme);
        userRepository.save(user);

        return ResponseEntity.ok(user);

    }
    @ApiOperation("Set user membership type-Esha Gadgil")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Membership type set successfully"),
            @ApiResponse(code = 400, message = "User not found")
    })
    @PostMapping("/set_membership_type")
    public ResponseEntity<?> setUserMembershipType(@RequestParam String username, @RequestParam String membershipType) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        user.setMembershipType(membershipType);
        userRepository.save(user);

        return ResponseEntity.ok("Membership type set successfully");
    }
    @ApiOperation("Get user membership type-Esha Gadgil")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user membership type"),
            @ApiResponse(code = 400, message = "User not found")
    })
    @GetMapping("/get_membership_type")
    public ResponseEntity<?> getUserMembershipType(@RequestParam String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String membershipType = user.getMembershipType();
        return ResponseEntity.ok(membershipType);
    }

    @ApiOperation("gets user selected ui theme - Megan Boyd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ui theme retrieved"),
            @ApiResponse(code = 400, message = "user not found")
    })
    @GetMapping("/get_ui_theme")
    public ResponseEntity<?> getUserUITheme(@RequestParam String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String uiThemeColor = user.getUiThemeColor();
        return ResponseEntity.ok(uiThemeColor);
    }

    // Get Profile Picture endpoint
    @ApiOperation("retrieves profile picture user selected - Megan Boyd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "profile picture retrieved for user"),
            @ApiResponse(code = 400, message = "user not found")
    })
    @GetMapping("/get_profile_picture")
    public ResponseEntity<?> getUserProfilePicture(@RequestParam String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String profilePicture = user.getProfilePicture();
        return ResponseEntity.ok(profilePicture);
    }

    @ApiOperation("returns an entire user entity for username - Megan Boyd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "user retrieved"),
            @ApiResponse(code = 400, message = "user not found")
    })
    @PostMapping("/set_user_info")
    public ResponseEntity<?> setUserInfo(@RequestParam String username){
        User user = userRepository.findByUsername(username);

        if(user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        return ResponseEntity.ok(user);
    }


    @ApiOperation("sets ban for user - Megan Boyd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ban successfully set"),
            @ApiResponse(code = 400, message = "user not found")
    })
    @PostMapping("set_ban")
    public ResponseEntity<?> ban(@RequestParam String username, @RequestParam String ban){
        User user = userRepository.findByUsername(username);
        if(user == null){
            return ResponseEntity.badRequest().body("User not found");
        }

        if(ban !=null){
            if(ban.equals("true")){
                user.setBlocked("true");
            }
            else if(ban.equals("false")){
                user.setBlocked("false");

            }
        }


        userRepository.save(user);
        return ResponseEntity.ok(user);

    }

    @ApiOperation("retrieves ban status for user - Megan Boyd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ban status retrieved"),
            @ApiResponse(code = 400, message = "user not found")
    })
    @GetMapping("get_ban")
    public String getBan(@RequestParam String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            return "user not found";
        }

        if(user.getBlocked() == "true"){
            return "true";
        }
        else{
            return "false";
        }
    }
    @ApiOperation("Get all usernames-Esha Gadgil")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all usernames"),
    })
    @GetMapping("get_usernames")
    public List<String> getAllUsernames(){

        return userService.returnAllUsernames();
    }

    @PostMapping("/send-friend-request")
    public ResponseEntity<?> sendFriendRequest(
            @RequestParam String senderUsername,
            @RequestParam String receiverUsername) {

        User sender = userRepository.findByUsername(senderUsername);
        User receiver = userRepository.findByUsername(receiverUsername);

        if (sender == null || receiver == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        userService.sendFriendRequest(sender, receiver);

        return ResponseEntity.ok("Friend request sent successfully");
    }

    @GetMapping("/friend-requests/{username}")
    public ResponseEntity<List<FriendRequest>> getFriendRequests(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<FriendRequest> friendRequests = userService.getFriendRequests(user);
        return ResponseEntity.ok(friendRequests);
    }

    @PostMapping("/accept-friend-request")
    public ResponseEntity<?> acceptFriendRequest(
            @RequestParam String senderUsername,
            @RequestParam String receiverUsername) {

        User sender = userRepository.findByUsername(senderUsername);
        User receiver = userRepository.findByUsername(receiverUsername);

        if (sender == null || receiver == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        userService.acceptFriendRequest(sender,receiver);
        sender.addFriend(receiver);
        receiver.addFriend(sender);
        userRepository.save(sender);
        userRepository.save(receiver);

        return ResponseEntity.ok("Friend request accepted");
    }

    @PostMapping("/set_font")
    ResponseEntity<?> setFont(@RequestParam String font, @RequestParam String username){
        User user = userRepository.findByUsername(username);

        if(user == null){
            return ResponseEntity.badRequest().body("User does not exist");
        }
        else {
            user.setFont(font);
            userRepository.save(user);
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/get_font")
    ResponseEntity<?> getFont(@RequestParam String username){
        User user = userRepository.findByUsername(username);

        if(user == null) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
        else {
            return ResponseEntity.ok(user.getFont());
        }
    }
    @Autowired
    private LeaderboardService leaderboardService;

    @PostMapping("/create_user_with_leaderboard")
    public ResponseEntity<?> createUserWithLeaderboard(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }

        Leaderboard leaderboard = new Leaderboard(user, 0);
        user.setLeaderboard(leaderboard);

        userRepository.save(user);

        return ResponseEntity.ok("User with leaderboard created successfully");
    }

    @GetMapping("/get_user_with_leaderboard/{username}")
    public ResponseEntity<?> getUserWithLeaderboard(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Leaderboard leaderboard = leaderboardService.getLeaderboardByUserId(user);

        if (leaderboard == null) {
            // Handle scenario where leaderboard is not found for the user
            return ResponseEntity.badRequest().body("Leaderboard not found for the user");
        }

        // Exclude unnecessary details to avoid potential circular dependencies
        leaderboard.setUser(null);
        user.setLeaderboard(leaderboard);

        return ResponseEntity.ok(user);
    }


}



