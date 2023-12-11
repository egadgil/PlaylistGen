package login;


import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);

    User findByEmail(String email);

    boolean existsByUsername(String username);

    // You can add more custom query methods as needed for your application.
}
