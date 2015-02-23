package bg.unisofia.fmi.ai.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import bg.unisofia.fmi.ai.recommend.UserStatistics;

public class UserStatisticsTest {

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
        movie1 = new Movie("1", "Title 1", "");
        movie2 = new Movie("2", "Title 2", "");
        movie3 = new Movie("3", "Title 3", "");
        movie4 = new Movie("4", "Title 4", "");
    }

    @Before
    public void beforeEachSetRating() {
        beforeEachCreateUsers();
        beforeEachCreateMovies();
    }

    @Test
    public void testGetMeanRating() {
        assertThat(UserStatistics.getMeanRating(user1)).isEqualTo(0);

        user1.rate(movie1, 0);
        user1.rate(movie2, 1);
        user1.rate(movie3, 2);
        assertThat(UserStatistics.getMeanRating(user1)).isEqualTo(1);

        user1.rate(movie4, 0);
        assertThat(UserStatistics.getMeanRating(user1)).isEqualTo(0.75);

    }

    @Test
    public void testGetStandsrdDeviation() {
        final Set<Movie> movies = new TreeSet<Movie>();

        user1.rate(movie1, 2);
        user1.rate(movie2, 1);
        assertThat(UserStatistics.getStandardDeviation(user1, movies)).isEqualTo(0);

        movies.add(movie1);
        movies.add(movie2);
        assertThat(UserStatistics.getStandardDeviation(user1, movies)).isEqualTo(0.5);

        user1.rate(movie3, 0);
        user1.rate(movie4, 0);
        assertThat(UserStatistics.getStandardDeviation(user1, movies)).isEqualTo(0.9013878188659973);
    }

    @Test
    public void testGetRelatedUsers() {

        user1.rate(movie2, 1);
        user1.rate(movie1, 2);

        user2.rate(movie2, 2);
        user2.rate(movie3, 1);

        user3.rate(movie3, 1);

        Set<User> relatedUsersToUser1 = new TreeSet<User>();
        relatedUsersToUser1.add(user2);

        Set<User> relatedUsersToUser2 = new TreeSet<User>();
        relatedUsersToUser2.add(user1);
        relatedUsersToUser2.add(user3);

        Set<User> relatedUsersToUser3 = new TreeSet<User>();
        relatedUsersToUser3.add(user2);

        assertThat(UserStatistics.getRelatedUsers(user1)).isEqualTo(relatedUsersToUser1);
        assertThat(UserStatistics.getRelatedUsers(user2)).isEqualTo(relatedUsersToUser2);
        assertThat(UserStatistics.getRelatedUsers(user3)).isEqualTo(relatedUsersToUser3);
    }

    @Test
    public void testGetMoviesInCommon() {
        assertThat(UserStatistics.getMoviesInCommon(user1, user2)).isEmpty();

        user1.rate(movie1, 2);
        user1.rate(movie2, 2);

        user2.rate(movie2, 2);
        user2.rate(movie3, 2);

        assertThat(UserStatistics.getMoviesInCommon(user1, user2)).containsOnly(movie2);
        assertThat(UserStatistics.getMoviesInCommon(user2, user1)).containsOnly(movie2);
    }

    @Test
    public void testGetMoviesDifference() {
        assertThat(UserStatistics.getMoviesDifference(user1, user2)).isEmpty();

        user1.rate(movie1, 2);
        user1.rate(movie2, 2);

        user2.rate(movie2, 2);
        user2.rate(movie3, 2);

        assertThat(UserStatistics.getMoviesDifference(user1, user2)).containsOnly(movie1);
        assertThat(UserStatistics.getMoviesDifference(user2, user1)).containsOnly(movie3);
    }

}
