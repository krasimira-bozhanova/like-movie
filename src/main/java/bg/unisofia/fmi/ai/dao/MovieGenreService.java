package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;

import bg.unisofia.fmi.ai.data.MovieGenre;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;

public class MovieGenreService {
    private RuntimeExceptionDao<MovieGenre, Integer> movieGenreDao;

    public MovieGenreService(ConnectionSource connectionSource) {
        try {
            movieGenreDao = RuntimeExceptionDao.createDao(connectionSource, MovieGenre.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public void save(MovieGenre movieGenre) {
        movieGenreDao.createOrUpdate(movieGenre);
    }

    // TODO remove me
    public RuntimeExceptionDao<MovieGenre, Integer> getDao() {
        return movieGenreDao;
    }

}
