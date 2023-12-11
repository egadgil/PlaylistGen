package login;
//import local classes
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Frequency;


// Import Java libraries
import java.util.*;

// import junit/spring tests
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestingControllerIntegration {

    @LocalServerPort
    int port;


    List<String> songs;
    String playlist_name;
    int playlist_length;
    String username;
    String playlist_id;
    @Before
    public void setUp(){
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

    }

    @Test
    public void test() throws Exception {

        Response response = RestAssured
                .with()
                .param("playlist_id","1")
                .param("name","assert_equals")
                .request("POST","/api/playlist/rename");

        int statusCode = response.getStatusCode();

        assertEquals(200,statusCode);

        String responseString = response.getBody().asString();

        JSONObject jsonResp = (JSONObject) new JSONObject(responseString);

        String check = jsonResp.getString("playlistName");


        assertEquals(check,"assert_equals");
    }



    @Test
    public void testPlaylistLength() throws Exception {
        Response response = RestAssured
                .with()
                .param("type", "song")
                .param("query", "1ZozGivTAYsOwhy6LVHsPX")
                .param("username", "TESTING")
                .param("playlist_name", "test")
                .param("playlist_length", "9")
                .request("POST", "/api/playlist/gen");


        int statusCode = response.getStatusCode();

        assertEquals(200, statusCode);
        String responseString = response.getBody().asString();
        ArrayList<String> q = new ArrayList<>();

        List<HashMap<String, String>> list_songs = new ArrayList<>();

        int counter = 0;
        JSONObject playlist;
        try {
            playlist = new JSONObject(responseString);
            JSONArray songs = (JSONArray) playlist.getJSONArray("playlist_songs");

            for (int i = 0; i < songs.length(); i++) {
                counter++;
                HashMap<String, String> song_artist_map = new HashMap<>();
                JSONObject temp = new JSONObject();
                temp = songs.getJSONObject(i);


                song_artist_map.put(temp.getString("name"), temp.getString("artistName"));

                list_songs.add(song_artist_map);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        for (int j = 0; j < list_songs.size(); j++) {
            HashMap<String, String> temphash = new HashMap<>();
            temphash = list_songs.get(j);
            String str = temphash.toString();
            str.replace("}", "");
            str.replace("{", "");
            String[] list_str = str.split("=");
            q.add(list_str[0].replace("{", ""));
        }
        playlist_name = playlist.getString("playlistName");
        playlist_id = String.valueOf(playlist.getLong("playlistId"));
        playlist_length = counter;
        username = playlist.getString("username");


        assertEquals(username, "TESTING");
        assertEquals(playlist_length, 9);
        assertEquals(playlist_name, "test");

        deletePlaylist();
    }

    @Test
    public void testPublicStatus(){
        Response response = RestAssured
                .with()
                .param("set_public", "true")
                .param("playlist_id", "1")
                .request("POST", "/api/playlist/set_public");


        int statusCode = response.getStatusCode();

        assertEquals(200, statusCode);
        String responseString = response.getBody().asString();

        JSONObject playlist = new JSONObject(responseString);

        assertTrue(playlist.getBoolean("is_public"));
    }

    @Test
    public void testAddSong(){
        Response response = RestAssured
                .with()
                .param("playlist_id", "1")
                .param("song_name", "Money Trees")
                .param("spotify_id","2HbKqm4o0w5wEeEFXm2sD4")
                .param("artist_name","Kendrick Lamar, Jay Rock")
                .request("POST", "/api/playlist/add_song");


        int statusCode = response.getStatusCode();

        assertEquals(200, statusCode);
    }

    @Test
    public void testRemoveSong(){
        Response response = RestAssured
                .with()
                .param("playlist_id", "1")
                .param("song_name", "Money Trees")
                .param("spotify_id","2HbKqm4o0w5wEeEFXm2sD4")
                .param("artist_name","Kendrick Lamar, Jay Rock")
                .request("POST", "/api/playlist/remove_song");

        int statusCode = response.getStatusCode();

        assertEquals(200, statusCode);
    }

    @Test
    public void testPreview(){
        Response response = RestAssured
                .with()
                .param("song_name", "Money Trees")
                .request("POST", "/api/playlist/get_preview");


        int statusCode = response.getStatusCode();

        assertEquals(200, statusCode);
    }

    @Test
    public void getLikedPlaylist(){
        Response response = RestAssured
                .with()
                .param("username", "mb")
                .request("POST", "/api/playlist/liked_playlists");


        int statusCode = response.getStatusCode();

        assertEquals(200, statusCode);
    }

    @Test
    public void testTopLikes(){
        Response response = RestAssured
                .with()
                .request("GET", "/api/playlist/top-likes");


        int statusCode = response.getStatusCode();

        assertEquals(200, statusCode);
    }

    @Test
    public void testLike(){
        Response response = RestAssured
                .with()
                .params("playlistId","1")
                .param("username","mb")
                .request("POST", "/api/playlist/like");


        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        System.out.println(body);

        int statusCode2 = 400;

        if(statusCode == 400){
            Response response2 = RestAssured
                    .with()
                    .params("playlistId","1")
                    .param("username","mb")
                    .request("POST", "/api/playlist/remove_like");

            statusCode2 = response2.getStatusCode();

        }

        System.out.println(statusCode);
        System.out.println(statusCode2);
        boolean checker = (statusCode2 == 200) || (statusCode == 200);

        assertTrue(checker);
       // assertEquals(200, statusCode);

    }

    @Test
    public void testUserPlaylist(){
        Response response = RestAssured
                .with()
                .param("username","mb")
                .request("GET", "/api/playlist/user_playlists");


        int statusCode = response.getStatusCode();

        assertEquals(200, statusCode);
    }
    @Test
    @Query(value ="Delete from playlist_songs where \n" +
            "playlist_id = (select playlist_id from playlist where playlist.username = \"TESTING\" limit 1);\n" +
            "DELETE from playlist where playlist.username = \"TESTING\";",nativeQuery = true)
    public void deletePlaylist(){
    }


}
