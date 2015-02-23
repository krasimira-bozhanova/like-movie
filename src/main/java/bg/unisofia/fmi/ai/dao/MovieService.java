package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.MovieGenre;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class MovieService {
    private RuntimeExceptionDao<Movie, String> movieDao;
    private RuntimeExceptionDao<MovieGenre, Integer> movieGenreDao;

    public MovieService(ConnectionSource connectionSource) {
        try {
            movieDao = RuntimeExceptionDao.createDao(connectionSource, Movie.class);
            movieGenreDao = RuntimeExceptionDao.createDao(connectionSource, MovieGenre.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public Movie find(final String movieId) {
        return movieDao.queryForId(movieId);
    }

    public List<Movie> getRandom(long limit) {
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
        QueryBuilder<MovieGenre, Integer> movieGenreQb = movieGenreDao.queryBuilder();
        QueryBuilder<Movie, String> movieQb = movieDao.queryBuilder();
        List<MovieGenre> result = null;
        try {
            movieGenreQb.where().eq("genre_id", genre.getId());
            movieGenreQb.join(movieQb);
            result = movieGenreQb.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.stream().map(mg -> mg.getMovie()).collect(Collectors.toList());
    }

    public void save(final Movie movie) {
        movieDao.createOrUpdate(movie);
    }

    public void refresh(final Movie movie) {
        movieDao.refresh(movie);
    }

}
