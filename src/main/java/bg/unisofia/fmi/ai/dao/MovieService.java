package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

public class MovieService {
    private RuntimeExceptionDao<Movie, Integer> movieDao;

    public MovieService(ConnectionSource connectionSource) {
        try {
            movieDao = RuntimeExceptionDao.createDao(connectionSource, Movie.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public Movie find(final int movieId) {
        return movieDao.queryForId(movieId);
    }

    public List<Integer> getRandom(long limit) {
//        QueryBuilder<Movie, Integer> queryBuilder = movieDao.queryBuilder();
//        queryBuilder.selectColumns("movie_id");
//        queryBuilder.orderByRaw("RANDOM()");
//        queryBuilder.limit(limit);
//        List<Integer> result = null;
//        try {
//            result = queryBuilder.query();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        final List<Integer> result = new ArrayList<Integer>();

        GenericRawResults<String[]> rawResults = movieDao
                .queryRaw("select id from movie m order by RANDOM() limit " + limit);

        for (String[] resultArray : rawResults) {
            result.add(Integer.valueOf(resultArray[0]));
        }

        return result;
    }

    public List<Integer> getRandomWithGenre(int limit, Genre genre) {
        final List<Integer> result = new ArrayList<Integer>();

        GenericRawResults<String[]> rawResults = movieDao
                .queryRaw("select movie_id from movie m join (select * from moviegenre where genre_id="
                        + genre.getId() + ") g on m.id=g.movie_id limit " + limit);

        for (String[] resultArray : rawResults) {
            result.add(Integer.valueOf(resultArray[0]));
        }

        return result;
    }

    public void create(final Movie movie) {
        movieDao.create(movie);
        movieDao.assignEmptyForeignCollection(movie, "ratings");
        movieDao.assignEmptyForeignCollection(movie, "watchings");
        movieDao.assignEmptyForeignCollection(movie, "genres");
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
