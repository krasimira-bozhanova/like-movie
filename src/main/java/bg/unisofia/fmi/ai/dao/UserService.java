package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.db.util.DatasetsUtil;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;

public class UserService {

    private RuntimeExceptionDao<User, Integer> userDao;

    public UserService(ConnectionSource connectionSource) {
        try {
            userDao = RuntimeExceptionDao.createDao(connectionSource, User.class);
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }

    public User find(final int userId) {
        return userDao.queryForId(userId);
    }

    public User find(final String username, final String password) {
        Map<String, Object> queryArguments = new HashMap<String, Object>();
        queryArguments.put("username", username);
        queryArguments.put("password", password);
        return userDao.queryForFieldValues(queryArguments).get(0);
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

         User newUser = new User(username, password);
         save(newUser);
         DatasetsUtil.storeUser(newUser);

        return true;
    }

    public User login(String username, String password) throws Exception {
        Map<String, Object> queryArguments = new HashMap<String, Object>();
        queryArguments.put("username", username);
        queryArguments.put("password", password);
        List<User> user = userDao.queryForFieldValues(queryArguments);
        if (user.isEmpty()) {
            throw new Exception("Wrong username or password");
        }

        return user.get(0);
    }

    public void create(final User user) {
        userDao.create(user);
        userDao.assignEmptyForeignCollection(user, "ratings");
        userDao.assignEmptyForeignCollection(user, "watchings");
    }

    public void save(final User user) {
        userDao.createOrUpdate(user);
    }

    public void refresh(User user) {
        userDao.refresh(user);
    }

}
