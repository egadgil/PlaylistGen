package onetoone.leaderboard;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController

public class LeaderBoardController {


    @Autowired
    LeaderBoardRepository leaderboardRepository;

    //@Autowired
   //  LeaderBoardRepository leaderboardRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/leaderboard")
    List<Leaderboard> getAllLeads(){

            return leaderboardRepository.findAll();

    }

    @GetMapping(path = "/leaderboard/{id}")
    Leaderboard getLeadById(@PathVariable int id){
        return leaderboardRepository.findById(id);
    }

    @PostMapping(path = "/leaderboard")
    String createLead(Leaderboard Lead){
        if (Lead == null)
            return failure;
        leaderboardRepository.save(Lead);
        return success;
    }

    @PutMapping(path = "/leaderboard/{id}")
    Leaderboard updateLead(@PathVariable int id, @RequestBody Leaderboard request){
        Leaderboard leaderboard = leaderboardRepository.findById(id);
        if(leaderboard == null)
            return null;
        leaderboardRepository.save(request);
        return leaderboardRepository.findById(id);
    }

    @DeleteMapping(path = "/leader/{id}")
    String deleteLead(@PathVariable int id) {


        // delete the Song if the changes have not been reflected by the above statement
        leaderboardRepository.deleteById(id);
        return success;

    }
}
