package bg.unisofia.fmi.ai.dao;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.routes.Main;

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
        final User user = userDao.queryForId(userId);

        try (Jedis jedis = Main.pool.getResource()) {
            Set<Tuple> ratingTuples = jedis.zrangeWithScores(user.getId(), 0, -1);

            ratingTuples.forEach(r -> {
                user.rate(new Movie(r.getElement(), ""), r.getScore()); // TODO
                });
        }

        return user;
    }

    public Set<User> getVotedUsers(final Movie movie) {
        try (Jedis jedis = Main.pool.getResource()) {
            Set<Tuple> voters = jedis.zrangeWithScores(movie.getId(), 0, -1);
            return voters.stream().map(v -> find(v.getElement())).collect(Collectors.toSet());
        }
    }

    public void save(final User user) {
        userDao.createOrUpdate(user);
    }

}
