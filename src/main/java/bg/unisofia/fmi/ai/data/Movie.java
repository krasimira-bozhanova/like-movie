package bg.unisofia.fmi.ai.data;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class Movie implements Comparable<Movie> {

    @Expose
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private Integer id;

    @DatabaseField
    private String imdbId;

    @Expose
    @DatabaseField
    private String title;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Rating> ratings;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<MovieGenre> genres;

    public Movie() {
    }

    public Movie(final Integer id, final String title, final String imdbId) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getTitle() {
        return title;
    }

    public ForeignCollection<MovieGenre> getGenres() {
        return genres;
    }

    public ForeignCollection<Rating> getRatings() {
        return ratings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movie other = (Movie) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(Movie o) {
        return this.id.compareTo(o.id);
    }

}
