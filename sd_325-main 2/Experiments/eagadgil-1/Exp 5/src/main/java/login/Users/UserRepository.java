package login.Users;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);

    User findByEmail(String email);

    // You can add more custom query methods as needed for your application.
}
