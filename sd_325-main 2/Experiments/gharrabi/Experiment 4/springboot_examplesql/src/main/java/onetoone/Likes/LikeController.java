package onetoone.Likes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Users.User;
import onetoone.Users.UserRepository;

import static onetoone.Likes.LikesRepository.*;

/**
 *
 * @author Vivek Bengre
 *
 */

@RestController
public class LikeController {

    @Autowired
    LikesRepository likesRepository;

   // @Autowired
   // LikesRepository likesRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/likes")
    List<Likes> getAllLikes(){
        return likesRepository.findAll();
    }

    @GetMapping(path = "/likes/{id}")
    Likes getLikesById(@PathVariable int id){
        return likesRepository.findById(id);
    }

    @PostMapping(path = "/likes")
    String createLikes(Likes Likes){
        if (Likes == null)
            return failure;
        likesRepository.save(Likes);
        return success;
    }

    @PutMapping(path = "/likes/{id}")
    Likes updateLikes(@PathVariable int id, @RequestBody Likes request){
        Likes likes = likesRepository.findById(id);
        if(likes == null)
            return null;
        likesRepository.save(request);
        return likesRepository.findById(id);
    }

    @DeleteMapping(path = "/likes/{id}")
    String deleteLikes(@PathVariable int id) {


        // delete the Song if the changes have not been reflected by the above statement
        likesRepository.deleteById(id);
        return success;

    }
}



