package onetoone.weekly;
import onetoone.today.Today;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface WeeklyRepository extends JpaRepository<Weekly,Long> {

    Weekly findById(int id);

    @Transactional
    void deleteById(int id);
}
