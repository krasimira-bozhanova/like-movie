package bg.unisofia.fmi.ai.recommend;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;

public class KNN {

    private final int neighboursNumber;

    private User user;
    private Map<User, Double> closestNeighbours;
    private SortedSet<Map.Entry<Movie, Double>> moviesToRecommend;

    public KNN(User user, int neighboursNumber) {
        this.user = user;
        this.neighboursNumber = neighboursNumber;

        closestNeighbours = getClosestNeighbours();
        moviesToRecommend = getMoviesToRecommend();
    }

    public Map<User, Double> getClosestNeighbours() {
        SortedMap<User, Double> pearsonCoefficients = new TreeMap<User, Double>();
        Map<User, Double> closestUsersSimilarity = new HashMap<User, Double>();

        // Compute similarities between the current user and the users that
        // have edited articles which the current user likes/has edited.
        for (User relatedUser : UserStatistics.getRelatedUsers(user)) {
            double similarity = calculateSimilatiry(relatedUser);
            // Since the Pearson's coefficient is between -1 and 1
            // and value close to 1 and -1 mean higher similarity
            // and we want to sort the users depending on the similarity
            // we are negating the absolute value of the coefficient
            pearsonCoefficients.put(relatedUser, -Math.abs(similarity));

        }

        // TODO check if these users have something new to offer to the current
        // user and if not - get some of the not-so-similar users
        int closeUsers = 0;
        for (Map.Entry<User, Double> entry : pearsonCoefficients.entrySet()) {
            if (closeUsers++ >= neighboursNumber)
                break;

            closestUsersSimilarity.put(entry.getKey(), -entry.getValue());
        }

        return closestUsersSimilarity;

    }

    public double calculateSimilatiry(User otherUser) {
        Set<Movie> moviesInCommon = UserStatistics.getMoviesInCommon(user, otherUser);

        double numerator = 0;
        if (moviesInCommon.isEmpty()) {
            return numerator;
        }

        for (Movie commonMovie : moviesInCommon) {
            numerator += (otherUser.getRating(commonMovie) - UserStatistics.getMeanRating(otherUser))
                    * (user.getRating(commonMovie) - UserStatistics.getMeanRating(user));
        }

        double denominator = (moviesInCommon.size() * UserStatistics.getStandardDeviation(user, moviesInCommon) * UserStatistics
                .getStandardDeviation(otherUser, moviesInCommon));

        if (numerator == denominator)
            return 1;

        return numerator / denominator;
    }

    public SortedSet<Map.Entry<Movie, Double>> getMoviesToRecommend() {
        Map<Movie, Double> moviesRating = new HashMap<Movie, Double>();
        Set<Movie> allMovies = new TreeSet<Movie>();

        // Collect the possible new movies to recommend
        for (User neighbour : closestNeighbours.keySet()) {
            Set<Movie> neighbourMovies = UserStatistics.getMoviesDifference(neighbour, user);
            allMovies.addAll(neighbourMovies);
        }

        // Iterate through the movies and calculate the expected
        // rating for each movie
        for (Movie currentMovie : allMovies) {
            double expectedRating = getExpectedRating(currentMovie);
            moviesRating.put(currentMovie, expectedRating);
        }

        SortedSet<Map.Entry<Movie, Double>> sortedset = new TreeSet<Map.Entry<Movie, Double>>(
                new Comparator<Map.Entry<Movie, Double>>() {
                    @Override
                    public int compare(Map.Entry<Movie, Double> e1,
                            Map.Entry<Movie, Double> e2) {
                        if (e1.getValue().equals(e2.getValue()))
                            return e1.getKey().compareTo(e2.getKey());
                        return e1.getValue().compareTo(e2.getValue());
                    }
                });

        sortedset.addAll(moviesRating.entrySet());

        return sortedset;
    }

    public double getExpectedRating(Movie movie) {
        double numerator = 0;
        double denominator = 0;
        for (Map.Entry<User, Double> neighbourSimilatiry : closestNeighbours.entrySet()) {
            User neighbour = neighbourSimilatiry.getKey();
            Double similarity = neighbourSimilatiry.getValue();

            if (neighbour.getRating(movie) != 0) {
                numerator += (similarity * (neighbour.getRating(movie) - UserStatistics.getMeanRating(neighbour)));
                denominator += Math.abs(similarity);
            }
        }

        return UserStatistics.getMeanRating(user) + numerator / denominator;
    }
}
