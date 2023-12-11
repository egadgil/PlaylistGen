package onetoone.Songs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author
 * 
 */ 

public interface SongRepository extends JpaRepository<Song, Long> {
    Song findById(int id);

    @Transactional
    void deleteById(int id);
}
