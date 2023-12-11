package onetoone.Songs;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Artists.Artist;


/**
 * 
 * @author Esha g
 */ 

@Entity
public class Song {
    
    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;


    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */
    @JsonIgnore
    @OneToOne(mappedBy = "song")
    private Artist artist;

    public Song(String t) {
      title = t;
    }

    public Song() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }



    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setArtist(Artist artist){
        this.artist = artist;
    }
    public Artist getArtist(){
        return artist;
    }


}
