
package login;
import login.Leaderboard;
import login.LeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaderboardService {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    public Leaderboard getLeaderboardByUserId(User user) {
        return leaderboardRepository.findByUser(user);
    }

    public void saveLeaderboard(Leaderboard leaderboard) {
        leaderboardRepository.save(leaderboard);
    }

    public boolean doesLeaderboardExist(User user) {
        return leaderboardRepository.existsByUser(user);
    }

    // Other methods for leaderboard-related operations...

    // You might want to add additional methods based on your requirements
}
