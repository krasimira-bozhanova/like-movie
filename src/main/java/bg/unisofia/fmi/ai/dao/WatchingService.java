package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;

import bg.unisofia.fmi.ai.data.Watching;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;

public class WatchingService {
    private RuntimeExceptionDao<Watching, String> watchingDao;

    public WatchingService(ConnectionSource connectionSource) {
        try {
            watchingDao = RuntimeExceptionDao.createDao(connectionSource, Watching.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public void save(final Watching watching) {
        watchingDao.createOrUpdate(watching);
    }

}
