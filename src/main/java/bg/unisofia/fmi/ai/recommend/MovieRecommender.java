package bg.unisofia.fmi.ai.recommend;

import java.util.List;
import java.util.stream.Collectors;

import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.omdb.MovieInfo;

public class MovieRecommender {

    User currentUser;
    KNN currentKNN;

    public List<MovieInfo> getFrontPageMovies(int number) {
        // TODO - check for logged in user
        User loggedInUser = new User("a");
        if (loggedInUser != null) {
            if (!loggedInUser.equals(currentUser)) {
                currentUser = loggedInUser;
                currentKNN = new KNN(currentUser, 10);
            }
            return currentKNN.getLimitedMoviesToRecommend(number).stream()
                    .map(m -> new MovieInfo(m.getTitle()))
                    .collect(Collectors.toList());
        }
        return ColdStart.getRandomMovies(number).stream()
                .map(m -> new MovieInfo(m.getTitle()))
                .collect(Collectors.toList());
    }

    public List<MovieInfo> getMoviesFromCategory(int number, String category) {
        // TODO - check for logged in user
        User loggedInUser = new User("a");
        if (loggedInUser != null) {
            if (!loggedInUser.equals(currentUser)) {
                currentUser = loggedInUser;
                currentKNN = new KNN(currentUser, 10);
            }
            return currentKNN.getLimitedMoviesWithCategory(number, category).stream()
                    .map(m -> new MovieInfo(m.getTitle()))
                    .collect(Collectors.toList());
        }
        return ColdStart.getRandomMoviesWithCategory(number, category).stream()
                .map(m -> new MovieInfo(m.getTitle()))
                .collect(Collectors.toList());
    }

    public List<MovieInfo> getSimilarMovies(int number, MovieInfo movieInfo) {
        Movie movie = Movie.getMovieWithTitle(movieInfo.getTitle());
        // TODO - check for logged in user
        User loggedInUser = new User("a");
        if (loggedInUser != null) {
            if (!loggedInUser.equals(currentUser)) {
                currentUser = loggedInUser;
                currentKNN = new KNN(currentUser, 10);
            }
            return currentKNN.getLimitedMoviesWithCategory(number,
                    movie.getCategory()).stream()
                    .map(m -> new MovieInfo(m.getTitle()))
                    .collect(Collectors.toList());
        }
        return ColdStart.getSimilarMovies(movie, number).stream()
                .map(m -> new MovieInfo(m.getTitle()))
                .collect(Collectors.toList());
    }

}
