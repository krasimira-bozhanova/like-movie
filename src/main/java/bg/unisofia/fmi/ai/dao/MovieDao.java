package bg.unisofia.fmi.ai.dao;

import java.util.Set;
import java.util.stream.Collectors;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import bg.unisofia.fmi.ai.Main;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;

public class MovieDao {

    public static Set<User> getVotedUsers(final Movie movie) {
        try (Jedis jedis = Main.pool.getResource()) {
            Set<Tuple> voters = jedis.zrangeWithScores(movie.getId(), 0, -1);
            return voters.stream().map(v -> findUser(v.getElement())).collect(Collectors.toSet());
        }
    }

    public static User findUser(final String username) {
        final User user = new User(username);

        try (Jedis jedis = Main.pool.getResource()) {
            Set<Tuple> ratingTuples = jedis.zrangeWithScores(user.getId(), 0, -1);

            ratingTuples.forEach(r -> {
                user.rate(new Movie(r.getElement()), r.getScore());
            });
        }

        return user;
    }

}
