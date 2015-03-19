package bg.unisofia.fmi.ai.recommend.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.Rating;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.recommend.Recommender;

public class ColdStart implements Recommender {

    @Override
    public List<Movie> getTopMovies(int number) {
        return movieService.getRandom(number);
    }

    @Override
    public List<Movie> getMoviesWithGenre(int number, Genre genre) {
        return movieService.getRandomWithGenre(number, genre);
    }

    @Override
    public List<Movie> getSimilarMovies(int number, Movie movie) {
        // People who liked this also liked...
        // TODO: Check genre
        movieService.refresh(movie);
        Set<User> usersForCurrentMovie = movie.getRatings().stream().map(r -> r.getUser()).collect(Collectors.toSet());

        Set<Movie> relatedMovies = new TreeSet<Movie>();
        for (User currentUser : usersForCurrentMovie) {
            userService.refresh(currentUser);
            for (Rating currentRating : currentUser.getRatings()) {
                Movie currentMovie = currentRating.getMovie();
                relatedMovies.add(currentMovie);
            }
        }

        List<Movie> relatedMoviesList = new ArrayList<Movie>();
        relatedMoviesList.addAll(relatedMovies);

        Random random = new Random();
        List<Movie> limitedMovies = new ArrayList<Movie>();
        for (int i = 0; i < number; i++) {
            int randomIndex = random.nextInt(relatedMoviesList.size());
            Movie currentMovie = relatedMoviesList.get(randomIndex);
            relatedMoviesList.remove(randomIndex);
            movieService.refresh(currentMovie);
            limitedMovies.add(currentMovie);
        }

        return limitedMovies;
    }


    private List<Movie> getSimilarMoviesWithRating(int number, Movie movie) {
        // People who liked this also liked the most...
        // TODO: Check genre
        movieService.refresh(movie);
        Set<User> usersForCurrentMovie = movie.getRatings().stream().map(r -> r.getUser()).collect(Collectors.toSet());

        Map<Movie, Double> relatedMoviesSum = new HashMap<>();
        Map<Movie, Integer> relatedMoviesCount = new HashMap<>();


        for (User currentUser : usersForCurrentMovie) {
            userService.refresh(currentUser);
            for (Rating currentRating : currentUser.getRatings()) {
                Movie currentMovie = currentRating.getMovie();

                double rating = currentRating.getRating();

                if (!relatedMoviesSum.containsKey(currentMovie)) {
                    relatedMoviesSum.put(currentMovie, 0.0);
                    relatedMoviesCount.put(currentMovie, 0);
                }

                relatedMoviesSum.put(currentMovie, relatedMoviesSum.get(currentMovie) + rating);
                relatedMoviesCount.put(currentMovie, relatedMoviesCount.get(currentMovie) + 1);
            }
        }

        Map<Movie, Double> relatedMoviesRating = new HashMap<>();
        for (Map.Entry<Movie, Integer> entry : relatedMoviesCount.entrySet()) {
            Movie currentMovie = entry.getKey();
            double sum = relatedMoviesSum.get(currentMovie);
            double count = entry.getValue();
            relatedMoviesRating.put(currentMovie, sum / count);
        }

        SortedSet<Map.Entry<Movie, Double>> sortedMovieSet = new TreeSet<Map.Entry<Movie, Double>>(
                new Comparator<Map.Entry<Movie, Double>>() {
                    @Override
                    public int compare(Map.Entry<Movie, Double> e1, Map.Entry<Movie, Double> e2) {
                        int doubleComparison = Double.compare(e1.getValue(), e2.getValue());
                        if (doubleComparison != 0)
                            return doubleComparison;

                        return e1.getKey().getId().compareTo(e2.getKey().getId());
                    }
                });

        sortedMovieSet.addAll(relatedMoviesRating.entrySet());


        List<Movie> limitedMovies = new ArrayList<Movie>();
        int i = 0;
        for (Map.Entry<Movie, Double> entry : sortedMovieSet) {
            Movie currentMovie = entry.getKey();
            if (i++ < number) {
                movieService.refresh(currentMovie);
                limitedMovies.add(currentMovie);
            }
        }

        return limitedMovies;
    }

}
