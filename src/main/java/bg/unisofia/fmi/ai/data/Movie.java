package bg.unisofia.fmi.ai.data;

public class Movie implements Comparable<Movie> {
    private final String id;
    private String category;
    //TODO: include title
    //private final String title;

    public Movie(final String id) {
        //this.title = title;
        this.id = id;
    }

//    public String getTitle() {
//        return this.title;
//    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
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

    public static Movie getMovieWithTitle(String title) {
        //TODO
        return new Movie("a");
    }

}
