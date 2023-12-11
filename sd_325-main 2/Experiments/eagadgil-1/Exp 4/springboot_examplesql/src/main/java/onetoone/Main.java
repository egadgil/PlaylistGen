package onetoone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.Songs.Song;
import onetoone.Songs.SongRepository;
import onetoone.Artists.Artist;
import onetoone.Artists.ArtistRepository;

/**
 *
 *
 *
 * @author Vivek Bengre
 *
 */

@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 Artists with their machines
    /**
     *
     * @param ArtistRepository repository for the Artist entity
     * @param SongRepository repository for the Song entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in Artist.java just associating the Song object with the Artist will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initArtist(ArtistRepository ArtistRepository, SongRepository SongRepository) {
        return args -> {
            Artist Artist1 = new Artist("John");
            Artist Artist2 = new Artist("Jane");
            Artist Artist3 = new Artist("Justin");
            Song Song1 = new Song( "OOH");
            Song Song2 = new Song("OOH1");
            Song Song3 = new Song("OOH2");
            Artist1.setSong(Song1);
            Artist2.setSong(Song2);
            Artist3.setSong(Song3);
            ArtistRepository.save(Artist1);
            ArtistRepository.save(Artist2);
            ArtistRepository.save(Artist3);

        };
    }

}
