package bg.unisofia.fmi.ai.movieinfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import bg.unisofia.fmi.ai.data.Movie;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class MovieInfo {
    private int id;
    private String title;
    private String year;
    private String runtime;
    private String genre;
    private String director;
    private String plot;
    private String poster;
    private String writer;
    private String actors;
    private String language;
    private String country;
    private String awards;
    private String imdbRating;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getPlot() {
        return plot;
    }

    public String getPoster() {
        return poster;
    }

    public String getWriter() {
        return writer;
    }

    public String getActors() {
        return actors;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getAwards() {
        return awards;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public static MovieInfo create(final Movie movie) {
        final String movieEndpoint = "http://www.omdbapi.com/?i=" + movie.getImdbId() + "&plot=full&r=json";

        InputStream content;
        try {
            URL url = new URL(movieEndpoint);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            content = (InputStream) request.getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        final JsonParser jp = new JsonParser();
        final JsonElement root = jp.parse(new InputStreamReader(content));

        final MovieInfo movieInfo = gson.fromJson(root, MovieInfo.class);
        movieInfo.id = movie.getId();
        movieInfo.title = movie.getTitle();
        movieInfo.imdbRating = root.getAsJsonObject().get("imdbRating").getAsString();

        return movieInfo;
    }

}
