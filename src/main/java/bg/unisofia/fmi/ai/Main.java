package bg.unisofia.fmi.ai;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import spark.ModelAndView;
import bg.unisofia.fmi.ai.dao.MovieDao;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.recommend.KNN;
import bg.unisofia.fmi.ai.recommend.UserStatistics;
import bg.unisofia.fmi.ai.template.FreeMarkerEngine;

public class Main {
    public static JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

    public static void main(String[] args) throws IOException {
        staticFileLocation("/web");

        final User u = MovieDao.findUser("user_196");
        System.out.println(UserStatistics.getMeanRating(u));
        new KNN(u, 10).getMoviesToRecommend().forEach((m, v) -> {
            System.out.println("movie:" + m.getId() + ", value:" + v);
        });

        get("/hello", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

    }
}
