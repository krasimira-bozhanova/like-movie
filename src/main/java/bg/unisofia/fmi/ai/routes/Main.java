package bg.unisofia.fmi.ai.routes;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import spark.ModelAndView;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.omdb.MovieInfo;
import bg.unisofia.fmi.ai.recommend.MovieRecommender;
import bg.unisofia.fmi.ai.template.FreeMarkerEngine;

public class Main {
    public static JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

    public static void main(String[] args) throws IOException {
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

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            List<Movie> movies = recommender.getFrontPageMovies(5);
            attributes.put("message", "Hello World!");
            attributes.put("categories", categories);
            attributes.put("movies", movies);

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        get("/category/:categoryName", (request, response) -> {
            String chosenCategory = request.params(":categoryName");
            System.out.println(chosenCategory);
            Map<String, Object> attributes = new HashMap<>();
            List<Movie> movies = recommender.getMoviesFromCategory(5, chosenCategory);
            attributes.put("message", "Hello World!");
            attributes.put("categories", categories);
            attributes.put("movies", movies);

            return null;
        });

        get("/register", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Register");
            return new ModelAndView(attributes, "register.ftl");

        }, new FreeMarkerEngine());

        post("/register", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String passwordRepeat = request.queryParams("repeat_password");
            String message;
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
            String chosenTitle = request.params(":movieName");
            Movie movie = Movie.getMovieWithTitle(chosenTitle);

            List<Movie> movies = recommender.getSimilarMovies(5, movie);
            attributes.put("categories", categories);
            attributes.put("movie", new MovieInfo(chosenTitle));
            attributes.put("movies", movies);
            return new ModelAndView(attributes, "preview.ftl");

        }, new FreeMarkerEngine());

    }
}
