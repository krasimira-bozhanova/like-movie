package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;

import bg.unisofia.fmi.ai.data.Movie;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;

public class MovieService {
    private RuntimeExceptionDao<Movie, String> movieDao;

    public MovieService(ConnectionSource connectionSource) {
        try {
            movieDao = RuntimeExceptionDao.createDao(connectionSource, Movie.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public Movie find(final String movieId) {
        return movieDao.queryForId(movieId);
    }

    public void save(Movie movie) {
        movieDao.createOrUpdate(movie);
    }

}
