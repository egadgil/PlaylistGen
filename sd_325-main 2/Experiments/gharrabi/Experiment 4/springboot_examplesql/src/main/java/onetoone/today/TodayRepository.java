package onetoone.today;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TodayRepository extends JpaRepository<Today,Long> {
    Today findById(int id);

    @Transactional
    void deleteById(int id);
}
