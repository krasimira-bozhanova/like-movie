package bg.unisofia.fmi.ai.recommend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.recommend.algorithm.KNN;

public class KNNTest {

    User user1;
    User user2;
    User user3;

    Movie movie1;
    Movie movie2;
    Movie movie3;
    Movie movie4;

    public void beforeEachCreateUsers() {
        user1 = new User("1");
        user2 = new User("2");
        user3 = new User("3");
    }

    public void beforeEachCreateMovies() {
        movie1 = new Movie("1", "");
        movie2 = new Movie("2", "");
        movie3 = new Movie("3", "");
        movie4 = new Movie("4", "");
    }

    @Before
    public void beforeEachSetRating() {
        beforeEachCreateUsers();
        beforeEachCreateMovies();

        user1.rate(movie1, 2);
        user1.rate(movie2, 2);

        user2.rate(movie3, 1);

        user3.rate(movie1, 1);
        user3.rate(movie4, 2);
    }

    @Test
    public void testGetClosestNeighbours() {
        User newUser = new User("4");
        KNN newUserProblem = new KNN(newUser, 1);
        assertThat(newUserProblem.getClosestNeighbours()).isEmpty();

        newUser.rate(movie1, 2);

        assertThat(newUserProblem.getClosestNeighbours()).containsOnlyKeys(user1);

        newUser.rate(movie1, 1);
        newUserProblem = new KNN(newUser, 2);

        assertThat(newUserProblem.getClosestNeighbours()).containsOnlyKeys(user3, user1);
    }

    @Test
    public void testCalculateSimilarity() {
        User newUser = new User("4");
        KNN newUserProblem = new KNN(newUser, 1);
        assertThat(newUserProblem.calculateSimilatiry(user1)).isEqualTo(0);
        assertThat(newUserProblem.calculateSimilatiry(user2)).isEqualTo(0);
        assertThat(newUserProblem.calculateSimilatiry(user3)).isEqualTo(0);

        newUser.rate(movie1, 2);

        assertThat(newUserProblem.calculateSimilatiry(newUser)).isEqualTo(1);
        assertThat(newUserProblem.calculateSimilatiry(user1)).isEqualTo(1);
        assertThat(newUserProblem.calculateSimilatiry(user2)).isEqualTo(0);
        assertThat(newUserProblem.calculateSimilatiry(user3)).isEqualTo(1);

    }

    @Test
    public void testGetMoviesToRecommend() {
        User newUser = new User("4");
        newUser.rate(movie1, 2);

        KNN newUserProblem = new KNN(newUser, 2);

        assertThat(newUserProblem.getMoviesToRecommend()).containsOnly(movie2, movie4);
    }

    @Test
    public void testGetExpectedRating() {
        User newUser = new User("4");
        newUser.rate(movie1, 2);

        KNN newUserProblem = new KNN(newUser, 2);

        assertThat(newUserProblem.getExpectedRating(movie2)).isEqualTo(1.25);
        assertThat(newUserProblem.getExpectedRating(movie4)).isEqualTo(1.25);
    }

}
