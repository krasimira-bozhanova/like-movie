package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;
import java.util.List;

import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
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

    public List<Movie> getRandom(int limit) {
        QueryBuilder<Movie, String> queryBuilder = movieDao.queryBuilder();
        queryBuilder.orderByRaw("RANDOM()");
        queryBuilder.limit(limit);
        List<Movie> result = null;
        try {
            result = queryBuilder.query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    public List<Movie> getRandomWithGenre(int limit, Genre genre) {
        // TODO: return appropriate result
        return null;
    }

    public void save(Movie movie) {
        movieDao.createOrUpdate(movie);
    }

}
