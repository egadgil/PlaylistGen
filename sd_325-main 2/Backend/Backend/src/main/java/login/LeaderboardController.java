package login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaderboards")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create/{username}")
    public ResponseEntity<?> createLeaderboardForUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Check if the user already has a leaderboard
        if (leaderboardService.doesLeaderboardExist(user)) {
            return ResponseEntity.badRequest().body("Leaderboard already exists for the user");
        }

        Leaderboard leaderboard = new Leaderboard(user, 0);
        leaderboardService.saveLeaderboard(leaderboard);

        return ResponseEntity.ok("Leaderboard created successfully for the user");
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<?> getLeaderboardForUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Leaderboard leaderboard = leaderboardService.getLeaderboardByUserId(user);

        if (leaderboard == null) {
            return ResponseEntity.badRequest().body("Leaderboard not found for the user");
        }

        // Exclude unnecessary details to avoid potential circular dependencies
        leaderboard.getUser().setLeaderboard(null);

        return ResponseEntity.ok(leaderboard);
    }
}
