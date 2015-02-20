import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class KNN {

    private final int neighboursNumber;

    private User user;
    private Map<User, Double> closestNeighbours;
    private SortedMap<Movie, Double> moviesToRecommend;

    public KNN(User user, int neighboursNumber) {
        this.user = user;
        this.neighboursNumber = neighboursNumber;
        closestNeighbours = getClosestNeighbours();
        moviesToRecommend = getMoviesToRecommend();
    }

    public Map<User, Double> getClosestNeighbours() {

        SortedMap<User, Double> pearsonCoefficients = new TreeMap<User, Double>();

        // Compute similarities between the current user and the users that
        // have edited articles which the current user likes/has edited.
        Set<User> relatedUsers = user.getRelatedUsers();
        Map<User, Double> closestUsersSimilarity = new HashMap<User, Double>();

        for (User relatedUser : relatedUsers) {
            double similarity = calculateSimilatiry(relatedUser);

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
        Set<Movie> moviesInCommon = user.getMoviesInCommon(otherUser);

        double numerator = 0;
        if (moviesInCommon.isEmpty()) {
            return numerator;
        }

        for (Movie commonMovie : moviesInCommon) {
            numerator += (otherUser.getRating(commonMovie) - otherUser
                    .getMeanRating())
                    * (user.getRating(commonMovie) - user.getMeanRating());
        }

        double denominator = (moviesInCommon.size()
                * user.getStandardDeviation(moviesInCommon) * otherUser
                .getStandardDeviation(moviesInCommon));

        if (numerator == denominator)
            return 1;

        return numerator / denominator;
    }

    public SortedMap<Movie, Double> getMoviesToRecommend() {
        SortedMap<Movie, Double> moviesRating = new TreeMap<Movie, Double>();
        // Collect the possible new movies to recommend

        Set<Movie> allMovies = new TreeSet<Movie>();
        for (User neighbour : closestNeighbours.keySet()) {
            Set<Movie> neighbourMovies = neighbour.getMoviesDifference(user);
            allMovies.addAll(neighbourMovies);
        }

        // Iterate through the movies and calculate the expected
        // rating for each movie

        for (Movie currentMovie : allMovies) {
            double expectedRating = getExpectedRating(currentMovie);
            moviesRating.put(currentMovie, expectedRating);
        }

        return moviesRating;
    }

    public double getExpectedRating(Movie movie) {

        double numerator = 0;
        double denominator = 0;
        for (Map.Entry<User, Double> neighbourSimilatiry : closestNeighbours
                .entrySet()) {
            User neighbour = neighbourSimilatiry.getKey();
            Double similarity = neighbourSimilatiry.getValue();

            numerator += (similarity * (neighbour.getRating(movie) - neighbour
                    .getMeanRating()));
            denominator += Math.abs(similarity);

        }

        return user.getMeanRating() + numerator / denominator;

    }
}
