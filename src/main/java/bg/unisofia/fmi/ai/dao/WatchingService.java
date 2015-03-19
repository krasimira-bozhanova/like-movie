package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import bg.unisofia.fmi.ai.data.Watching;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class WatchingService {
    private RuntimeExceptionDao<Watching, String> watchingDao;

    public WatchingService(ConnectionSource connectionSource) {
        try {
            watchingDao = RuntimeExceptionDao.createDao(connectionSource,
                    Watching.class);
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Problems initializing database objects", e);
        }
    }

    public void save(final Watching watching) {
        watchingDao.createOrUpdate(watching);
    }

    public boolean find(Integer userId, Integer movieId) {
        if (userId == null || movieId == null) {
            return false;
        }
        Map<String, Object> queryArguments = new HashMap<String, Object>();
        queryArguments.put("user_id", userId);
        queryArguments.put("movie_id", movieId);
        return watchingDao.queryForFieldValues(queryArguments).size() > 0;
    }

    public void remove(Integer userId, Integer movieId) {
        if (userId == null || movieId == null) {
            return;
        }
        DeleteBuilder deleteBuilder = watchingDao.deleteBuilder();
        try {
            deleteBuilder.where().eq("user_id", userId).and().eq("movie_id", movieId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
