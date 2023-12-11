package login;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
//configurationprocessor.json.JSONArray;
//import org.springframework.boot.configurationprocessor.json.JSONException;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/playlist")
@Api(tags = "Playlist Controller", description = "Operations related to playlist creation and management")

public class PlaylistController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SongService songService;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private PlaylistRepository playlistRepo;

    //access token
    private String token = "";
    private String user_token = "";


    //app's credentials
    private final String client_id ="c1674e1ab75943bd8b230ee88a57f47f";
    private final String client_secret = "ef45d175b89e4cbbbe22879df19fce63";

    @ApiOperation("change playlist name")
    @PostMapping("/name")
    public ResponseEntity<?> changeName(@RequestParam String playlist_id, @RequestParam String name){

        Playlist playlist;

        //search for playlist id
        if(playlistRepo.existsById(Long.parseLong(playlist_id)))
        {
            //set playlist
            playlist = playlistRepo.getOne(Long.parseLong(playlist_id));
        }
        else {
            return ResponseEntity.badRequest().body("playlist id does not exist");
        }

        playlist.setPlaylistName(name);

        return ResponseEntity.ok(playlist);

    }

    //generates a playlist with type (song/artist) user specified
    //grabs username for relevant mapping
    //android volley may only post json/ string
    @ApiOperation("generates playlist based on users selection of song/artist - Megan Boyd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "playlist generated"),
            @ApiResponse(code = 400, message = "user not found")
    })
    @PostMapping("/gen")
    public ResponseEntity<?> generatePlaylist(@RequestParam String type,
                                     @RequestParam String query,
                                     @RequestParam String username,
                                     @RequestParam String playlist_name,
                                     @RequestParam String playlist_length)  throws
            URISyntaxException, IOException, InterruptedException, JSONException {

        User user = userRepository.findByUsername(username);

       //return user not found error if user is not in repo
        if (user == null) {
            JSONObject obj = new JSONObject();
            obj.put("error", "user not found");
            return ResponseEntity.badRequest().body(obj.toString());
        }

        //create a new playlist object and map it to user
        Playlist playlist = new Playlist(user, playlist_name);


        String artistName ="";
        String artist_id = "";


        //create hash maps for :
        //            artist_map - artist name = artist spotify id
        //            song_map   - song name = artist name
        //            song_id     - song name = song spotify id
        List<HashMap<String,String>> artist_map = new ArrayList<>();
        List<HashMap<String, String>> song_map = new ArrayList<>();
        List<HashMap<String,String>> song_id = new ArrayList<>();



        //grab token
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "client_credentials");

        //encode parameters
        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String header = client_id + ":" + client_secret;

        //build http request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI("https://accounts.spotify.com/api/token"))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + Base64.getEncoder().encodeToString(header.getBytes()))
                .POST(HttpRequest.BodyPublishers.ofString(form)).build();

        //send http request
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());

        //response returned as json object
        JSONObject obj = new JSONObject(response.body());

        //grab value for access_token field, which we may then use to make requests
        token = obj.getString("access_token");


        //if type is song
        //query song and find artist id
        if(type.equals("song") || type.equals("songs")){


            JSONObject arr = httpHelper("https://api.spotify.com/v1/tracks/" + query, token);

            //create a new json array full of artist info
            System.out.println(arr.toString());
            JSONArray artist = arr.getJSONArray("artists");
            //JSONArray artist = new JSONArray(arr.getString("artists"));
            //JSONArray artist = new JSONArray(arr.getJSONArray("artists"));

            //json object external urls contains the name of the artist, so create the object
            JSONObject extern_url = new JSONObject(String.valueOf(artist.getJSONObject(0)));

            //print artist name and id
            //here the key is "name" and we're extracting the value, "Taylor Swift"
            artistName = extern_url.getString("name");
            artist_id = extern_url.getString("id");
            query = artist_id;

        } else if(type.equals("artist")) {
            artist_id = query;
        } else {
            JSONObject err = new JSONObject();
            obj.put("error", "hmm...looks like you've entered an unsupported type. supported types are artist and track.");
            return ResponseEntity.badRequest().body(obj.toString());
        }
        
        //code to get similar songs
        //to find similar artists use get /artists/{id}/related artists
        JSONObject main = httpHelper("https://api.spotify.com/v1/artists/" + query + "/related-artists", token);


        JSONArray artistArr = (JSONArray) main.get("artists");
        System.out.println(artistArr);

        //for each artist
        for(int i = 0; i < artistArr.length(); i++){
            JSONObject json_object = (JSONObject) artistArr.get(i);
            String temp_name = json_object.getString("name");
            String temp_id = json_object.getString("id");

            //list of hash maps
            //artist name = artist id
            //useful for sql mappings
            HashMap<String,String> map = new HashMap<>();
            map.put(temp_name, temp_id);
            artist_map.add(map);
        }

        for (HashMap<String, String> stringStringHashMap : artist_map) {
            System.out.println(stringStringHashMap);
        }

        //from each artist id (get /artists/{id}
        //grab top tracks
        //get /artists/{id}/top-tracks
        for(HashMap<String, String> stringStringHashMap : artist_map){
            String str = stringStringHashMap.toString();
            String[] strArr = str.split("=");
            query = strArr[1].replace("}","");

            //top tracks req needs country parameter

            JSONObject resp = httpHelper("https://api.spotify.com/v1/artists/" + query + "/top-tracks" + "?market=US", token);

            //JSONArray trackArr = new JSONArray(resp.getString("tracks"));
            JSONArray trackArr = resp.getJSONArray("tracks");


            String artist_str = "";

            for(int i = 0; i < trackArr.length(); i++){
                JSONObject temp_js = (JSONObject) trackArr.get(i);

                JSONArray art = temp_js.getJSONArray("artists");


                //store multiple artist as one in our db, separated by comma
                for(int j = 0; j < art.length(); j++){
                    JSONObject gd = (JSONObject)art.get(j);
                    if(j != art.length() - 1){
                        artist_str = artist_str.concat(gd.getString("name") + ", ");
                    }else {
                        artist_str = artist_str.concat(gd.getString("name"));
                    }
                }

                //get some name and id
                String song_name = temp_js.getString("name");
                String song_spotify_id = temp_js.getString("id");

                //map song name and artist
                HashMap<String,String> map = new HashMap<>();
                map.put(song_name, artist_str);
                song_map.add(map);

                HashMap<String,String> spotifyM = new HashMap<>();
                spotifyM.put(song_name, song_spotify_id);
                song_id.add(spotifyM);

                song_name = "";
                artist_str = "";

            }
        }

        //print out all hash values for song name = artist
        for(HashMap<String, String> stringStringHashMap : song_id){
            System.out.println(stringStringHashMap);
        }

        System.out.println("SHUFFLING....");
        //shuffle songs
        //add the first 15 to a playlist
        Collections.shuffle(song_map);

        // list is shuffled :)
        for(HashMap<String, String> stringStringHashMap : song_map){
            System.out.println(stringStringHashMap);
        }

        int length = Integer.parseInt(playlist_length);
        if (song_map.size() < length){
            length = song_map.size();
        }
        
        playlistRepo.save(playlist);

        Set<Song> songs = new HashSet<Song>();
        for(int i = 0; i < length; i++){
            //try printing out specifc song and artist
            String temp = song_map.get(i).toString();
            temp = temp.replace("{", "");
            temp = temp.replace("}", "");

            //hash map stored like: SONG NAME = ARTISTS
            //this will not work if song or artist contain an equal sign
            //so proceed with cation and look into better way to parse hashies
            String[] values = temp.split("=");
            String name = values[0];
            String artist = values[1];

            //make artist object
            //search for existing artist by name
            //if doesnt exist, make a new one
            Artist ar;

            if(artistRepo.findByArtistname(artist) == null){
                System.out.println("new artist");
                ar = new Artist(artist, songs);
                artistRepo.save(ar);
            } else {
                System.out.println("old artist");
                ar = (Artist) artistRepo.findByArtistname(artist);
            }
            System.out.println(artist);
            System.out.println(ar.getArtistname());

            Song s;
            //search for song s
            //create new song if not found

            //song_map was shuffled -> song_id was NOT
            String searchName = song_map.get(i).toString();
            String[] tempSt = searchName.split("=");
            searchName = tempSt[0].replace("{","");
            System.out.println(searchName);
            String temp_id = "";
            for(Map<String,String> te : song_id){
                if(te.containsKey(searchName)){
                    temp_id = te.get(searchName);
                    System.out.println("a match " + temp_id);
                }
            }
           // String temp_id = song_id.get(i).toString();
            System.out.println("TEMP ID" + temp_id);
            //String[] ids = temp_id.split("=");
            //String id = ids[1];
            //id = id.replace("}", "");
            String id = temp_id;
            //FIGURE OUT HOW TO IMPLEMENT MANY TO MANY BETWEEN PLAYLIST AND SONGS
            if(songRepo.findByName(name) == null){
                s = new Song(name, ar, id);
                // s.addPlaylist(playlistRepo.getOne(playlist.getPlaylistId()));
                songRepo.save(s);

            } else {
                s = (Song) songRepo.findByName(name);
            }

            playlist.addSong(s);
            songs.add(s);
            ar.addSong(s);
            artistRepo.save(ar);
            s.setArtist(ar);
            s.setArtistName(artist);

            System.out.println("adding "+ name + " by " + artist);
        }
        //playlist.setSongs(songs);
        playlist.setUsername(username);
        //public by default
        playlist.setIs_public(true);
        playlistRepo.save(playlist);

        //return a custom json object with information relevant to front end
        //constructing a playlist object for front end
        JSONObject play = new JSONObject();
        play.put("name", playlist.getPlaylistName());
        play.put("id", playlist.getPlaylistId());

        JSONArray songArr = new JSONArray();
        for(Song element: songs){
            JSONObject tm = new JSONObject();
            tm.put(element.getName(), element.getArtist().getArtistname());
            songArr.put(tm);
        }

        play.put("songs_in_playlist",songArr);

        System.out.println(play);

        //return play.toString();
        return ResponseEntity.ok(playlist);
    }

    public JSONObject httpHelper(String url, String token) throws IOException, InterruptedException,
            JSONException, URISyntaxException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest req = HttpRequest.newBuilder()
               .uri(new URI(url))
             .header("Authorization", "Bearer " + token)
          .GET().build();

        //get response
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        //System.out.println(resp.body());

         JSONObject obj = new JSONObject(resp.body());

         return obj;
    }


    @ApiOperation("receives auth token grabbed from user logging un")
    @PostMapping("/scope")
    public void user_scope_token(@RequestParam String user_token){
        System.out.println(user_token);
        this.user_token = user_token;
    }


    @ApiOperation("post the specified playlist to the spotify user logged in")
    @PostMapping("/export")
    public ResponseEntity<?> export_playlist(@RequestParam String playlist_id) throws JSONException, IOException, URISyntaxException, InterruptedException {
        Playlist playlist;

        //search for playlist id
        if(playlistRepo.existsById(Long.parseLong(playlist_id)))
        {
            //set playlist
            playlist = playlistRepo.getOne(Long.parseLong(playlist_id));
        }
        else {
            return ResponseEntity.badRequest().body("playlist id does not exist");
        }

        //grab each song from playlist
        Set<Song> songs = playlist.getPlaylist_songs();
        for(Song el : songs){
            System.out.println(el.getName());
            System.out.println(el.getSpotifyId());
        }

        //checks if user is logged into spotify
        if(user_token == null || user_token.isEmpty()){
            return ResponseEntity.badRequest().body("user not logged into spotify");
        }

        //get user id
        HttpClient client = HttpClient.newHttpClient();
        System.out.println(user_token);

        //grab profile data
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/me"))
                .header("Authorization", "Bearer " + user_token)
                .GET().build();

        //get response
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println(resp.body());

        //get user id
        JSONObject obj = new JSONObject(resp.body());
        String spotifyUserID = obj.getString("id");
        System.out.println(spotifyUserID);

        //set playlist parameters
        //name matches playlist name
        //description is app plug
        //public is set to false
        JSONObject post = new JSONObject();
        post.put("name", playlist.getPlaylistName());
        post.put("description", "generated by PlaylistGenerator");
        post.put("public", false);



        //create a playlist
        //note: will not throw error is a playlist already has playlist name -> will just make a separate playlist
        HttpRequest createPlaylist = HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/users/" + spotifyUserID +"/playlists"))
                .POST(HttpRequest.BodyPublishers.ofString(post.toString()))
                .header("Authorization", "Bearer " + user_token)
                .build();
        HttpResponse<String> playlist_resp = client.send(createPlaylist, HttpResponse.BodyHandlers.ofString());

         //grab playlist id
        JSONObject pl = new JSONObject(playlist_resp.body());
        String play_id = pl.getString("id");


        //json object to form body for post
        JSONObject pst = new JSONObject();
        JSONArray uri= new JSONArray();

        //add in each song
        for(Song el : songs){
            System.out.println(el.getName());
            System.out.println(el.getSpotifyId());
            String id_clean = el.getSpotifyId().replace("}", "");
            uri.put("spotify:track:" + id_clean);
        }
        pst.put("uris", uri);


        System.out.println(play_id);
        System.out.println(pst);

        //a request can add at most 100 items
        //TODO: add in check if there are more than 100 songs
        HttpRequest addSong = HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/playlists/" + play_id +"/tracks"))
                .POST(HttpRequest.BodyPublishers.ofString(pst.toString()))
                .header("Authorization", "Bearer " + user_token)
                .build();
        HttpResponse<String> addSong_resp = client.send(addSong, HttpResponse.BodyHandlers.ofString());

        //check response
        System.out.println(addSong_resp.body());

        //playlist created
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Like a playlist")
    @PostMapping("/like")
    public ResponseEntity<?> likePlaylist(@RequestParam String playlistId, @RequestParam String username) {
        User user = userRepository.findByUsername(username);

        // Check if the playlist exists
        Long id = Long.parseLong(playlistId);
        Optional<Playlist> optionalPlaylist = playlistRepo.findById(id);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();

            if(user.likedPlaylists.contains(playlist)){
                return ResponseEntity.badRequest().body("Playlist already liked.");

            } else {
                // Increment likes
                playlist.like(user);
                user.addLikedPlaylist(playlist);

                playlistRepo.save(playlist);
                userRepository.save(user);
            }



            return ResponseEntity.ok("Playlist liked successfully.");
        } else {
            return ResponseEntity.badRequest().body("Playlist not found.");
        }
    }

    @PostMapping("/remove_like")
    public ResponseEntity<?> removeLike(@RequestParam String username, @RequestParam String playlistId){
        User user = userRepository.findByUsername(username);

        // Check if the playlist exists
        Long id = Long.parseLong(playlistId);
        Optional<Playlist> optionalPlaylist = playlistRepo.findById(id);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();

            if(playlist.user_likes.contains(user)){
                user.removeLikedPlaylist(playlist);
                playlist.removeLike(user);

                playlistRepo.save(playlist);
                userRepository.save(user);

                return ResponseEntity.ok("liked remove");
            } else {
                return ResponseEntity.badRequest().body("user has not liked this playlist");
            }

        }
        return ResponseEntity.badRequest().body("an error occurred");
    }


    @GetMapping("/user_playlists")
    public ResponseEntity<?> getPlaylists(@RequestParam String username){
        User user = userRepository.findByUsername(username);

        //return user not found error if user is not in repo
        if (user == null) {
            JSONObject obj = new JSONObject();
            obj.put("error", "user not found");
            return ResponseEntity.badRequest().body(obj.toString());
        }

        List<Playlist> playlist = user.getPlaylist();

        for (Playlist temp : playlist) {
            System.out.println(temp.getPlaylistName());
            Set<Song> songs = temp.getPlaylist_songs();
            for (Song song : songs) {
                System.out.println(song.getName() + " by " + song.getArtistName());
            }
        }

       // return ResponseEntity.ok().build();
        return ResponseEntity.ok(playlist);
    }


    @PostMapping("/remove_song")
    public ResponseEntity<?> removeSong(@RequestParam String playlist_id, @RequestParam String song_name){
        Playlist playlist;

        System.out.println(song_name);
        //search for playlist id
        if(playlistRepo.existsById(Long.parseLong(playlist_id)))
        {
            //set playlist
            playlist = playlistRepo.getOne(Long.parseLong(playlist_id));
        }
        else {
            return ResponseEntity.badRequest().body("playlist id does not exist");
        }


        Set<Song> songs = playlist.getPlaylist_songs();

        Long removeId = null;

        Iterator<Song> itr = songs.iterator();
        while(itr.hasNext()){
            Song song = itr.next();
            if(song.getName().equals(song_name)){
                itr.remove();
                removeId = song.getSongId();
               // playlist.removeSong(song.getSongId());
               // playlistRepo.save(playlist);
            }
        }
        //to avoid concurrent modification exception
        if(removeId != null){
            System.out.println(song_name + " was removed");
            playlist.removeSong(removeId);
            playlistRepo.save(playlist);
        }

        return ResponseEntity.ok(playlist);
    }
    @PostMapping("/rename")
    public ResponseEntity<?> rename(@RequestParam String playlist_id, @RequestParam String name){
        Playlist playlist;

        //search for playlist id
        if(playlistRepo.existsById(Long.parseLong(playlist_id)))
        {
            //set playlist
            playlist = playlistRepo.getOne(Long.parseLong(playlist_id));
        }
        else {
            return ResponseEntity.badRequest().body("playlist id does not exist");
        }

        playlist.setPlaylistName(name);

        playlistRepo.save(playlist);

        return ResponseEntity.ok(playlist);
    }

    @PostMapping("/add_song")
    public ResponseEntity<?> addSong(@RequestParam String playlist_id,
                                     @RequestParam String song_name,
                                     @RequestParam String spotify_id,
                                     @RequestParam String artist_name){

        Playlist playlist;

        //search for playlist id
        if(playlistRepo.existsById(Long.parseLong(playlist_id)))
        {
            //set playlist
            playlist = playlistRepo.getOne(Long.parseLong(playlist_id));
        }
        else {
            return ResponseEntity.badRequest().body("playlist id does not exist");
        }

        Artist ar;
        Set<Song> songs = new HashSet<Song>();

        if(artistRepo.findByArtistname(artist_name) == null){
            System.out.println("new artist");
            ar = new Artist(artist_name, songs);
            artistRepo.save(ar);
        } else {
            System.out.println("old artist");
            ar = (Artist) artistRepo.findByArtistname(artist_name);
        }

        Song s;
        if(songRepo.findByName(song_name) == null){
            s = new Song(song_name, ar, spotify_id);
            // s.addPlaylist(playlistRepo.getOne(playlist.getPlaylistId()));
            songRepo.save(s);

        } else {
            s = (Song) songRepo.findByName(song_name);
        }

        playlist.addSong(s);
        songs.add(s);
        ar.addSong(s);
        artistRepo.save(ar);
        s.setArtist(ar);
        s.setArtistName(artist_name);

        playlistRepo.save(playlist);

        System.out.println("adding "+ song_name + " by " + artist_name);

        return ResponseEntity.ok(playlist);

    }
    @GetMapping("/top-likes")
    public ResponseEntity<List<Playlist>> getTop10PlaylistsByLikes() {
        List<Playlist> topPlaylists = playlistService.getTop10PlaylistsByLikes();
        return new ResponseEntity<>(topPlaylists, HttpStatus.OK);
    }

    @PostMapping("/get_preview")
    public ResponseEntity<?> getPreview(@RequestParam String song_name) throws URISyntaxException, IOException, InterruptedException {
        //grab token
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "client_credentials");

        //encode parameters
        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String header = client_id + ":" + client_secret;

        //build http request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI("https://accounts.spotify.com/api/token"))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + Base64.getEncoder().encodeToString(header.getBytes()))
                .POST(HttpRequest.BodyPublishers.ofString(form)).build();

        //send http request
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());

        //response returned as json object
        JSONObject obj = new JSONObject(response.body());

        Song s = (Song) songRepo.findByName(song_name);

        String track_id = s.getSpotifyId();

        System.out.println(track_id);


        //grab value for access_token field, which we may then use to make requests
        token = obj.getString("access_token");
        System.out.println(token);


        JSONObject resp = httpHelper("https://api.spotify.com/v1/tracks/" + track_id, token);
        System.out.println(resp.toString());

        String test = "";
        try{
            test = resp.getString("preview_url");
        } catch(JSONException e){
            return ResponseEntity.ok("preview unavailable for this track");
        }


        return ResponseEntity.ok(test);
    }

    @GetMapping("/get_all_playlists")
    public List<Playlist> getAllPlaylists(){
        return playlistRepo.findByIspublic(true);
    }

    @PostMapping("/liked_playlists")
    public ResponseEntity<?> getLikedPlaylists(@RequestParam String username){
        User user = userRepository.findByUsername(username);
        if(user != null){
            return ResponseEntity.ok(user.getLikedPlaylists());
        }
        else {
            return ResponseEntity.badRequest().body("user does not exists");
        }
    }

    @PostMapping("/set_public")
    public ResponseEntity<?> setPublic(@RequestParam String set_public, @RequestParam String playlist_id){
        Playlist playlist;

        //search for playlist id
        if(playlistRepo.existsById(Long.parseLong(playlist_id)))
        {
            //set playlist
            playlist = playlistRepo.getOne(Long.parseLong(playlist_id));
        }
        else {
            return ResponseEntity.badRequest().body("playlist id does not exist");
        }

        //parse boolean string
        //music
        playlist.setIs_public(Boolean.parseBoolean(set_public));

        System.out.println(playlist.getIs_public());

        playlistRepo.save(playlist);

        return ResponseEntity.ok(playlist);
    }

}