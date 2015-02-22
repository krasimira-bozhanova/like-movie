package bg.unisofia.fmi.ai.data;

import com.j256.ormlite.field.DatabaseField;

public class MovieGenre {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, index = true)
    private Movie movie;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Genre genre;

    public MovieGenre() {
    }

    public MovieGenre(final Movie movie, final Genre genre) {
        this.movie = movie;
        this.genre = genre;
    }

    public Movie getMovie() {
        return movie;
    }

    public Genre getGenre() {
        return genre;
    }

}
