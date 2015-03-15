package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import bg.unisofia.fmi.ai.data.Genre;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

public class GenreService {

    private RuntimeExceptionDao<Genre, Integer> genreDao;

    public GenreService(ConnectionSource connectionSource) {
        try {
            genreDao = RuntimeExceptionDao.createDao(connectionSource, Genre.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public List<Genre> list() {
        return genreDao.queryForAll();
    }

    public Genre find(final int genreId) {
        return genreDao.queryForId(genreId);
    }

    public void save(final Genre genre) {
        genreDao.createOrUpdate(genre);
    }

    public Optional<Genre> findByName(final String name) {
        final SelectArg nameArg = new SelectArg(name);
        final List<Genre> genresFound = genreDao.queryForEq("name", nameArg);

        return genresFound.size() == 0 ? Optional.empty() : Optional.of(genresFound.get(0));
    }

}
