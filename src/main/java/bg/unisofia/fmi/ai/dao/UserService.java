package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;

import bg.unisofia.fmi.ai.data.User;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;

public class UserService {

    private RuntimeExceptionDao<User, String> userDao;

    public UserService(ConnectionSource connectionSource) {
        try {
            userDao = RuntimeExceptionDao.createDao(connectionSource, User.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public User find(final String userId) {
        return userDao.queryForId(userId);
    }

    public void save(final User user) {
        userDao.createOrUpdate(user);
    }

}
