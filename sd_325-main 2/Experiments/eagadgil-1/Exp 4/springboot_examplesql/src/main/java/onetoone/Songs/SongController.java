package onetoone.Songs;

import java.util.List;

import onetoone.Artists.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import onetoone.Artists.Artist;
import onetoone.Artists.ArtistRepository;


/**
 * 
 * @author
 * 
 */ 

@RestController
public class SongController {

    @Autowired
    SongRepository songRepository;
    ArtistRepository artistRepository;


    
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/Songs")
    List<Song> getAllSongs(){
        return songRepository.findAll();
    }

    @GetMapping(path = "/Songs/{id}")
    Song getSongById(@PathVariable int id){
        return songRepository.findById(id);
    }

    @PostMapping(path = "/Songs")
    String createSong(Song Song){
        if (Song == null)
            return failure;
        songRepository.save(Song);
        return success;
    }

    @PutMapping(path = "/Songs/{id}")
    Song updateSong(@PathVariable int id, @RequestBody Song request){
        Song Song = songRepository.findById(id);
        if(Song == null)
            return null;
        songRepository.save(request);
        return songRepository.findById(id);
    }

    @DeleteMapping(path = "/Songs/{id}")
    String deleteSong(@PathVariable int id){


        // delete the Song if the changes have not been reflected by the above statement
        songRepository.deleteById(id);
        return success;
    }
}
