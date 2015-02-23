package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

    public boolean isUsernameAvailable(final String username) {
        Map<String, Object> queryArguments = new HashMap<String, Object>();
        queryArguments.put("username", username);
        return userDao.queryForFieldValues(queryArguments).isEmpty();
    }

    public boolean registerUser(String username, String password, String repeatPassword) throws Exception {

        if (password.trim().equals("") || !password.equals(repeatPassword)) {
            throw new Exception("Uncorrect passwords");
        }

        if (!isUsernameAvailable(username)) {
            throw new Exception("User with this username already exists");
        }

        // User newUser = new User()
        // save(new User)

        return true;

    }

    public void save(final User user) {
        userDao.createOrUpdate(user);
    }

    public void refresh(User user) {
        userDao.refresh(user);
    }

}
