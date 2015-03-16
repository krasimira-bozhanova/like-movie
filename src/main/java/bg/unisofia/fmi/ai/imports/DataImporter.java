package bg.unisofia.fmi.ai.imports;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Supplier;
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

    public static void movielensImporter(final String datasetPath) throws IOException, SQLException {
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

        try (Stream<String> lines = Files.lines(Paths.get(datasetPath, "u.item.imdb"), Charset.forName("UTF-8"))) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\\|");
                final int movieId = Integer.parseInt(lineParts[0]);
                final String title = lineParts[1];
                final String imdbId = lineParts[4];

                final Movie movie = new Movie(movieId, title, imdbId);
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
                final int userId = Integer.parseInt(lineParts[0]);
                User user = new User(userId);
                if (lineParts.length > 5) {
                    String username = lineParts[5];
                    String password = lineParts[6];
                    user = new User(userId, username, password);
                }
                userService.save(user);
            });
        }

        try (Stream<String> lines = Files.lines(Paths.get(datasetPath, "u.data"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\t");

                final User u = userService.find(lineParts[0]);
                final Movie m = movieService.find(Integer.parseInt(lineParts[1]));
                final double rating = Double.parseDouble(lineParts[2]);

                ratingService.save(new Rating(u, m, rating));
            });
        }
    }

    public static void customWikiExtractedFilesImporter(final String filesPath) throws SQLException, IOException {
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

        try (Stream<String> lines = Files.lines(Paths.get(filesPath, "movies.data"), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split(":", 3);
                final int pageId = Integer.parseInt(lineParts[0]);
                final String imdbId = lineParts[1];
                final String[] categories = lineParts[2].split(",");

                // save movie
                // TODO add movie title
                final Movie movie = new Movie(pageId, null, imdbId);
                movieService.save(movie);

                // save its categories
                for (String categoryName : categories) {
                    final String normalizedName = categoryName.substring(1, categoryName.length() - 1);
                    final Optional<Genre> genre = genreService.findByName(normalizedName);

                    final Genre g = genre.orElseGet(new Supplier<Genre>() {
                        @Override
                        public Genre get() {
                            final Genre g = new Genre(normalizedName);
                            genreService.save(g);
                            return g;
                        }
                    });

                    movieGenreService.save(new MovieGenre(movie, g));
                }
            });
        }

    }
}
