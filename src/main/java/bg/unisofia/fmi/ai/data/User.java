package bg.unisofia.fmi.ai.data;

import java.util.HashMap;
import java.util.Map;

import com.j256.ormlite.field.DatabaseField;

public class User implements Comparable<User> {
    @DatabaseField(id = true)
    private String id;

    private Map<Movie, Double> ratings;

    public User() {
        this.ratings = new HashMap<>();
    }

    public User(final String id) {
        this.id = id;
        this.ratings = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Map<Movie, Double> getRatings() {
        return ratings;
    }

    public double getRating(final Movie movie) {
        return this.ratings.getOrDefault(movie, 0d);
    }

    // TODO
    public void rate(final Movie movie, final double rating) {
        this.ratings.put(movie, rating);
        movie.addUserVote(this);
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
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(User o) {
        return this.id.compareTo(o.id);
    }

    public static boolean registerUser(String username, String password, String repeatPassword) throws Exception {
        // TODO:
        // Check if there exists a user with the same username
        // If not - successful register
        if (password.trim().equals("") || !password.equals(repeatPassword)) {
            throw new Exception("Uncorrect passwords");
        }

        return true;

    }
}
