package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.MovieGenre;
import bg.unisofia.fmi.ai.data.Rating;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

public class MovieService {
    private RuntimeExceptionDao<Movie, Integer> movieDao;
    private RuntimeExceptionDao<MovieGenre, Integer> movieGenreDao;
    private RuntimeExceptionDao<Rating, String> ratingDao;

    public MovieService(ConnectionSource connectionSource) {
        try {
            movieDao = RuntimeExceptionDao.createDao(connectionSource, Movie.class);
            movieGenreDao = RuntimeExceptionDao.createDao(connectionSource, MovieGenre.class);
            ratingDao = RuntimeExceptionDao.createDao(connectionSource, Rating.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public Movie find(final int movieId) {
        return movieDao.queryForId(movieId);
    }

    public List<Movie> getRandom(long limit) {
        QueryBuilder<Movie, Integer> queryBuilder = movieDao.queryBuilder();
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
        final List<Movie> result = new ArrayList<Movie>();

        GenericRawResults<String[]> rawResults = movieDao
                .queryRaw("select movie_id, title, imdbId, genre_id from movie m join (select * from moviegenre where genre_id="
                        + genre.getId() + ") g on m.id=g.movie_id limit " + limit);

        for (String[] resultArray : rawResults) {
            Movie movie = new Movie(Integer.parseInt(resultArray[0]), resultArray[1], resultArray[2]);
            result.add(movie);
        }

        return result;
    }

    public void create(final Movie movie) {
        movieDao.create(movie);
        movieDao.assignEmptyForeignCollection(movie, "ratings");
        movieDao.assignEmptyForeignCollection(movie, "watchings");
    }

    public void save(final Movie movie) {
        movieDao.createOrUpdate(movie);
    }

    public void refresh(final Movie movie) {
        movieDao.refresh(movie);
    }

    public List<Movie> autocompleteSearch(final String searchText) {
        try {
            return movieDao.query(movieDao.queryBuilder().limit(10l).where()
                    .like("title", new SelectArg("%" + searchText + "%")).prepare());
        } catch (SQLException e) {
            e.printStackTrace();

            return new ArrayList<>();
        }
    }
}
