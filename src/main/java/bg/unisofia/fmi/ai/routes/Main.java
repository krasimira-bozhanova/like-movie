package bg.unisofia.fmi.ai.routes;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.ModelAndView;
import bg.unisofia.fmi.ai.dao.GenreService;
import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.db.util.DbUtil;
import bg.unisofia.fmi.ai.imports.DataImporter;
import bg.unisofia.fmi.ai.omdb.MovieFetcher;
import bg.unisofia.fmi.ai.omdb.MovieInfo;
import bg.unisofia.fmi.ai.template.FreeMarkerEngine;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        staticFileLocation("/web");

        DataImporter.movielensIntoDbImporter("src/main/resources/datasets/");

        GenreService genreService = new GenreService(DbUtil.getConnectionSource());
        UserService userService = new UserService(DbUtil.getConnectionSource());

        List<Genre> genres = genreService.list();

        MovieFetcher fetcher = new MovieFetcher();

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            //List<MovieInfo> movies = fetcher.getFrontPageMovies(5);
            List<MovieInfo> movies = new ArrayList<MovieInfo>();
            attributes.put("genres", genres);
            attributes.put("movies", movies);

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        get("/genre/:genreId", (request, response) -> {
            String chosenGenreId = request.params(":genreId");

            Genre genre = genreService.find(Integer.parseInt(chosenGenreId));

            Map<String, Object> attributes = new HashMap<>();
            List<MovieInfo> movies = fetcher.getMoviesWithGenre(5, genre);
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
                 userService.registerUser(username, password, passwordRepeat);
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
            String chosenMovieId = request.params(":movieId");

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
