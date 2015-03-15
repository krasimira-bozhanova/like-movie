package bg.unisofia.fmi.ai.movieinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.recommend.Recommender;
import bg.unisofia.fmi.ai.recommend.algorithm.ColdStart;
import bg.unisofia.fmi.ai.recommend.algorithm.KNN;

public class MovieInfoFetcher {

    MovieFetcher movieFetcher;

    List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();

    public MovieInfoFetcher() {
        movieFetcher = new MovieFetcher();
    }

    public void switchUser() {
        movieFetcher.switchUser();
    }

    public void switchUser(User user) {
        movieFetcher.switchUser(user);
    }

    public MovieInfo getMovie(int id) {
        return new MovieInfo(id, movieFetcher.findMovie(id).getImdbId());
    }

    public List<MovieInfo> getFrontPageMovies(int number) {

        List<Movie> topMovies = movieFetcher.getTopMovies(number);

        return topMovies.stream().map(m -> new MovieInfo(m.getId(), m.getImdbId())).collect(Collectors.toList());
    }

    public List<MovieInfo> getMoviesWithGenre(int number, Genre genre) {
        List<Movie> moviesWithGenre = movieFetcher.getMoviesWithGenre(number, genre);

        return moviesWithGenre.stream().map(m -> new MovieInfo(m.getId(), m.getImdbId())).collect(Collectors.toList());
    }

    public List<MovieInfo> getSimilarMovies(int number, MovieInfo movieInfo) {
        Movie movie = movieFetcher.findMovie(movieInfo.getId());
        List<Movie> similarMovies = movieFetcher.getSimilarMovies(number, movie);

        return similarMovies.stream().map(m -> new MovieInfo(m.getId(), m.getImdbId())).collect(Collectors.toList());
    }

    private class MovieFetcher implements Recommender {

        private final Recommender coldStartRecommender = new ColdStart();
        private final static int NEIGHBOURS_NUMBER = 10;

        Recommender primeRecommender;
        Recommender secondRecommender;

        public MovieFetcher() {
            primeRecommender = coldStartRecommender;
            secondRecommender = coldStartRecommender;
        }

        public void switchUser() {
            primeRecommender = coldStartRecommender;
            secondRecommender = coldStartRecommender;
        }

        public void switchUser(User user) {
            primeRecommender = new KNN(user, NEIGHBOURS_NUMBER);
            secondRecommender = coldStartRecommender;

        }

        @Override
        public List<Movie> getTopMovies(int number) {
            List<Movie> primeResult = primeRecommender.getTopMovies(number);
            List<Movie> secondResult = secondRecommender.getTopMovies(number - primeResult.size());
            primeResult.addAll(secondResult);
            return primeResult;
        }

        @Override
        public List<Movie> getSimilarMovies(int number, Movie movie) {
            List<Movie> primeResult = primeRecommender.getSimilarMovies(number, movie);
            List<Movie> secondResult = secondRecommender.getSimilarMovies(number - primeResult.size(), movie);
            primeResult.addAll(secondResult);
            return primeResult;
        }

        @Override
        public List<Movie> getMoviesWithGenre(int number, Genre genre) {
            List<Movie> primeResult = primeRecommender.getMoviesWithGenre(number, genre);
            List<Movie> secondResult = secondRecommender.getMoviesWithGenre(number - primeResult.size(), genre);
            primeResult.addAll(secondResult);
            return primeResult;
        }

        public Movie findMovie(int id) {
            return getMovieService().find(id);
        }

    }

}
