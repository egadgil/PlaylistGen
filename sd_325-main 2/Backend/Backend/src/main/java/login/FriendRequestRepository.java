package login;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findBySenderAndReceiverAndAccepted(User sender, User receiver, boolean accepted);
    List<FriendRequest> findByReceiverAndAccepted(User receiver, boolean accepted);
}

