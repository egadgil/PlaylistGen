package login.Users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void registerUser(User user) {
        // Hash the user's password before saving it
        String hashedPassword = user.getPassword();
        user.setPassword(hashedPassword);

        // Set the email, full name, and username
        user.setEmail(user.getEmail());
        user.setFullName(user.getFullName());
        user.setUsername(user.getUsername());

        // Save the user to the database
        userRepository.save(user);
    }




}
