package login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepo;

    private Set<Song> songs = new HashSet<>();
    public void createPlaylist(Playlist playlist) {

        playlist.setUser(playlist.getUser());
        playlist.setPlaylistName(playlist.getPlaylistName());

        playlistRepo.save(playlist);
    }
    public List<Playlist> getTop10PlaylistsByLikes() {
        return playlistRepo.findTop10ByOrderByLikesDesc();
    }

    public Playlist findPlaylistById(Long id){
        return playlistRepo.getOne(id);
    }
    public List<Playlist> findByIspublic(boolean a){
        //find all where public is true
        a = true;
        return playlistRepo.findByIspublic(a);
    }


}
