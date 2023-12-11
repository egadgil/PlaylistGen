package login.Users;

import javax.persistence.*;
import javax.sound.midi.Track;
import java.util.List;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    private String profilePicture; // Store the path or URL of the profile picture
    private String uiThemeColor;    // Store the UI theme color
    private List<String> playlist;

    // Constructors, getters, and setters

    public User() {
    }

    public User(String username, String password, String fullName, String email, String profilePicture, String uiThemeColor,List<String> playlist) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.uiThemeColor = uiThemeColor;
        this.playlist=playlist;
    }

    // Getter and setter methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }



    public String getUiThemeColor() {
        return uiThemeColor;
    }
    // Getter and setter methods for the 'playlist' field
    public List<String> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<String> playlist) {
        this.playlist = playlist;
    }


}
