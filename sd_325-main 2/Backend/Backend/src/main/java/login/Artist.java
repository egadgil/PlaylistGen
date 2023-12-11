package login;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artistId")
    private Long artistId;

    @NotBlank(message="artist name cannot be left blank")
    @Column(name="artistName")
    private String artistname;


    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    private Set<Song> songs;

    //many artist may have many songs
    //@ManyToMany
    //@JoinTable(
    //      name = "artist_songs",
    //    joinColumns = @JoinColumn(name="artist_id"),
    //  inverseJoinColumns = @JoinColumn(name="song_id")
    //)
    //List<Song> artist_songs;

    public Artist (String name, Set<Song> songs){
        this.artistname = name;
        this.songs = songs;
    }

    public Artist(){
    }
    public void addSong(Song song){
        this.songs.add(song);
    }
    public Set< Song> getArtist_songs() {
        return songs;
    }

    public void setArtist_songs(Set<Song> artist_songs) {
        this.songs = artist_songs;
    }

    public Long getArtistId() {
        return artistId;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }


}
