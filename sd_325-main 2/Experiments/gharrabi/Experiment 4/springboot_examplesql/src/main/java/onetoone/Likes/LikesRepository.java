package onetoone.Likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Vivek Bengre
 *
 */

public interface LikesRepository extends JpaRepository<Likes, Long> {

        Likes findById(int id);

        @Transactional
        void deleteById(int id);
    }
