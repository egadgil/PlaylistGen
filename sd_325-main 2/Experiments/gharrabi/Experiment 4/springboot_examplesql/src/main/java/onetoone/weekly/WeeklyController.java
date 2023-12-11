package onetoone.weekly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class WeeklyController {

    @Autowired
    WeeklyRepository weeklyRepository;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/weekly")
    List<Weekly> getAllweekly(){

        return weeklyRepository.findAll();

    }

    @GetMapping(path = "/weekly/{id}")
    Weekly getWeeklyById(@PathVariable int id){
        return weeklyRepository.findById(id);
    }

    @PostMapping(path = "/weekly")
    String createWeekly(Weekly weekly){
        if (weekly == null)
            return failure;
        weeklyRepository.save(weekly);
        return success;
    }

    @PutMapping(path = "/weekly/{id}")
    Weekly updateWeekly(@PathVariable int id, @RequestBody Weekly request){
        Weekly weekly = weeklyRepository.findById(id);
        if(weekly == null)
            return null;
        weeklyRepository.save(request);
        return weeklyRepository.findById(id);
    }

    @DeleteMapping(path = "/weekly/{id}")
    String deleteWeekly(@PathVariable int id) {


        // delete the Song if the changes have not been reflected by the above statement
        weeklyRepository.deleteById(id);
        return success;

    }
}
