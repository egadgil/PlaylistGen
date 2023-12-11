package login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepo;

    public Song findSongById(Long id){
        return songRepo.getOne(id);
    }

    public Song findSongByName(String name){
        return songRepo.findByName(name);
    }
}
