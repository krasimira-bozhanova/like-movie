package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;

import bg.unisofia.fmi.ai.data.Rating;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;

public class RatingService {
    private RuntimeExceptionDao<Rating, String> ratingDao;

    public RatingService(ConnectionSource connectionSource) {
        try {
            ratingDao = RuntimeExceptionDao.createDao(connectionSource, Rating.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public void save(final Rating rating) {
        ratingDao.createOrUpdate(rating);
    }
}
