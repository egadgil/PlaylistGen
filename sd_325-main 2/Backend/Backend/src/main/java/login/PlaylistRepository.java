package login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {


    @Query(value = "SELECT * FROM playlist WHERE public = 1 ORDER BY likes DESC LIMIT 10;", nativeQuery = true)
    List<Playlist> findTop10ByOrderByLikesDesc();

    List<Playlist> findByIspublic(boolean a);
}
