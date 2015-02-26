package bg.unisofia.fmi.ai.data;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class Movie implements Comparable<Movie> {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String imdbId;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Rating> ratings;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<MovieGenre> genres;

    public Movie() {
    }

    public Movie(final String id, final String imdbId) {
        this.id = id;
        this.imdbId = imdbId;
    }

    public String getId() {
        return id;
    }

    public ForeignCollection<MovieGenre> getGenres() {
        return genres;
    }

    public ForeignCollection<Rating> getRatings() {
        return ratings;
    }

    public String getImdbId() {
        return imdbId;
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
