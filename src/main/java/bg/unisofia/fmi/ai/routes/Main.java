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

import spark.ModelAndView;
import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.db.util.DbUtil;
import bg.unisofia.fmi.ai.imports.DataImporter;
import bg.unisofia.fmi.ai.omdb.MovieFetcher;
import bg.unisofia.fmi.ai.omdb.MovieInfo;
import bg.unisofia.fmi.ai.template.FreeMarkerEngine;

public class Main {
    //public static JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

    public static void main(String[] args) throws IOException, SQLException {
        staticFileLocation("/web");
        List<String> genres = new ArrayList<String>();
        MovieFetcher fetcher = new MovieFetcher();

        genres.add("Adventure");
        genres.add("Drama");
        genres.add("Horror");
        genres.add("Action");
        genres.add("Mistery");
        genres.add("Comedy");
        genres.add("Family");
        genres.add("Animation");

        // DataImporter.movielensIntoDbImporter("src/main/resources/datasets/");

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            List<MovieInfo> movies = fetcher.getFrontPageMovies(5);
            attributes.put("message", "Hello World!");
            attributes.put("genres", genres);
            attributes.put("movies", movies);

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        get("/genre/:genreName", (request, response) -> {
            String chosenGenre = request.params(":genreName");

            Map<String, Object> attributes = new HashMap<>();
            List<MovieInfo> movies = fetcher.getMoviesWithGenre(5, chosenGenre);
            attributes.put("message", "Hello World!");
            attributes.put("genres", genres);
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

        get("/preview/:movieId", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            String chosenMovieId = null;
            try {
                chosenMovieId = URLDecoder.decode(request.params(":movieId"), "UTF-8");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Movie movie = fetcher.getRecommender().getMovieService().find(chosenMovieId);
            MovieInfo movieInfo = new MovieInfo(chosenMovieId, movie.getTitle());

            List<MovieInfo> movies = fetcher.getSimilarMovies(5, movieInfo);
            attributes.put("genres", genres);
            attributes.put("movie", movieInfo);
            attributes.put("movies", movies);
            return new ModelAndView(attributes, "preview.ftl");

        }, new FreeMarkerEngine());

    }
}
