package login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private WebSocketController webSocketController;

    public void registerUser(User user) {
        // Hash the user's password before saving it (you may use a proper password hashing mechanism)
        String hashedPassword = user.getPassword();
        user.setPassword(hashedPassword);

        // Save the user to the database
        userRepository.save(user);
    }

    public List<String> returnAllUsernames() {
        List<String> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(user.getUsername()));
        return users;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUserInformation(User updatedUser) {
        Optional<User> existingUser = userRepository.findById(updatedUser.getId());

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Update user information based on the provided updatedUser
            user.setFullName(updatedUser.getFullName());
            user.setEmail(updatedUser.getEmail());
            user.setProfilePicture(updatedUser.getProfilePicture());
            user.setUiThemeColor(updatedUser.getUiThemeColor());
            user.setMembershipType(updatedUser.getMembershipType());
            user.setBlocked(updatedUser.getBlocked());

            // Save the updated user to the database
            userRepository.save(user);
        } else {
            // Handle the case where the user to be updated is not found
            // You may throw an exception or handle it based on your requirements
        }
    }

    public void sendFriendRequest(User sender, User receiver) {
        if (!areFriends(sender, receiver) && !hasPendingRequest(sender, receiver)) {
            FriendRequest friendRequest = new FriendRequest(sender, receiver);
            friendRequestRepository.save(friendRequest);

            // Notify the receiver about the friend request
            webSocketController.sendFriendRequestNotification(receiver.getId());
        }
    }

    public void acceptFriendRequest(User sender, User receiver) {
        List<FriendRequest> friendRequests = friendRequestRepository
                .findBySenderAndReceiverAndAccepted(sender, receiver, false);

        for (FriendRequest request : friendRequests) {
            request.setAccepted(true);
            friendRequestRepository.save(request);

        }

    }

    public void rejectFriendRequest(Long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId).orElse(null);
        if (friendRequest != null) {
            friendRequestRepository.delete(friendRequest);
        }
    }

    private boolean areFriends(User user1, User user2) {
        return user1.getFriends() != null && user1.getFriends().contains(user2);
    }

    private boolean hasPendingRequest(User sender, User receiver) {
        List<FriendRequest> pendingRequests = friendRequestRepository
                .findBySenderAndReceiverAndAccepted(sender, receiver, false);
        return !pendingRequests.isEmpty();
    }

    public List<FriendRequest> getFriendRequests(User user) {
        return friendRequestRepository.findByReceiverAndAccepted(user, false);

    }
}
