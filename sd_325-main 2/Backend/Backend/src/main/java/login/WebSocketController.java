// WebSocketController.java

package login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/api/topic/friend-request")
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendFriendRequestNotification(Long userId) {
        messagingTemplate.convertAndSendToUser(userId.toString(), "/topic/friend-request", "You have a new friend request!");
    }
}

