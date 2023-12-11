package onetoone.Artists;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Songs.Song;
import onetoone.Songs.SongRepository;

/**
 *
 * @author Vivek Bengre
 *
 */

@RestController
public class ArtistController {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    SongRepository songRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/Artists")
    List<Artist> getAllArtists(){
        return artistRepository.findAll();
    }

    @GetMapping(path = "/Artists/{id}")
    Artist getArtistById( @PathVariable int id){
        return artistRepository.findById(id);
    }

    @PostMapping(path = "/Artists")
    String createArtist(Artist artist){
        if (artist == null)
            return failure;
        artistRepository.save(artist);
        return success;
    }

    @PutMapping("/Artists/{id}")
    Artist updateArtist(@PathVariable int id, @RequestBody Artist request){
        Artist artist = artistRepository.findById(id);
        if(artist == null)
            return null;
        artistRepository.save(request);
        return artistRepository.findById(id);
    }

    @PutMapping("/Artists/{artistId}/Songs/{songId}")
    String assignSongToArtist(@PathVariable int artistId,@PathVariable int songId){
        Artist artist = artistRepository.findById(artistId);
        Song song = songRepository.findById(songId);
        if(song == null ||artist == null)
            return failure;
        song.setArtist(artist);
        artist.setSong(song);
        artistRepository.save(artist);
        return success;
    }

    @DeleteMapping(path = "/Artists/{id}")
    String deleteArtist(@PathVariable int id){
        artistRepository.deleteById(id);
        return success;
    }
}
