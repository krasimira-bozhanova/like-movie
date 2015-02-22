package bg.unisofia.fmi.ai.recommend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import bg.unisofia.fmi.ai.dao.MovieDao;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;

public class ColdStart {

    public static List<Movie> getRandomMovies(int number) {
        return null;
    }

    public static List<Movie> getSimilarMovies(Movie movie, int number) {
        // People who liked this also liked...
        Set<User> usersForCurrentMovie = MovieDao.getVotedUsers(movie);
        Map<Movie, Double> relatedMoviesSum = new HashMap <>();
        Map<Movie, Integer> relatedMoviesCount = new HashMap <>();
        for (User currentUser: usersForCurrentMovie) {
            for(Movie currentMovie: currentUser.getRatings().keySet()) {
                double rating = currentUser.getRatings().get(currentMovie);

                if(!relatedMoviesSum.containsKey(currentMovie)) {
                    relatedMoviesSum.put(currentMovie, 0.0);
                    relatedMoviesCount.put(currentMovie, 0);
                }

                relatedMoviesSum.put(currentMovie, relatedMoviesSum.get(currentMovie) + rating);
                relatedMoviesCount.put(currentMovie, relatedMoviesCount.get(currentMovie) + 1);

            }
        }

        Map<Movie, Double> relatedMoviesRating = new HashMap<>();
        for(Map.Entry<Movie, Integer> entry: relatedMoviesCount.entrySet()) {
            Movie currentMovie = entry.getKey();
            double sum = relatedMoviesSum.get(currentMovie);
            double count = entry.getValue();
            relatedMoviesRating.put(currentMovie, sum/count);
        }

        SortedSet<Map.Entry<Movie, Double>> sortedMovieSet = new TreeSet<Map.Entry<Movie, Double>>(
                new Comparator<Map.Entry<Movie, Double>>() {
                    @Override
                    public int compare(Map.Entry<Movie, Double> e1,
                            Map.Entry<Movie, Double> e2) {
                        if (e1.getValue().equals(e2.getValue()))
                            return e1.getKey().compareTo(e2.getKey());
                        return e1.getValue().compareTo(e2.getValue());
                    }
                });

        sortedMovieSet.addAll(relatedMoviesRating.entrySet());

        List<Movie> limitedMovies = new ArrayList<>();
        int i = 0;
        for(Map.Entry<Movie, Double> entry: sortedMovieSet) {
            Movie currentMovie = entry.getKey();
            if (i++ < number) {
                limitedMovies.add(currentMovie);
            }
        }
        return limitedMovies;
    }

}
