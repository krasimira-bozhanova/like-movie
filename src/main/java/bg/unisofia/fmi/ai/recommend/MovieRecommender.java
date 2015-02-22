package bg.unisofia.fmi.ai.recommend;

import java.util.ArrayList;
import java.util.List;

import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.omdb.MovieInfo;

public class MovieRecommender {

    User currentUser;
    KNN currentKNN;

    List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();

    public MovieRecommender() {
        movieInfos.add(new MovieInfo("The hunt"));
        movieInfos.add(new MovieInfo("Titanic"));
        movieInfos.add(new MovieInfo("12 years a slave"));

        movieInfos.add(new MovieInfo("Bad Teacher"));
        movieInfos.add(new MovieInfo("Knight and Day"));
    }

    public List<MovieInfo> getFrontPageMovies(int number) {

        return movieInfos;

        //TODO - remove above and uncomment below
        // TODO - check for logged in user
//        User loggedInUser = new User("a");
//        if (loggedInUser != null) {
//            if (!loggedInUser.equals(currentUser)) {
//                currentUser = loggedInUser;
//                currentKNN = new KNN(currentUser, 10);
//            }
//            return currentKNN.getLimitedMoviesToRecommend(number).stream()
//                    .map(m -> new MovieInfo(m.getTitle()))
//                    .collect(Collectors.toList());
//        }
//        return ColdStart.getRandomMovies(number).stream()
//                .map(m -> new MovieInfo(m.getTitle()))
//                .collect(Collectors.toList());
    }

    public List<MovieInfo> getMoviesFromCategory(int number, String category) {
        return movieInfos;

        // TODO - check for logged in user
//        User loggedInUser = new User("a");
//        if (loggedInUser != null) {
//            if (!loggedInUser.equals(currentUser)) {
//                currentUser = loggedInUser;
//                currentKNN = new KNN(currentUser, 10);
//            }
//            return currentKNN.getLimitedMoviesWithCategory(number, category).stream()
//                    .map(m -> new MovieInfo(m.getTitle()))
//                    .collect(Collectors.toList());
//        }
//        return ColdStart.getRandomMoviesWithCategory(number, category).stream()
//                .map(m -> new MovieInfo(m.getTitle()))
//                .collect(Collectors.toList());
    }

    public List<MovieInfo> getSimilarMovies(int number, MovieInfo movieInfo) {

        return movieInfos;
//
//        Movie movie = Movie.getMovieWithTitle(movieInfo.getTitle());
//        // TODO - check for logged in user
//        User loggedInUser = new User("a");
//        if (loggedInUser != null) {
//            if (!loggedInUser.equals(currentUser)) {
//                currentUser = loggedInUser;
//                currentKNN = new KNN(currentUser, 10);
//            }
//            return currentKNN.getLimitedMoviesWithCategory(number,
//                    movie.getCategory()).stream()
//                    .map(m -> new MovieInfo(m.getTitle()))
//                    .collect(Collectors.toList());
//        }
//        return ColdStart.getSimilarMovies(movie, number).stream()
//                .map(m -> new MovieInfo(m.getTitle()))
//                .collect(Collectors.toList());
    }

}
