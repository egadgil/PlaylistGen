package login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/api/notif/{username}")
@Component
public class BlockNotif {

    //session info
    private static Map<Session, String> sessionUsernamerMap = new HashMap<>();
    private static Map<String, Session> usernameSessionMap = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(BlockNotif.class);


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        logger.info("[onOpen] " + username);

        if(usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("user already blocked");
            session.close();
        }
        else{
            //map current session with username
            sessionUsernamerMap.put(session, username);
            usernameSessionMap.put(username,session);

            //immplemnet block functions
            sendMessageToParticularUser(username, username + " logged on websocket.");


            //map current username with session

        }
    }


    @OnMessage
    public void onMessage(Session session, String message) throws IOException{
        String username = sessionUsernamerMap.get(session);

        String[] mess = message.split(":");
        String userBlocked = mess[0];
        String status = mess[1];

        logger.info("[onMessage] " + userBlocked + ": " + status);
        if(status.equals("true")) {
            broadcast("user:" + userBlocked + "has been blocked. Reminder to follow our courtesy policies.");
        } else if(status.equals("false")) {
            broadcast("user:" + userBlocked + "has been unblocked. Reminder to follow our courtesy policies.");
        }


    }

    @OnClose
    public void onClose(Session session) throws IOException{
        String username = sessionUsernamerMap.get(session);

        logger.info("[onClose]" + username);
        sessionUsernamerMap.remove(session);
        usernameSessionMap.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        String username = sessionUsernamerMap.get(session);

        logger.info("[onError]" + username + throwable.getMessage());
    }

    private void sendMessageToParticularUser(String username, String message) throws IOException {
        try{
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        }
        catch(IOException e){
            logger.info("[Broadcast Exp]" + e.getMessage());
        }

    }

    private void broadcast(String message){
        sessionUsernamerMap.forEach((session, username) -> {
            try{
                session.getBasicRemote().sendText(message);
            } catch (IOException e){
                logger.info("[Broadcast Exception]" + e.getMessage());
            }
        });
    }


}
