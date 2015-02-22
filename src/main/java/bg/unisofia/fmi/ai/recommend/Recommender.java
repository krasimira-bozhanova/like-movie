package bg.unisofia.fmi.ai.recommend;

import java.util.List;

import bg.unisofia.fmi.ai.dao.MovieService;
import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.db.util.DbUtil;

public interface Recommender {

    MovieService movieService = new MovieService(DbUtil.getConnectionSource());

    public default MovieService getMovieService() {
        return movieService;
    }

    public List<Movie> getTopMovies(int number);

    public List<Movie> getSimilarMovies(int number, Movie movie);

    public List<Movie> getMoviesWithGenre(int number, Genre genre);

}
