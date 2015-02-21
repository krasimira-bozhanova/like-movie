package bg.unisofia.fmi.ai.recommend;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.Rating;
import bg.unisofia.fmi.ai.data.User;

public class UserStatistics {

    public static double getMeanRating(User user) {
        double mean = 0;

        if (user.getRatings().isEmpty()) {
            return mean;
        }

        for (Rating rating : user.getRatings()) {
            mean += rating.getRatingValue();
        }

        return mean / user.getRatings().size();
    }

    public static double getStandardDeviation(User user, Set<Movie> movies) {
        double sumSquares = 0;

        if(movies.isEmpty()) {
            return 0;
        }

        // Get all ids of the movies in the set
        List<Integer> moviesIds = movies.stream().mapToInt(
                m -> m.getId()).boxed().collect(Collectors.toList());
        for (Rating rating : user.getRatings()) {
            if (movies.contains(rating.getMovie())) {
                sumSquares += Math.pow(
                        (rating.getRatingValue() - getMeanRating(user)), 2);
            }
        }

        return Math.sqrt(sumSquares / movies.size());
    }

    public static double getRating(User user, Movie movie) {
        List<Rating> ratingForMovie = user.getRatings().stream().filter(
                r -> r.getMovie().getId() == movie.getId()).collect(Collectors.toList());
        if (ratingForMovie.isEmpty()) {
            return 0;
        }
        return ratingForMovie.get(0).getRatingValue();
    }

    public static Set<User> getRelatedUsers(User user) {
        Set<User> relatedUsers = new TreeSet<User>();

        for (Rating rating : user.getRatings()) {
            Set<User> usersForCurrentMovie = rating.getMovie().getVotedUsers();
            relatedUsers.addAll(usersForCurrentMovie);
        }

        relatedUsers.remove(user);
        return relatedUsers;
    }

    public static Set<Movie> getMoviesInCommon(User user, User otherUser) {
        Set<Movie> movies = user.getRatings().stream()
                .map(r -> r.getMovie()).collect(Collectors.toSet());
        Set<Movie> otherUserMovies = otherUser.getRatings().stream()
                .map(r -> r.getMovie()).collect(Collectors.toSet());

        movies.retainAll(otherUserMovies);

        List<Movie> commonMovies = new ArrayList<Movie>();
        for (Rating otherRating : otherUser.getRatings()) {
            Movie currentMovie = otherRating.getMovie();
            if (movies.contains(currentMovie)) {
                commonMovies.add(currentMovie);
            }
        }
        return movies;
    }

    public static Set<Movie> getMoviesDifference(User user, User otherUser) {
        // Get all the movies that I like and the other user doesn't
        Set<Movie> movies = user.getRatings().stream()
                .map(r -> r.getMovie()).collect(Collectors.toSet());

        for (Rating otherRating : otherUser.getRatings()) {
            Movie currentMovie = otherRating.getMovie();
            if (movies.contains(currentMovie)) {
                movies.remove(currentMovie);
            }
        }
        return movies;

    }

}
