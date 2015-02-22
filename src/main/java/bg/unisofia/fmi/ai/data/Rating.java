package bg.unisofia.fmi.ai.data;

import com.j256.ormlite.field.DatabaseField;

public class Rating {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, index = true)
    private User user;

    @DatabaseField(foreign = true, index = true)
    private Movie movie;

    @DatabaseField
    private double rating;

    public Rating() {
    }

    public Rating(final User u, final Movie m, final double rating) {
        this.user = u;
        this.movie = m;
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public Movie getMovie() {
        return movie;
    }

    public double getRating() {
        return rating;
    }

}
