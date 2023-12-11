package onetoone.leaderboard;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;

public interface LeaderBoardRepository extends JpaRepository <Leaderboard,Long>{
    Leaderboard findById(int id);

    @Transactional
    void deleteById(int id);
}
