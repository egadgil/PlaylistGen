package onetoone.today;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodayController {

    @Autowired
    TodayRepository todayRepository;

    //@Autowired
    //  LeaderBoardRepository leaderboardRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/today")
    List<Today> getAlltoday(){

        return todayRepository.findAll();

    }

    @GetMapping(path = "/today/{id}")
    Today getTodayById(@PathVariable int id){
        return todayRepository.findById(id);
    }

    @PostMapping(path = "/today")
    String createToday(Today today){
        if (today == null)
            return failure;
        todayRepository.save(today);
        return success;
    }

    @PutMapping(path = "/today/{id}")
    Today updateToday(@PathVariable int id, @RequestBody Today request){
        Today today = todayRepository.findById(id);
        if(today == null)
            return null;
        todayRepository.save(request);
        return todayRepository.findById(id);
    }

    @DeleteMapping(path = "/today/{id}")
    String deleteToday(@PathVariable int id) {


        // delete the Song if the changes have not been reflected by the above statement
        todayRepository.deleteById(id);
        return success;

    }
}
