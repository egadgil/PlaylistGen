package login;



import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;

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

    @Column(name = "font")
    private String font;

    private String profilePicture; // Store the path or URL of the profile picture
    private String uiThemeColor;    // Store the UI theme color

    private String membershipType;
    private String blocked;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Playlist> playlist;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<PaymentInfo> payments;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends;

    //keep track of liked playlists
    //only one playlist be liked
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "playlist_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    Set<Playlist> likedPlaylists;

    @JsonIgnore
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<FriendRequest> receivedFriendRequests;

    @JsonIgnore
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<FriendRequest> sentFriendRequests;

    // Constructors, getters, and setters...
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Leaderboard leaderboard;
    public User() {
    }



    public User(String username, String password, String fullName, String email, String profilePicture, String uiThemeColor, String membershipType, String blocked) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.uiThemeColor = uiThemeColor;
        this.membershipType = membershipType;
        this.blocked = blocked;
    }

    // Existing methods...


    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public List<PaymentInfo> getPayments() {
        return payments;
    }

    public void addPayment(PaymentInfo paymentInfo){
        this.payments.add(paymentInfo);
    }

    public void setPayments(List<PaymentInfo> payments) {
        this.payments = payments;
    }

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

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUiThemeColor() {
        return uiThemeColor;
    }

    public void setUiThemeColor(String uiThemeColor) {
        this.uiThemeColor = uiThemeColor;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public List<Playlist> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<Playlist> playlist) {
        this.playlist = playlist;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<FriendRequest> getReceivedFriendRequests() {
        return receivedFriendRequests;
    }

    public void setReceivedFriendRequests(List<FriendRequest> receivedFriendRequests) {
        this.receivedFriendRequests = receivedFriendRequests;
    }

    public List<FriendRequest> getSentFriendRequests() {
        return sentFriendRequests;
    }

    public void setSentFriendRequests(List<FriendRequest> sentFriendRequests) {
        this.sentFriendRequests = sentFriendRequests;
    }
    public void addFriend(User friend) {
        this.friends.add(friend);
    }

    public Set<Playlist> getLikedPlaylists() {
        return likedPlaylists;
    }

    public void setLikedPlaylists(Set<Playlist> likedPlaylists) {
        this.likedPlaylists = likedPlaylists;
    }
    public void addLikedPlaylist(Playlist playlist){
        this.likedPlaylists.add(playlist);
    }
    public void removeLikedPlaylist(Playlist playlist){
        this.likedPlaylists.remove(playlist);
    }
    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
        if (leaderboard != null) {
            leaderboard.setUser(this);
        }
    }
}
