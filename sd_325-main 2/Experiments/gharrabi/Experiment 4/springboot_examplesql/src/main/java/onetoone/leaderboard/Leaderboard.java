package onetoone.leaderboard;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int rank;
    private int likes;
    private String song;
    private String artist;
   // private int count;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */
    //  @OneToOneLikes
    // private Likes;

    public Leaderboard(int likes, String song, String artist,int rank) {
        this.rank = rank;
        this.likes = likes;
        this.song = song;
        this.artist = artist;
        //this.count = count;
    }

    public Leaderboard() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public double getRank(){
        return rank;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    public int getLikes(){
        return likes;
    }

    public void setLikes(int likes){
        this.likes = likes;
    }

    public String getSong(){
        return song;
    }

    public void setSong(String song){
        this.song = song;
    }

    public String getArtist(){
        return artist;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }



}
