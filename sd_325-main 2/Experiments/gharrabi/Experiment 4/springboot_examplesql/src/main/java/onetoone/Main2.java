package onetoone;

import onetoone.Likes.Likes;
import onetoone.Likes.LikesRepository;
import onetoone.leaderboard.LeaderBoardRepository;
import onetoone.leaderboard.Leaderboard;
import onetoone.today.Today;
import onetoone.today.TodayRepository;
import onetoone.weekly.Weekly;
import onetoone.weekly.WeeklyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author Vivek Bengre
 *
 */

@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines
    /**
     *
     * @param leaderboardRepository repository for the User entity
     * @param todayRepository repository for the Laptop entity
     * @param weeklyRepository
     * @param likesRepository
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(LeaderBoardRepository leaderboardRepository, TodayRepository todayRepository, WeeklyRepository weeklyRepository, LikesRepository likesRepository) {
        return args -> {
//            User user1 = new User("John", "john@somemail.com");
//            User user2 = new User("Jane", "jane@somemail.com");
//            User user3 = new User("Justin", "justin@somemail.com");
//            Laptop laptop1 = new Laptop( 2.5, 4, 8, "Lenovo", 300);
//            Laptop laptop2 = new Laptop( 4.1, 8, 16, "Hp", 800);
//            Laptop laptop3 = new Laptop( 3.5, 32, 32, "Dell", 2300);
//            user1.setLaptop(laptop1);
//            user2.setLaptop(laptop2);
//            user3.setLaptop(laptop3);
//            userRepository.save(user1);
//            userRepository.save(user2);
//            userRepository.save(user3);

            Likes likes1 = new Likes("new cumulative", 500);

            Today today1 = new Today (1, "12 AM", "theNighteman", 80);
            Today today2 = new Today (2, "Homeworkkkk", "AvgEngStdnt", 77);
            Today today3 = new Today (3, "Oh No!", "Marina and the Diamonds", 60);
            Today today4 = new Today (4, "Pizzaaa", "BrokeStdntHrs", 55);
            Today today5 = new Today (5, "Hello", "A-dell", 53);
            Today today6 = new Today (6, "Barbie", "theBarbie", 40);
            Today today7 = new Today (7, "Biteof87", "PurpleMan", 35);
            Today today8 = new Today (8, "Mood", "TiredCat", 30);
            Today today9 = new Today (9, "Pumpkin", "Soup", 25);
            Today today10 = new Today (10, "Elixir", "Mettalica", 20);


            Weekly weekly1 = new Weekly(1, "Keyboard", "theNighteman", 19);
            Weekly weekly2 = new Weekly(2, "Lantern", "Ashley", 17);
            Weekly weekly3 = new Weekly (3, "Yippee!!!", "MeMe", 15);
            Weekly weekly4 = new Weekly (4, "L.O.L", "Kidd", 12);
            Weekly weekly5 = new Weekly (5, "Gimme Chocolate", "Baby metal", 10);
            Weekly weekly6 = new Weekly (6, "Dream Haze", "Taylor Smith", 6);
            Weekly weekly7 = new Weekly (7, "The One", "Star Guy", 5);
            Weekly weekly8 = new Weekly (8, "Fun things", "Lil pup", 3);
            Weekly weekly9 = new Weekly (9, "Black and White", "SoupyColors", 2);
            Weekly weekly10 = new Weekly (10, "Old Times", "ACDB", 1);

            Leaderboard leaderboard1 = new Leaderboard(1, "What is Love", "Twice", 380);
            Leaderboard leaderboard2 = new Leaderboard(2, "TT", "Twice", 277);
            Leaderboard leaderboard3 = new Leaderboard(3, "Oh No!", "Marina and the Diamonds", 260);
            Leaderboard leaderboard4 = new Leaderboard(4, "Fun things", "Lil pup", 155);
            Leaderboard leaderboard5 = new Leaderboard(5, "Hello", "A-dell", 153);
            Leaderboard leaderboard6 = new Leaderboard(6, "Barbie", "theBarbie", 140);
            Leaderboard leaderboard7 = new Leaderboard(7, "Tuna", "ClassyCat", 120);
            Leaderboard leaderboard8 = new Leaderboard(8, "Mood", "TiredCat", 110);
            Leaderboard leaderboard9 = new Leaderboard (9, "Pumpkin", "Soup", 90);
            Leaderboard leaderboard10 = new Leaderboard(10, "Old Times", "ACDB", 85);



            likesRepository.save(likes1);

            todayRepository.save(today1);
            todayRepository.save(today2);
            todayRepository.save(today3);
            todayRepository.save(today4);
            todayRepository.save(today5);
            todayRepository.save(today6);
            todayRepository.save(today7);
            todayRepository.save(today8);
            todayRepository.save(today9);
            todayRepository.save(today10);

            weeklyRepository.save(weekly1);
            weeklyRepository.save(weekly2);
            weeklyRepository.save(weekly3);
            weeklyRepository.save(weekly4);
            weeklyRepository.save(weekly5);
            weeklyRepository.save(weekly6);
            weeklyRepository.save(weekly7);
            weeklyRepository.save(weekly8);
            weeklyRepository.save(weekly9);
            weeklyRepository.save(weekly10);

            leaderboardRepository.save(leaderboard1);
            leaderboardRepository.save(leaderboard2);
            leaderboardRepository.save(leaderboard3);
            leaderboardRepository.save(leaderboard4);
            leaderboardRepository.save(leaderboard5);
            leaderboardRepository.save(leaderboard6);
            leaderboardRepository.save(leaderboard7);
            leaderboardRepository.save(leaderboard8);
            leaderboardRepository.save(leaderboard9);
            leaderboardRepository.save(leaderboard10);






        };}}

