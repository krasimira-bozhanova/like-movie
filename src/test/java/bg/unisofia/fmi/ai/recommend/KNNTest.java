package bg.unisofia.fmi.ai.recommend;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.Rating;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.recommend.KNN;

public class KNNTest {

    User user1;
    User user2;
    User user3;

    Movie movie1;
    Movie movie2;
    Movie movie3;
    Movie movie4;

    public void beforeEachCreateUsers() {
        user1 = new User(1);
        user2 = new User(2);
        user3 = new User(3);
    }

    public void beforeEachCreateMovies() {
        movie1 = new Movie(1);
        movie2 = new Movie(2);
        movie3 = new Movie(3);
        movie4 = new Movie(4);
    }

    @Before
    public void beforeEachSetRating() {
        beforeEachCreateUsers();
        beforeEachCreateMovies();

        List<Rating> user1Ratings = new ArrayList<Rating>();
        user1Ratings.add(new Rating(2, movie1));
        user1Ratings.add(new Rating(2, movie2));
        user1.setRatings(user1Ratings);

        List<Rating> user2Ratings = new ArrayList<Rating>();
        user2Ratings.add(new Rating(1, movie3));
        user2.setRatings(user2Ratings);

        List<Rating> user3Ratings = new ArrayList<Rating>();
        user3Ratings.add(new Rating(1, movie1));
        user3Ratings.add(new Rating(2, movie4));
        user3.setRatings(user3Ratings);

        movie1.addUserVote(user1);
        movie1.addUserVote(user3);
        movie2.addUserVote(user1);
        movie3.addUserVote(user2);
        movie4.addUserVote(user3);
    }

    @Test
    public void testGetClosestNeighbours() {
        User newUser = new User(4);
        KNN newUserProblem = new KNN(newUser, 1);
        assertThat(newUserProblem.getClosestNeighbours()).isEmpty();

        List<Rating> newUserRatings = new ArrayList<Rating>();
        newUserRatings.add(new Rating(2, movie1));
        movie1.addUserVote(newUser);
        newUser.setRatings(newUserRatings);

        assertThat(newUserProblem.getClosestNeighbours()).containsOnlyKeys(user1);

        newUserRatings = new ArrayList<Rating>();
        newUserRatings.add(new Rating(1, movie1));
        newUser.setRatings(newUserRatings);

        newUserProblem = new KNN(newUser, 2);

        assertThat(newUserProblem.getClosestNeighbours()).containsOnlyKeys(user3, user1);

    }

    @Test
    public void testCalculateSimilarity() {
        User newUser = new User(4);
        KNN newUserProblem = new KNN(newUser, 1);
        assertThat(newUserProblem.calculateSimilatiry(user1)).isEqualTo(0);
        assertThat(newUserProblem.calculateSimilatiry(user2)).isEqualTo(0);
        assertThat(newUserProblem.calculateSimilatiry(user3)).isEqualTo(0);

        List<Rating> newUserRatings = new ArrayList<Rating>();
        newUserRatings.add(new Rating(2, movie1));
        movie1.addUserVote(newUser);
        newUser.setRatings(newUserRatings);

        assertThat(newUserProblem.calculateSimilatiry(newUser)).isEqualTo(1);

        assertThat(newUserProblem.calculateSimilatiry(user1)).isEqualTo(1);
        assertThat(newUserProblem.calculateSimilatiry(user2)).isEqualTo(0);
        assertThat(newUserProblem.calculateSimilatiry(user3)).isEqualTo(1);

    }

    @Test
    public void testGetMoviesToRecommend() {
        User newUser = new User(4);
        List<Rating> newUserRatings = new ArrayList<Rating>();
        newUserRatings.add(new Rating(2, movie1));
        movie1.addUserVote(newUser);
        newUser.setRatings(newUserRatings);
        KNN newUserProblem = new KNN(newUser, 2);

        assertThat(newUserProblem.getMoviesToRecommend()).containsOnlyKeys(movie2, movie4);

    }

    @Test
    public void testGetExpectedRating() {
        User newUser = new User(4);
        List<Rating> newUserRatings = new ArrayList<Rating>();
        newUserRatings.add(new Rating(2, movie1));
        movie1.addUserVote(newUser);
        newUser.setRatings(newUserRatings);
        KNN newUserProblem = new KNN(newUser, 2);

        assertThat(newUserProblem.getExpectedRating(movie2)).isEqualTo(1.25);
        assertThat(newUserProblem.getExpectedRating(movie4)).isEqualTo(1.25);
    }


}
