package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;
import java.util.List;

import bg.unisofia.fmi.ai.data.Genre;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;

public class GenreService {

    private RuntimeExceptionDao<Genre, String> genreDao;

    public GenreService(ConnectionSource connectionSource) {
        try {
            genreDao = RuntimeExceptionDao.createDao(connectionSource, Genre.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public List<Genre> listGenres() {
        return genreDao.queryForAll();
    }

    public void saveGenre(Genre genre) {
        genreDao.createOrUpdate(genre);
    }

}
