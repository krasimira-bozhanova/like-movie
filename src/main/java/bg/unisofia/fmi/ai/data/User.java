package bg.unisofia.fmi.ai.data;

import java.util.Optional;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class User implements Comparable<User> {
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private Integer id;

    @DatabaseField
    private String username;

    @DatabaseField
    private String password;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Rating> ratings;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Watching> watchings;

    public User() {
    }

    public User(final int id, final String username, final String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public User(final int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getId() {
        return id;
    }

    public ForeignCollection<Rating> getRatings() {
        return ratings;
    }

    public double getRating(int movieId) {
        Optional<Double> rating = ratings.stream().filter(r -> r.getMovie().getId().equals(movieId)).map(Rating::getRating)
                .findFirst();

        return rating.orElse(0d);
    }

    public boolean getWatched(int movieId) {
        Optional<Watching> watching = watchings.stream().filter(w -> w.getMovie().getId().equals(movieId)).findFirst();

        return watching.isPresent();
    }

    public void setUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.ratings = user.getRatings();
        this.password = user.getPassword();

    }

    public void like(final Movie movie, final double rating) {
        // TODO
        // this.ratings.put(movie, rating);
    }

    public void unlike(final Movie movie) {

    }

    public void watch(final Movie movie) {

    }

    public void unwatch(final Movie movie) {

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

}
