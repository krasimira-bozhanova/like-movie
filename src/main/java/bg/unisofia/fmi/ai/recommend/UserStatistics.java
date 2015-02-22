package bg.unisofia.fmi.ai.recommend;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.db.util.DbUtil;

public class UserStatistics {

    public static double getMeanRating(User user) {
        final Collection<Double> userRatings = user.getRatings().values();

        return userRatings.stream().mapToDouble(d -> d).average().orElse(0);
    }

    public static double getStandardDeviation(User user, Set<Movie> movies) {
        if (movies.isEmpty())
            return 0;

        double sumSquares = 0;
        for (Map.Entry<Movie, Double> entry : user.getRatings().entrySet()) {
            if (movies.contains(entry.getKey())) {
                sumSquares += Math.pow((entry.getValue() - getMeanRating(user)), 2);
            }
        }

        return Math.sqrt(sumSquares / movies.size());
    }

    public static Set<User> getRelatedUsers(User user) {
        Set<User> relatedUsers = new TreeSet<User>();
        final UserService userService = new UserService(DbUtil.getConnectionSource());

        user.getRatings().forEach((movie, rating) -> {
            Set<User> usersForCurrentMovie = userService.getVotedUsers(movie);
            relatedUsers.addAll(usersForCurrentMovie);
        });
        relatedUsers.remove(user);

        return relatedUsers;
    }

    public static Set<Movie> getMoviesInCommon(User user, User otherUser) {
        Set<Movie> movies = new TreeSet<>(user.getRatings().keySet());
        Set<Movie> otherUserMovies = new TreeSet<>(otherUser.getRatings().keySet());

        movies.retainAll(otherUserMovies);

        return movies;
    }

    public static Set<Movie> getMoviesDifference(User user, User otherUser) {
        Set<Movie> movies = new TreeSet<>(user.getRatings().keySet());
        Set<Movie> otherUserMovies = new TreeSet<>(otherUser.getRatings().keySet());

        movies.removeAll(otherUserMovies);

        return movies;
    }

}
