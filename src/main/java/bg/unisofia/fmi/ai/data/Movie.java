import java.util.Set;
import java.util.TreeSet;


public class Movie implements Comparable<Movie> {

    private final int id;
    Set<User> votedUsers;

    public Movie(int id) {
        this.id = id;
        votedUsers = new TreeSet<User>();
    }

    public int getId() {
        return id;
    }

    public Set<User> getVotedUsers() {
        return votedUsers;
    }

    public void addUserVote(User user) {
        this.votedUsers.add(user);
    }

    public void removeUserVote(User user) {
        if (this.votedUsers.contains(user)) {
            this.votedUsers.remove(user);
        }
    }

    @Override
    public int compareTo(Movie o) {
        if (this.id > o.getId())
            return 1;
        return this.id == o.getId() ? 0 : -1;
    }

}
