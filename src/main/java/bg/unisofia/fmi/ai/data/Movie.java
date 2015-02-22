package bg.unisofia.fmi.ai.data;

import java.io.Serializable;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;

public class Movie implements Comparable<Movie>, Serializable {
    private static final long serialVersionUID = -7596256343825532435L;

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String title;

    private List<String> genres;

    public Movie() {
    }

    public Movie(final String id, final String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public String getId() {
        return id;
    }

    public List<String> getGenres() {
        return genres;
    }

    // TODO remove
    public void addUserVote(User user) {
        // this.votedUsers.add(user);
    }

    // TODO remove
    public void removeUserVote(User user) {
        // if (this.votedUsers.contains(user)) {
        // this.votedUsers.remove(user);
        // }
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
