import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class User implements Comparable<User> {

    private List<Rating> ratings;
    private final int id;

    public User(int id) {
        this.id = id;
        ratings = new ArrayList<Rating>();
    }

    public int getId() {
        return id;
    }

    public List<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public double getMeanRating() {
        double mean = 0;

        if (ratings.isEmpty()) {
            return mean;
        }

        for (Rating rating : ratings) {
            mean += rating.getRatingValue();
        }

        return mean / ratings.size();
    }

    public double getStandardDeviation(Set<Movie> movies) {
        double sumSquares = 0;

        if(movies.isEmpty()) {
            return 0;
        }

        // Get all ids of the movies in the set
        List<Integer> moviesIds = movies.stream().mapToInt(
                m -> m.getId()).boxed().collect(Collectors.toList());
        for (Rating rating : ratings) {
            if (movies.contains(rating.getMovie())) {
                sumSquares += Math.pow(
                        (rating.getRatingValue() - getMeanRating()), 2);
            }
        }

        return Math.sqrt(sumSquares / movies.size());
    }

    public double getRating(Movie movie) {
        List<Rating> ratingForMovie = ratings.stream().filter(
                r -> r.getMovie().getId() == movie.getId()).collect(Collectors.toList());
        if (ratingForMovie.isEmpty()) {
            return 0;
        }
        return ratingForMovie.get(0).getRatingValue();
    }

    public Set<User> getRelatedUsers() {
        Set<User> relatedUsers = new TreeSet<User>();

        for (Rating rating : ratings) {
            Set<User> usersForCurrentMovie = rating.getMovie().getVotedUsers();
            relatedUsers.addAll(usersForCurrentMovie);
        }

        relatedUsers.remove(this);
        return relatedUsers;
    }

    public Set<Movie> getMoviesInCommon(User otherUser) {
        Set<Movie> movies = ratings.stream()
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

    public Set<Movie> getMoviesDifference(User otherUser) {
        // Get all the movies that I like and the other user doesn't
        Set<Movie> movies = ratings.stream()
                .map(r -> r.getMovie()).collect(Collectors.toSet());

        for (Rating otherRating : otherUser.getRatings()) {
            Movie currentMovie = otherRating.getMovie();
            if (movies.contains(currentMovie)) {
                movies.remove(currentMovie);
            }
        }
        return movies;

    }

    @Override
    public int compareTo(User o) {
        if (this.id > o.getId())
            return 1;
        return this.id == o.getId() ? 0 : -1;
    }
}
