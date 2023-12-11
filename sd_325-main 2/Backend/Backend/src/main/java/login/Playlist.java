package login;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.*;

@Entity
@Table(name = "playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlistId")
    private Long playlistId;

    @Column(name="playlistName")
    private String playlistName;

    @Column(name = "username")
    private String username;
    @Column(name = "likes")
    private int likes;
    @Column(name = "public")
    private boolean ispublic;

    //many playlists can have one user id
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToMany(mappedBy ="likedPlaylists")
    Set<User> user_likes;

    //many playlist can have many songs
    //playlist will be the owning side of this relationship
    @ManyToMany
    @JoinTable(
            name = "playlist_songs",
            joinColumns  = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private Set<Song> playlist_songs = new HashSet<Song>();
    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "playlist")
    //@JoinColumn(name="id")
    // private Set<Song> songs;

    public Playlist(){}
    public Playlist(User user,String name){
        this.user = user;
        this.playlistName = name;
        this.likes = 0;
    }



    public void setIs_public(boolean is_public) {
        this.ispublic = is_public;
    }

    public boolean getIs_public(){
        return this.ispublic;
    }

    //getters and setters
    public void addSong(Song song){
        this.playlist_songs.add(song);
        song.getPlaylists().add(this);
    }

    public void setLikes(int likes) {

        this.likes = likes;
    }

    public void removeSong(Long songId){
        Song song = this.playlist_songs.stream().filter
                (t -> t.getSongId() == songId).findFirst()
                .orElse(null);
        if( song != null){
            this.playlist_songs.remove(song);
            song.getPlaylists().remove(this);
        }
    }

    //null pointer excp
    public Long getPlaylistId(){
        return playlistId;
    }

    //may never need
    public void setPlaylistId(Long id){
        this.playlistId = id;
    }

    public String getPlaylistName(){
        return this.playlistName;
    }

    public void setPlaylistName(String name){
        this.playlistName = name;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Set<Song> getPlaylist_songs(){
        return playlist_songs;
    }

    public User getUser(){
        return this.user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void addLike() {

        this.likes++;
    }

    public void like(User user){
            System.out.println("add playlist like for " + getPlaylistName() + " by " + user.getUsername());
            user_likes.add(user);
            addLike();
    }

    public void removeLike(User user){
        user_likes.remove(user);
        this.likes--;
    }

    public int getLikes() {
        return likes;
    }
}
