package bg.unisofia.fmi.ai.imports;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Stream;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import bg.unisofia.fmi.ai.dao.GenreService;
import bg.unisofia.fmi.ai.dao.MovieService;
import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.db.util.DbUtil;
import bg.unisofia.fmi.ai.routes.Main;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataImporter {

    public static void movielensIntoRedisImporter(final Path datasetPath) throws IOException {
        try (Stream<String> lines = Files.lines(datasetPath, Charset.defaultCharset());
                Jedis jedis = Main.pool.getResource()) {

            final Transaction t = jedis.multi();
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\t");
                final String userId = "user_" + lineParts[0];
                final String movieId = "movie_" + lineParts[1];
                final double score = Double.parseDouble(lineParts[2]);

                t.zadd(movieId, score, userId);
                t.zadd(userId, score, movieId);
            });
            t.exec();
        }
    }

    public static void movielensIntoDbImporter(final String datasetPath) throws IOException, SQLException {

        ConnectionSource connectionSource = DbUtil.getConnectionSource();
        final MovieService movieService = new MovieService(connectionSource);
        final UserService userService = new UserService(connectionSource);
        final GenreService genreService = new GenreService(connectionSource);

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Movie.class);
        TableUtils.createTableIfNotExists(connectionSource, Genre.class);

        try (Stream<String> lines = Files.lines(Paths.get(datasetPath, "u.genre"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\\|");
                final String name = lineParts[0];
                final String genreId = lineParts[1];

                genreService.saveGenre(new Genre(genreId, name));
            });
        }

        try (Stream<String> lines = Files.lines(Paths.get(datasetPath, "u.item"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\\|");
                final String movieId = lineParts[0];
                final String name = lineParts[1];
                movieService.save(new Movie(movieId, name));
            });
        }

        try (Stream<String> lines = Files.lines(Paths.get(datasetPath, "u.user"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\\|");
                final String userId = lineParts[0];
                userService.save(new User(userId));
            });
        }

        try (Stream<String> lines = Files.lines(Paths.get(datasetPath, "u.data"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\t");
                final String userId = lineParts[0];
                final String movieId = lineParts[1];
                final double rating = Double.parseDouble(lineParts[2]);

                final User u = userService.find(userId);
                u.rate(movieService.find(movieId), rating);
                userService.save(u);
            });
        }
    }
}
