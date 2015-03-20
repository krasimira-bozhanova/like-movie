package bg.unisofia.fmi.ai.routes;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import bg.unisofia.fmi.ai.db.util.DatasetsUtil;
import bg.unisofia.fmi.ai.db.util.DbUtil;
import bg.unisofia.fmi.ai.movieinfo.MovieInfo;
import bg.unisofia.fmi.ai.recommend.algorithm.Recommender;
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

    private static final Properties FACEBOOK_CREDENTIALS = getApiCredentials("api/facebook-credentials.properties");
    private static final Token EMPTY_TOKEN = null;
    private static final OAuthService service = new ServiceBuilder().provider(FacebookApi.class)
            .apiKey(FACEBOOK_CREDENTIALS.getProperty("apiKey"))
            .apiSecret(FACEBOOK_CREDENTIALS.getProperty("apiSecret")).scope("email, user_likes")
            .callback("http://localhost:4567/oauth_callback_facebook").build();
    private static final String authUrl = service.getAuthorizationUrl(EMPTY_TOKEN);

    public static void main(String[] args) throws IOException, SQLException {
        staticFileLocation("/web");

        // DataImporter.movielensImporter("src/main/resources/datasets/movielens/");
        // DataImporter.customWikiExtractedFilesImporter("src/main/resources/datasets/wiki/");

        get("/", (request, response) -> {
            final ConnectionSource connection = DbUtil.getConnectionSource();
            final GenreService genreService = new GenreService(connection);

            Map<String, Object> attributes = new HashMap<>();
            Integer userId = request.session().attribute(USERID_ATTR);
            List<Movie> movies = new Recommender(userId).getTopMovies(FRONT_PAGE_MOVIES);
            List<MovieInfo> moviesInfos = MovieInfo.getInfos(movies);
            attributes.put("genres", genreService.list());
            attributes.put("movies", moviesInfos);
            attributes.put("username", request.session().attribute(USERNAME_ATTR));
            attributes.put("facebookAuthUrl", authUrl);

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());

        // TODO store id
        get("/oauth_callback_facebook", (request, response) -> {
            Verifier verifier = new Verifier(request.queryParams("code"));
            Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
            OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://graph.facebook.com/me", service);
            service.signRequest(accessToken, oauthRequest);
            Response oauthResponse = oauthRequest.send();

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
                    List<Movie> movies = new Recommender(request.session().attribute(USERID_ATTR)).getMoviesWithGenre(
                            FRONT_PAGE_MOVIES, genre);
                    List<MovieInfo> moviesInfos = MovieInfo.getInfos(movies);
                    attributes.put("genres", genreService.list());
                    attributes.put("selectedGenre", genre.getName());
                    attributes.put("movies", moviesInfos);
                    attributes.put("username", request.session().attribute(USERNAME_ATTR));
                    attributes.put("facebookAuthUrl", authUrl);

                    return new ModelAndView(attributes, "index.ftl");
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
                response.redirect("/");
                return null;
            }
            User user = userService.login(username, password);
            request.session().attribute(USERNAME_ATTR, username);
            request.session().attribute(USERID_ATTR, user.getId());

            response.redirect("/");
            return request;
        });

        post("/login", (request, response) -> {

            final ConnectionSource connection = DbUtil.getConnectionSource();
            final UserService userService = new UserService(connection);

            final String username = request.queryParams("username");
            final String password = request.queryParams("password");

            User loggedUser;
            try {
                loggedUser = userService.login(username, password);
            } catch (Exception e) {
                response.redirect("/");
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
                    final MovieService movieService = new MovieService(connection);
                    final WatchingService watchingService = new WatchingService(connection);

                    Integer userId = request.session().attribute(USERID_ATTR);
                    final Recommender recommender = new Recommender(userId);

                    int chosenMovieId = Integer.parseInt(request.params(":movieId"));
                    MovieInfo movieInfo = MovieInfo.create(movieService.find(chosenMovieId));
                    List<MovieInfo> similarMovies = MovieInfo.getInfos(recommender.getSimilarMovies(
                            SIMILAR_MOVIES_NUMBER, chosenMovieId));

                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("genres", genreService.list());
                    attributes.put("movie", movieInfo);
                    attributes.put("username", request.session().attribute(USERNAME_ATTR));
                    attributes.put("movies", similarMovies);
                    attributes.put("liked", ratingService.find(userId, chosenMovieId));
                    attributes.put("facebookAuthUrl", authUrl);
                    attributes.put("watched", watchingService.find(userId, chosenMovieId));
                    return new ModelAndView(attributes, "preview.ftl");
                }, new FreeMarkerEngine());

        get("/movies", (request, response) -> {
            final ConnectionSource connection = DbUtil.getConnectionSource();
            final MovieService movieService = new MovieService(connection);

            final String searchText = request.queryParams("name");

            return movieService.autocompleteSearch(searchText);
        }, new JsonTransformer());

        // TODO fix me later
        get("/movies/:movieId/like", (request, response) -> {
            final ConnectionSource connection = DbUtil.getConnectionSource();
            final RatingService ratingService = new RatingService(connection);
            final MovieService movieService = new MovieService(connection);
            final UserService userService = new UserService(connection);
            int chosenMovieId = Integer.parseInt(request.params(":movieId"));
            Movie movie = movieService.find(chosenMovieId);

            int userId = request.session().attribute(USERID_ATTR);
            User user = userService.find(userId);

            Rating newRating = new Rating(user, movie, 1);
            ratingService.save(newRating);
            DatasetsUtil.setPreference(userId, chosenMovieId);

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
            int userId = request.session().attribute(USERID_ATTR);
            ratingService.remove(userId, chosenMovieId);

            DatasetsUtil.removePreference(userId, chosenMovieId);
            response.redirect("/movies/" + chosenMovieId);

            return null;
        });

    }

    private static Properties getApiCredentials(final String path) {
        Properties prop = new Properties();

        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(path);
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return prop;
    }
}
