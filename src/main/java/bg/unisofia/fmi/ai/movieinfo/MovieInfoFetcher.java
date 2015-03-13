package bg.unisofia.fmi.ai.movieinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.recommend.Recommender;

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

    public MovieInfo getMovie(String id) {
        return new MovieInfo(id, movieFetcher.findMovie(id).getImdbId());
    }

    public List<MovieInfo> getFrontPageMovies(int number) {

        List<Movie> topMovies = movieFetcher.getTopMovies(number);

        return topMovies.stream()
                .map(m -> new MovieInfo(m.getId(), m.getImdbId()))
                .collect(Collectors.toList());
    }

    public List<MovieInfo> getMoviesWithGenre(int number, Genre genre) {
        List<Movie> moviesWithGenre = movieFetcher.getMoviesWithGenre(number,
                genre);

        return moviesWithGenre.stream()
                .map(m -> new MovieInfo(m.getId(), m.getImdbId()))
                .collect(Collectors.toList());
    }

    public List<MovieInfo> getSimilarMovies(int number, MovieInfo movieInfo) {
        Movie movie = movieFetcher.findMovie(movieInfo.getId());
        List<Movie> similarMovies = movieFetcher.getSimilarMovies(number, movie);

        return similarMovies.stream()
                .map(m -> new MovieInfo(m.getId(), m.getImdbId()))
                .collect(Collectors.toList());
    }

    private class MovieFetcher implements Recommender {

        Recommender currentRecommender;

        public void switchUser() {

        }

        public void switchUser(User user) {

        }

        @Override
        public List<Movie> getTopMovies(int number) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<Movie> getSimilarMovies(int number, Movie movie) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<Movie> getMoviesWithGenre(int number, Genre genre) {
            // TODO Auto-generated method stub
            return null;
        }

        public Movie findMovie(String id) {
            return getMovieService().find(id);
        }

    }

}
