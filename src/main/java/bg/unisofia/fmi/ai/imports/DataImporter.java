package bg.unisofia.fmi.ai.imports;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Stream;

import bg.unisofia.fmi.ai.dao.GenreService;
import bg.unisofia.fmi.ai.dao.MovieGenreService;
import bg.unisofia.fmi.ai.dao.MovieService;
import bg.unisofia.fmi.ai.dao.RatingService;
import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.MovieGenre;
import bg.unisofia.fmi.ai.data.Rating;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.db.util.DbUtil;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataImporter {

    public static void movielensIntoDbImporter(final String datasetPath) throws IOException, SQLException {
        final ConnectionSource connectionSource = DbUtil.getConnectionSource();

        final MovieService movieService = new MovieService(connectionSource);
        final UserService userService = new UserService(connectionSource);
        final GenreService genreService = new GenreService(connectionSource);
        final RatingService ratingService = new RatingService(connectionSource);
        final MovieGenreService movieGenreService = new MovieGenreService(connectionSource);

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Movie.class);
        TableUtils.createTableIfNotExists(connectionSource, Genre.class);
        TableUtils.createTableIfNotExists(connectionSource, Rating.class);
        TableUtils.createTableIfNotExists(connectionSource, MovieGenre.class);

        try (Stream<String> lines = Files.lines(Paths.get(datasetPath, "u.genre"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\\|");
                final String name = lineParts[0];
                final String genreId = lineParts[1];

                genreService.save(new Genre(Integer.parseInt(genreId), name));
            });
        }

        try (Stream<String> lines = Files.lines(Paths.get(datasetPath, "u.item"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\\|");
                final String movieId = lineParts[0];
                final String title = lineParts[1];

                final String imdbUrl = lineParts[4];
//                try {
//                    URL url = new URL(imdbUrl);
//                    URLConnection con = url.openConnection();
//                    con.connect();
//                    InputStream is = con.getInputStream();
//                    System.out.println("redirected url: " + con.getURL());
//                    is.close();
//                } catch (IOException ex) {
//                }

                final Movie movie = new Movie(movieId, title);
                movieService.save(movie);

                for (int i = 6; i < lineParts.length; i++) {
                    if (lineParts[i].equals("1")) {
                        final Genre genre = genreService.find(i - 5);
                        movieGenreService.save(new MovieGenre(movie, genre));
                    }
                }
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

                final User u = userService.find(lineParts[0]);
                final Movie m = movieService.find(lineParts[1]);
                final double rating = Double.parseDouble(lineParts[2]);

                ratingService.save(new Rating(u, m, rating));
            });
        }
    }
}
