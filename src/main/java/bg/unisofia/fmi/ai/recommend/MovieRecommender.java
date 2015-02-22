package bg.unisofia.fmi.ai.recommend;

import java.util.List;

import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;

public class MovieRecommender {

    User currentUser;
    KNN currentKNN;

    public List<Movie> getFrontPageMovies(int number) {
        //TODO - check for logged in user
        User loggedInUser = new User("a");
        if (loggedInUser != null) {
            if (!loggedInUser.equals(currentUser)) {
                currentUser = loggedInUser;
                currentKNN = new KNN(currentUser, 10);
            }
            return currentKNN.getLimitedMoviesToRecommend(number);
        }
        return ColdStart.getRandomMovies(number);
    }

    public List<Movie> getMoviesFromCategory(int number, String category) {
      //TODO - check for logged in user
        User loggedInUser = new User("a");
        if (loggedInUser != null) {
            if (!loggedInUser.equals(currentUser)) {
                currentUser = loggedInUser;
                currentKNN = new KNN(currentUser, 10);
            }
            return currentKNN.getLimitedMoviesWithCategory(number, category);
        }
        return ColdStart.getRandomMoviesWithCategory(number, category);
    }

    public List<Movie> getSimilarMovies(int number, Movie movie) {
      //TODO - check for logged in user
        User loggedInUser = new User("a");
        if (loggedInUser != null) {
            if (!loggedInUser.equals(currentUser)) {
                currentUser = loggedInUser;
                currentKNN = new KNN(currentUser, 10);
            }
            return currentKNN.getLimitedMoviesWithCategory(number, movie.getCategory());
        }
        return ColdStart.getSimilarMovies(movie, number);
    }

}
