package login;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "songId")
    private Long songId;


    @NotBlank(message = "song name is required")
    @Column(name = "name")
    private String name;

    @Column(name = "spotify_id")
    private String spotifyId;

    //many songs may be in many playlists
    @JsonIgnore
    @ManyToMany(mappedBy = "playlist_songs")
    private Set<Playlist> playlists = new HashSet<>();

    @Column(name = "artistName")
    private String artistName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    //may need to implement a many to may use case?
    //while many songs can have one artist, one song may also have many artists
    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "playlist_id")
    // private Playlist playlist;
    //@ManyToMany(mappedBy = "artist_songs")
    //List<Artist> artists;


    public Song( String song_name, Artist artist_name, String spotifyId){
        this.name = song_name;
        this.artist = artist_name;
        this.spotifyId = spotifyId;
    }
    public Song(){}

    public Long getSongId(){
        return songId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public Set<Playlist> getPlaylists(){
        return playlists;
    }
    public void setPlaylists(Set<Playlist> playlists){
        this.playlists = playlists;
    }

    public void setArtist(Artist artist){
        this.artist = artist;
    }

    public Artist getArtist(){
        return this.artist;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
