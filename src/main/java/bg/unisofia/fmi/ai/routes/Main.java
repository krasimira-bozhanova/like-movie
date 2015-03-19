package bg.unisofia.fmi.ai.routes;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.hh.oauth.subscribe.apis.FacebookApi;
import ru.hh.oauth.subscribe.core.builder.ServiceBuilder;
import ru.hh.oauth.subscribe.core.model.OAuthRequest;
import ru.hh.oauth.subscribe.core.model.Response;
import ru.hh.oauth.subscribe.core.model.Token;
import ru.hh.oauth.subscribe.core.model.Verb;
import ru.hh.oauth.subscribe.core.model.Verifier;
import ru.hh.oauth.subscribe.core.oauth.OAuthService;
import spark.ModelAndView;
import bg.unisofia.fmi.ai.dao.GenreService;
import bg.unisofia.fmi.ai.dao.MovieService;
import bg.unisofia.fmi.ai.dao.RatingService;
import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.dao.WatchingService;
import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.Rating;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.data.Watching;
import bg.unisofia.fmi.ai.db.util.DbUtil;
import bg.unisofia.fmi.ai.movieinfo.MovieInfo;
import bg.unisofia.fmi.ai.movieinfo.MovieInfoFetcher;
import bg.unisofia.fmi.ai.template.FreeMarkerEngine;
import bg.unisofia.fmi.ai.transformers.JsonTransformer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.j256.ormlite.support.ConnectionSource;

public class Main {
    public final static int SIMILAR_MOVIES_NUMBER = 4;
    public final static int FRONT_PAGE_MOVIES = 10;

    private static final String USERNAME_ATTR = "username";
    private static final String USERID_ATTR = "userId";

    private static final Token EMPTY_TOKEN = null;
    private static final OAuthService service = new ServiceBuilder().provider(FacebookApi.class)
            .apiKey("1424586531175233").apiSecret("82d013d5d8cf08c6789c2b9f3b1cd50b").scope("user_likes")
            .callback("http://localhost:4567/oauth_callback_facebook").build();
    private static final String authUrl = service.getAuthorizationUrl(EMPTY_TOKEN);

    public static void main(String[] args) throws IOException, SQLException {
        staticFileLocation("/web");

        // DataImporter.movielensImporter("src/main/resources/datasets/movielens/");
        // DataImporter.customWikiExtractedFilesImporter("src/main/resources/datasets/wiki/");

        get("/",
                (request, response) -> {

                    final ConnectionSource connection = DbUtil.getConnectionSource();
                    final GenreService genreService = new GenreService(connection);

                    Map<String, Object> attributes = new HashMap<>();
                    List<MovieInfo> movies = new MovieInfoFetcher(request.session().attribute(USERID_ATTR))
                            .getFrontPageMovies(FRONT_PAGE_MOVIES);
                    attributes.put("genres", genreService.list());
                    attributes.put("selectedGenre", "all");
                    attributes.put("movies", movies);
                    attributes.put("username", request.session().attribute(USERNAME_ATTR));
                    attributes.put("facebookAuthUrl", authUrl);

                    return new ModelAndView(attributes, "index.ftl");
                }, new FreeMarkerEngine());

        get("/oauth_callback_facebook", (request, response) -> {

            Verifier verifier = new Verifier(request.queryParams("code"));
            Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
            OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://graph.facebook.com/me", service);
            service.signRequest(accessToken, oauthRequest);
            Response oauthResponse = oauthRequest.send();

            // TODO here decide how to store user id for votes and stuff
                JsonParser parser = new JsonParser();
                JsonObject res = (JsonObject) parser.parse(oauthResponse.getBody());
                request.session().attribute(USERNAME_ATTR, res.get("name").getAsString());

                response.redirect("/");
                return null;
            });

        get("/genre/:genreId",
                (request, response) -> {
                    final ConnectionSource connection = DbUtil.getConnectionSource();
                    final GenreService genreService = new GenreService(connection);

                    String chosenGenreId = request.params(":genreId");
                    Genre genre = genreService.find(Integer.parseInt(chosenGenreId));

                    Map<String, Object> attributes = new HashMap<>();
                    List<MovieInfo> movies = new MovieInfoFetcher(request.session().attribute(USERID_ATTR))
                            .getMoviesWithGenre(FRONT_PAGE_MOVIES, genre);
                    attributes.put("message", "Hello World!");
                    attributes.put("genres", genreService.list());
                    attributes.put("selectedGenre", genre.getName());
                    attributes.put("movies", movies);
                    attributes.put("username", request.session().attribute(USERNAME_ATTR));

                    return new ModelAndView(attributes, "index.ftl");
                }, new FreeMarkerEngine());

        get("/register", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Register");
            return new ModelAndView(attributes, "register.ftl");

        }, new FreeMarkerEngine());

