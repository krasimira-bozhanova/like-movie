package bg.unisofia.fmi.ai.movieinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

public class MovieInfo {

    private String title;
    private String year;
    private String runtime;
    private String genre;
    private String director;
    private String plot;
    private String image;
    private String writer;
    private String actors;
    private String language;
    private String country;
    private String awards;
    private String imdbRating;
    private int id;
    private String imdbId;

    public MovieInfo(int id, String imdbId) {
        this.id = id;
        this.imdbId = imdbId;

        retrieveData();
    }

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

    public String getImage() {
        return image;
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

    private void retrieveData() {
        String urlString = "http://www.omdbapi.com/?i=" + this.imdbId + "&plot=full&r=json";

        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        URLConnection yc;
        try {
            yc = url.openConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        String inputLine;
        String result = "";

        try {
            while ((inputLine = in.readLine()) != null) {
                result += inputLine;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject(result);

        // TODO: create object in static method
        System.out.println(obj + " " + this.imdbId);
        this.title = obj.getString("Title");
        this.year = obj.getString("Year");
        this.runtime = obj.getString("Runtime");
        this.genre = obj.getString("Genre");
        this.director = obj.getString("Director");
        this.plot = obj.getString("Plot");
        this.image = obj.getString("Poster");
        this.writer = obj.getString("Writer");
        this.actors = obj.getString("Actors");
        this.language = obj.getString("Language");
        this.country = obj.getString("Country");
        this.awards = obj.getString("Awards");
        this.imdbRating = obj.getString("imdbRating");
    }
}
