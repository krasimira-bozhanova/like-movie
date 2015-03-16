package bg.unisofia.fmi.ai.routes;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.ModelAndView;
import bg.unisofia.fmi.ai.dao.GenreService;
import bg.unisofia.fmi.ai.dao.MovieService;
import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.db.util.DbUtil;
import bg.unisofia.fmi.ai.movieinfo.MovieInfo;
import bg.unisofia.fmi.ai.movieinfo.MovieInfoFetcher;
import bg.unisofia.fmi.ai.template.FreeMarkerEngine;
import bg.unisofia.fmi.ai.transformers.JsonTransformer;

import com.j256.ormlite.support.ConnectionSource;

public class Main {
    public final static int SIMILAR_MOVIES_NUMBER = 4;
    public final static int FRONT_PAGE_MOVIES = 10;

    public static void main(String[] args) throws IOException, SQLException {
        staticFileLocation("/web");

        // DataImporter.movielensImporter("src/main/resources/datasets/movielens/");
        // DataImporter.customWikiExtractedFilesImporter("src/main/resources/datasets/wiki/");

        final User GUEST = new User("guest", "");
        final User currentUser = new User();
        currentUser.setUser(GUEST);

        final ConnectionSource connection = DbUtil.getConnectionSource();
        final GenreService genreService = new GenreService(connection);
        final UserService userService = new UserService(connection);
        final MovieService movieService = new MovieService(connection);

        List<Genre> genres = genreService.list();
        MovieInfoFetcher fetcher = new MovieInfoFetcher();

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            List<MovieInfo> movies = fetcher.getFrontPageMovies(FRONT_PAGE_MOVIES);
            attributes.put("genres", genres);
            attributes.put("selectedGenre", "all");
            attributes.put("movies", movies);
            attributes.put("user", currentUser);

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        get("/genre/:genreId", (request, response) -> {
            String chosenGenreId = request.params(":genreId");
            Genre genre = genreService.find(Integer.parseInt(chosenGenreId));

            Map<String, Object> attributes = new HashMap<>();
            List<MovieInfo> movies = fetcher.getMoviesWithGenre(FRONT_PAGE_MOVIES, genre);
            attributes.put("message", "Hello World!");
            attributes.put("genres", genres);
            attributes.put("selectedGenre", genre.getName());
            attributes.put("movies", movies);
            attributes.put("user", currentUser);

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
                return null;
            }
            User registeredUser = userService.login(username, password);
            currentUser.setUser(registeredUser);
            response.redirect("/");
            return request;
        });

        get("/login", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Login");
            return new ModelAndView(attributes, "login.ftl");

        }, new FreeMarkerEngine());

        post("/login", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");

            User user = null;

            try {
                user = userService.login(username, password);
            } catch (Exception e) {
                response.redirect("/login");
                return request;
            }

            currentUser.setUser(user);
            fetcher.switchUser(currentUser);
            response.redirect("/");

            return request;
        });

        get("/logout", (request, response) -> {
            currentUser.setUser(GUEST);
            response.redirect("/");

            return request;
        });

        get("/movies/:movieId", (request, response) -> {
            int chosenMovieId = Integer.parseInt(request.params(":movieId"));
            MovieInfo movieInfo = fetcher.getMovie(chosenMovieId);

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("genres", genres);
            attributes.put("movie", movieInfo);
            attributes.put("movies", fetcher.getSimilarMovies(SIMILAR_MOVIES_NUMBER, movieInfo));

            return new ModelAndView(attributes, "preview.ftl");
        }, new FreeMarkerEngine());

        get("/movies", (request, response) -> {
            final String searchText = request.queryParams("name");

            return movieService.autocompleteSearch(searchText);
        }, new JsonTransformer());

    }
}