        post("/register", (request, response) -> {
            final ConnectionSource connection = DbUtil.getConnectionSource();
            final UserService userService = new UserService(connection);

            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String passwordRepeat = request.queryParams("repeat_password");

            try {
                userService.registerUser(username, password, passwordRepeat);
            } catch (Exception e) {
                response.redirect("/register");
                return null;
            }
            userService.login(username, password);
            request.session().attribute(USERNAME_ATTR, username);

            response.redirect("/");
            return request;
        });

        get("/login", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Login");

            return new ModelAndView(attributes, "login.ftl");

        }, new FreeMarkerEngine());

        post("/login", (request, response) -> {

            final ConnectionSource connection = DbUtil.getConnectionSource();
            final UserService userService = new UserService(connection);

            final String username = request.queryParams("username");
            final String password = request.queryParams("password");

            User loggedUser;
            try {
                loggedUser = userService.login(username, password);
            } catch (Exception e) {
                response.redirect("/login");
                return request;
            }

            request.session().attribute(USERID_ATTR, loggedUser.getId());
            request.session().attribute(USERNAME_ATTR, username);

            response.redirect("/");
            return null;
        });

        get("/logout", (request, response) -> {
            request.session().removeAttribute("username");
            response.redirect("/");

            return request;
        });

        get("/movies/:movieId",
                (request, response) -> {
                    final ConnectionSource connection = DbUtil.getConnectionSource();
                    final GenreService genreService = new GenreService(connection);
                    final RatingService ratingService = new RatingService(connection);

                    final WatchingService watchingService = new WatchingService(connection);

                    // int userId = request.session().attribute(USERID_ATTR);
                    final MovieInfoFetcher fetcher = new MovieInfoFetcher(request.session().attribute(USERID_ATTR));

                    int chosenMovieId = Integer.parseInt(request.params(":movieId"));
                    MovieInfo movieInfo = fetcher.getMovie(chosenMovieId);

                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("genres", genreService.list());
                    attributes.put("movie", movieInfo);
                    attributes.put("username", request.session().attribute(USERNAME_ATTR));
                    attributes.put("movies", fetcher.getSimilarMovies(SIMILAR_MOVIES_NUMBER, chosenMovieId));
                    attributes.put("liked", ratingService.find(request.session().attribute(USERID_ATTR), chosenMovieId));
                    attributes.put("watched",
                            watchingService.find(request.session().attribute(USERID_ATTR), chosenMovieId));
                    return new ModelAndView(attributes, "preview.ftl");
                }, new FreeMarkerEngine());

        get("/movies", (request, response) -> {
            final ConnectionSource connection = DbUtil.getConnectionSource();
            final MovieService movieService = new MovieService(connection);

            final String searchText = request.queryParams("name");

            return movieService.autocompleteSearch(searchText);
        }, new JsonTransformer());

        get("/movies/:movieId/like", (request, response) -> {
            final ConnectionSource connection = DbUtil.getConnectionSource();
            final RatingService ratingService = new RatingService(connection);
            final MovieService movieService = new MovieService(connection);
            final UserService userService = new UserService(connection);
            int chosenMovieId = Integer.parseInt(request.params(":movieId"));
            Movie movie = movieService.find(chosenMovieId);
            User user = userService.find(request.session().attribute(USERID_ATTR));
            // TODO: think about the value of rating
                Rating newRating = new Rating(user, movie, 1);
                ratingService.save(newRating);
                response.redirect("/movies/" + chosenMovieId);

                return null;
            });

        get("/movies/:movieId/watch", (request, response) -> {
            final ConnectionSource connection = DbUtil.getConnectionSource();
            final WatchingService watchingService = new WatchingService(connection);
            final MovieService movieService = new MovieService(connection);
            final UserService userService = new UserService(connection);
            int chosenMovieId = Integer.parseInt(request.params(":movieId"));
            Movie movie = movieService.find(chosenMovieId);
            User user = userService.find(request.session().attribute(USERID_ATTR));
            Watching newWatching = new Watching(user, movie);
            watchingService.save(newWatching);
            response.redirect("/movies/" + chosenMovieId);
            return null;
        });

        get("/movies/:movieId/unwatch", (request, response) -> {
            final ConnectionSource connection = DbUtil.getConnectionSource();
            final WatchingService watchingService = new WatchingService(connection);

            int chosenMovieId = Integer.parseInt(request.params(":movieId"));
            watchingService.remove(request.session().attribute(USERID_ATTR), chosenMovieId);
            response.redirect("/movies/" + chosenMovieId);
            return null;
        });

        get("/movies/:movieId/unlike", (request, response) -> {
            final ConnectionSource connection = DbUtil.getConnectionSource();
            final RatingService ratingService = new RatingService(connection);

            int chosenMovieId = Integer.parseInt(request.params(":movieId"));
            ratingService.remove(request.session().attribute(USERID_ATTR), chosenMovieId);
            response.redirect("/movies/" + chosenMovieId);
            return null;
        });

    }
}
