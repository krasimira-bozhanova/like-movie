package bg.unisofia.fmi.ai.recommend;

import java.util.List;

import bg.unisofia.fmi.ai.dao.MovieService;
import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.db.util.DbUtil;

import com.j256.ormlite.support.ConnectionSource;

public interface Recommender {

    ConnectionSource conn = DbUtil.getConnectionSource();
    MovieService movieService = new MovieService(conn);
    UserService userService = new UserService(conn);

    public default MovieService getMovieService() {
        return movieService;
    }

    public default UserService getUserService() {
        return userService;
    }

    public List<Movie> getTopMovies(int number);

    public List<Movie> getSimilarMovies(int number, Movie movie);

    public List<Movie> getMoviesWithGenre(int number, Genre genre);

}
