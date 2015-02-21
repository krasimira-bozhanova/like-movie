package bg.unisofia.fmi.ai.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.Rating;
import bg.unisofia.fmi.ai.data.User;


public class UserTest {

    @Test
    public void testGetMeanRating() {

        User user = new User(1);
        assertThat(user.getMeanRating()).isEqualTo(0);

        List<Rating> ratings = new ArrayList<Rating>();

        ratings.add(new Rating(2, null));
        ratings.add(new Rating(1, null));
        ratings.add(new Rating(0, null));

        user.setRatings(ratings);
        assertThat(user.getMeanRating()).isEqualTo(1);

        ratings.add(new Rating(0, null));
        user.setRatings(ratings);
        assertThat(user.getMeanRating()).isEqualTo(0.75);

    }

    @Test
    public void testGetStandsrdDeviation() {
        User user = new User(1);

        Movie movie1 = new Movie(1);
        Movie movie2 = new Movie(2);

        List<Rating> ratings = new ArrayList<Rating>();
        ratings.add(new Rating(2, movie1));
        ratings.add(new Rating(1, movie2));
        user.setRatings(ratings);

        Set<Movie> movies = new TreeSet<Movie>();

        assertThat(user.getStandardDeviation(movies)).isEqualTo(0);

        movies.add(movie1);
        movies.add(movie2);

        assertThat(user.getStandardDeviation(movies)).isEqualTo(0.5);

        Movie movie3 = new Movie(3);
        Movie movie4 = new Movie(4);

        ratings.add(new Rating(0, movie3));
        ratings.add(new Rating(0, movie4));
        assertThat(user.getStandardDeviation(movies)).isEqualTo(0.9013878188659973);
    }

    @Test
    public void testGetRating() {
        User user = new User(1);

        Movie movie1 = new Movie(1);
        Movie movie2 = new Movie(2);
        Movie movie3 = new Movie(3);

        List<Rating> ratings = new ArrayList<Rating>();
        ratings.add(new Rating(2, movie1));
        ratings.add(new Rating(1, movie2));
        user.setRatings(ratings);

        assertThat(user.getRating(movie1)).isEqualTo(2);
        assertThat(user.getRating(movie2)).isEqualTo(1);
        assertThat(user.getRating(movie3)).isEqualTo(0);
    }

    @Test
    public void testGetRelatedUsers() {
        User user1 = new User(1);
        User user2 = new User(2);
        User user3 = new User(3);

        Movie movie1 = new Movie(1);
        Movie movie2 = new Movie(2);
        Movie movie3 = new Movie(3);

        List<Rating> ratingsUser1 = new ArrayList<Rating>();
        ratingsUser1.add(new Rating(2, movie1));
        movie1.addUserVote(user1);
        ratingsUser1.add(new Rating(1, movie2));
        movie2.addUserVote(user1);
        user1.setRatings(ratingsUser1);

        List<Rating> ratingsUser2 = new ArrayList<Rating>();
        ratingsUser2.add(new Rating(2, movie2));
        movie2.addUserVote(user2);
        ratingsUser2.add(new Rating(1, movie3));
        movie3.addUserVote(user2);
        movie3.addUserVote(user3);
        user2.setRatings(ratingsUser2);

        List<Rating> ratingsUser3 = new ArrayList<Rating>();
        ratingsUser3.add(new Rating(1, movie3));
        user3.setRatings(ratingsUser3);
        Set<User> relatedUsersToUser1 = new TreeSet<User>();
        relatedUsersToUser1.add(user2);

        Set<User> relatedUsersToUser2 = new TreeSet<User>();
        relatedUsersToUser2.add(user1);
        relatedUsersToUser2.add(user3);

        Set<User> relatedUsersToUser3 = new TreeSet<User>();
        relatedUsersToUser3.add(user2);

        assertThat(user1.getRelatedUsers()).isEqualTo(relatedUsersToUser1);
        assertThat(user2.getRelatedUsers()).isEqualTo(relatedUsersToUser2);
        assertThat(user3.getRelatedUsers()).isEqualTo(relatedUsersToUser3);
    }

    @Test
    public void testGetMoviesInCommon() {
        User user1 = new User(1);
        User user2 = new User(2);
        assertThat(user1.getMoviesInCommon(user2)).isEmpty();

        Movie movie1 = new Movie(1);
        Movie movie2 = new Movie(2);
        Movie movie3 = new Movie(3);

        List<Rating> ratingUser1 = new ArrayList<Rating>();
        ratingUser1.add(new Rating(2, movie1));
        ratingUser1.add(new Rating(2, movie2));
        movie1.addUserVote(user1);
        movie2.addUserVote(user1);
        user1.setRatings(ratingUser1);

        List<Rating> ratingUser2 = new ArrayList<Rating>();
        ratingUser2.add(new Rating(2, movie3));
        ratingUser2.add(new Rating(2, movie2));
        movie3.addUserVote(user2);
        movie2.addUserVote(user2);
        user2.setRatings(ratingUser2);

        assertThat(user1.getMoviesInCommon(user2)).containsOnly(movie2);
        assertThat(user2.getMoviesInCommon(user1)).containsOnly(movie2);
    }

    @Test
    public void testGetMoviesDifference() {
        User user1 = new User(1);
        User user2 = new User(2);
        assertThat(user1.getMoviesDifference(user2)).isEmpty();

        Movie movie1 = new Movie(1);
        Movie movie2 = new Movie(2);
        Movie movie3 = new Movie(3);

        List<Rating> ratingUser1 = new ArrayList<Rating>();
        ratingUser1.add(new Rating(2, movie1));
        ratingUser1.add(new Rating(2, movie2));
        movie1.addUserVote(user1);
        movie2.addUserVote(user1);
        user1.setRatings(ratingUser1);

        List<Rating> ratingUser2 = new ArrayList<Rating>();
        ratingUser2.add(new Rating(2, movie3));
        ratingUser2.add(new Rating(2, movie2));
        movie3.addUserVote(user2);
        movie2.addUserVote(user2);
        user2.setRatings(ratingUser2);

        assertThat(user1.getMoviesDifference(user2)).containsOnly(movie1);
        assertThat(user2.getMoviesDifference(user1)).containsOnly(movie3);
    }


}
