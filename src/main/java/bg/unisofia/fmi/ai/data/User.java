package bg.unisofia.fmi.ai.data;
import java.util.ArrayList;
import java.util.List;

public class User implements Comparable<User> {

    private List<Rating> ratings;
    private final int id;

    public User(int id) {
        this.id = id;
        ratings = new ArrayList<Rating>();
    }

    public int getId() {
        return id;
    }

    public List<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public int compareTo(User o) {
        if (this.id > o.getId())
            return 1;
        return this.id == o.getId() ? 0 : -1;
    }
}
