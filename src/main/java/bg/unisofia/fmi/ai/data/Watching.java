package bg.unisofia.fmi.ai.data;

import com.j256.ormlite.field.DatabaseField;

public class Watching {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, index = true)
    private User user;

    @DatabaseField(foreign = true, index = true)
    private Movie movie;

    public Watching() {
    }

    public Watching(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    public User getUser() {
        return user;
    }

}
