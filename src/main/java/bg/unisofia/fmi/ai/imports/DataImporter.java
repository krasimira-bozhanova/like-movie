package bg.unisofia.fmi.ai.imports;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Stream;

import bg.unisofia.fmi.ai.dao.GenreService;
import bg.unisofia.fmi.ai.dao.MovieService;
import bg.unisofia.fmi.ai.dao.RatingService;
import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.Rating;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.db.util.DbUtil;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataImporter {

    public static void movielensIntoDbImporter(final String datasetPath)
            throws IOException, SQLException {

        ConnectionSource connectionSource = DbUtil.getConnectionSource();
        final MovieService movieService = new MovieService(connectionSource);
        final UserService userService = new UserService(connectionSource);
        final GenreService genreService = new GenreService(connectionSource);
        final RatingService ratingService = new RatingService(connectionSource);

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Movie.class);
        TableUtils.createTableIfNotExists(connectionSource, Genre.class);
        TableUtils.createTableIfNotExists(connectionSource, Rating.class);

        try (Stream<String> lines = Files.lines(
                Paths.get(datasetPath, "u.genre"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\\|");
                final String name = lineParts[0];
                final String genreId = lineParts[1];

                genreService.saveGenre(new Genre(genreId, name));
            });
        }

        try (Stream<String> lines = Files.lines(
                Paths.get(datasetPath, "u.item"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\\|");
                final String movieId = lineParts[0];
                final String imdbUrl = lineParts[4];
                try {
                    URL url = new URL(imdbUrl);
                    URLConnection con = url.openConnection();
                    con.connect();
                    InputStream is = con.getInputStream();
                    System.out.println("redirected url: " + con.getURL());
                    is.close();

                } catch (IOException ex) {

                }
                final String name = lineParts[1].substring(0,
                        lineParts[1].lastIndexOf("(") - 1);
                movieService.save(new Movie(movieId, name));
            });
        }

        try (Stream<String> lines = Files.lines(
                Paths.get(datasetPath, "u.user"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\\|");
                final String userId = lineParts[0];
                userService.save(new User(userId));
            });
        }

        try (Stream<String> lines = Files.lines(
                Paths.get(datasetPath, "u.data"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\t");

                final User u = userService.find(lineParts[0]);
                final Movie m = movieService.find(lineParts[1]);
                final double rating = Double.parseDouble(lineParts[2]);

                ratingService.save(new Rating(u, m, rating));
            });
        }
    }
}
