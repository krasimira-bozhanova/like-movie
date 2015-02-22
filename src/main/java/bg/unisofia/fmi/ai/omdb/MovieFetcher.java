package bg.unisofia.fmi.ai.omdb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.recommend.ColdStart;
import bg.unisofia.fmi.ai.recommend.KNN;
import bg.unisofia.fmi.ai.recommend.Recommender;

public class MovieFetcher {

    Recommender currentRecommender;

    List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();

    public MovieFetcher() {
        currentRecommender = new ColdStart();
        // movieInfos.add(new MovieInfo("The hunt"));
        // movieInfos.add(new MovieInfo("Titanic"));
        // movieInfos.add(new MovieInfo("12 years a slave"));
        //
        // movieInfos.add(new MovieInfo("Bad Teacher"));
        // movieInfos.add(new MovieInfo("Knight and Day"));
    }

    public Recommender getRecommender() {
        return this.currentRecommender;
    }

    public void Login(User user) {
        currentRecommender = new KNN(user, 10);
    }

    public void Logout() {
        currentRecommender = new ColdStart();
    }

    public List<MovieInfo> getFrontPageMovies(int number) {

        List<Movie> topMovies = currentRecommender.getTopMovies(number);

        return topMovies.stream()
                .map(m -> new MovieInfo(m.getId(), m.getTitle()))
                .collect(Collectors.toList());
    }

    public List<MovieInfo> getMoviesWithGenre(int number, Genre genre) {

        List<Movie> moviesWithGenre = currentRecommender.getMoviesWithGenre(number, genre);

        return moviesWithGenre
                .stream().map(m -> new MovieInfo(m.getId(), m.getTitle()))
                .collect(Collectors.toList());
    }

    public List<MovieInfo> getSimilarMovies(int number, MovieInfo movieInfo) {
        Movie movie = currentRecommender.getMovieService().find(
                movieInfo.getId());

        return currentRecommender.getSimilarMovies(number, movie).stream()
                .map(m -> new MovieInfo(m.getId(), m.getTitle()))
                .collect(Collectors.toList());
    }

}
