package bg.unisofia.fmi.ai.data;


public class Rating {

    double ratingValue;
    Movie movie;

    public Rating(double rating, Movie movie) {
        this.ratingValue = rating;
        this.movie = movie;
    }

    public double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Movie getMovie() {
        return this.movie;
    }




}
