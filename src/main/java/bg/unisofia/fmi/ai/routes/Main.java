package bg.unisofia.fmi.ai.routes;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import spark.ModelAndView;
import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.db.util.DbUtil;
import bg.unisofia.fmi.ai.imports.DataImporter;
import bg.unisofia.fmi.ai.omdb.MovieInfo;
import bg.unisofia.fmi.ai.recommend.MovieRecommender;
import bg.unisofia.fmi.ai.template.FreeMarkerEngine;

public class Main {
    public static JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

    public static void main(String[] args) throws IOException, SQLException {
        staticFileLocation("/web");
        List<String> categories = new ArrayList<String>();
        MovieRecommender recommender = new MovieRecommender();

        categories.add("Adventure");
        categories.add("Drama");
        categories.add("Horror");
        categories.add("Action");
        categories.add("Mistery");
        categories.add("Comedy");
        categories.add("Family");
        categories.add("Animation");

        // DataImporter.movielensIntoDbImporter("src/main/resources/datasets/");

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            List<MovieInfo> movies = recommender.getFrontPageMovies(5);
            attributes.put("message", "Hello World!");
            attributes.put("categories", categories);
            attributes.put("movies", movies);

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        get("/category/:categoryName", (request, response) -> {
            String chosenCategory = request.params(":categoryName");
            System.out.println(chosenCategory);
            Map<String, Object> attributes = new HashMap<>();
            List<MovieInfo> movies = recommender.getMoviesFromCategory(5, chosenCategory);
            attributes.put("message", "Hello World!");
            attributes.put("categories", categories);
            attributes.put("movies", movies);

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        get("/register", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Register");
            return new ModelAndView(attributes, "register.ftl");

        }, new FreeMarkerEngine());

        post("/register", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String passwordRepeat = request.queryParams("repeat_password");

            try {
                User.registerUser(username, password, passwordRepeat);
            } catch (Exception e) {
                response.redirect("/register");
            }
            response.redirect("/");
            return request;
        });

        get("/login", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Login");
            return new ModelAndView(attributes, "login.ftl");

        }, new FreeMarkerEngine());

        get("/preview/:movieName", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "preview");
            String chosenTitle = null;
            try {
                chosenTitle = URLDecoder.decode(request.params(":movieName"), "UTF-8");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(chosenTitle);
            MovieInfo movieInfo = new MovieInfo(chosenTitle);

            List<MovieInfo> movies = recommender.getSimilarMovies(5, movieInfo);
            attributes.put("categories", categories);
            attributes.put("movie", new MovieInfo(chosenTitle));
            attributes.put("movies", movies);
            return new ModelAndView(attributes, "preview.ftl");

        }, new FreeMarkerEngine());

    }
}
