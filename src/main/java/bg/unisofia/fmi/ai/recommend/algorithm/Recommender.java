package bg.unisofia.fmi.ai.recommend.algorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import bg.unisofia.fmi.ai.dao.MovieService;
import bg.unisofia.fmi.ai.dao.UserService;
import bg.unisofia.fmi.ai.data.Genre;
import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.User;
import bg.unisofia.fmi.ai.db.util.DbUtil;

import com.j256.ormlite.support.ConnectionSource;

public class Recommender {
    private User user;
    public final DataModel model;

    private final TanimotoCoefficientSimilarity similarity;

    final ConnectionSource connection = DbUtil.getConnectionSource();
    final UserService userService = new UserService(connection);
    final MovieService movieService = new MovieService(connection);

    public Recommender() {
        try {
            model = new FileDataModel(new File("src/main/resources/datasets/wiki/recommend.data"));
        } catch (Exception oops) {
            throw new RuntimeException();
        }
        similarity = new TanimotoCoefficientSimilarity(model);
    }

    public Recommender(Integer userId) {
        this();
        if (userId != null) {
            this.user = userService.find(userId);
        }
    }

    private GenericUserBasedRecommender getUserRecommender() {
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        return new GenericBooleanPrefUserBasedRecommender(model, neighborhood, similarity);
    }

    private GenericItemBasedRecommender getItemRecommender() {
        return new GenericBooleanPrefItemBasedRecommender(model, similarity);
    }

    private List<Integer> getWatched() {
        return user != null ? user.getWatchedMoviesIds() : new ArrayList<Integer>();
    }

    private List<Integer> getLiked() {
        return user != null ? user.getLikedMoviesIds() : new ArrayList<Integer>();
    }

    public List<Movie> getTopMovies(int number) {
        if (user != null && user.getRatings().size() > 0) {
            List<RecommendedItem> recommendedItems = new ArrayList<>();
            List<Integer> watched = getWatched();
            try {
                recommendedItems = getUserRecommender().recommend(user.getId(), number + watched.size());
            } catch (TasteException e) {
                e.printStackTrace();
            }
            Set<Integer> recommendedMovies = recommendedItems.stream().map(r -> (int) r.getItemID())
                    .collect(Collectors.toSet());
            recommendedMovies.removeAll(watched);
            int retrievedNumber = recommendedMovies.size() < number ? recommendedMovies.size() : number;
            List<Movie> resultList = recommendedMovies.stream().map(i -> movieService.find(i))
                    .collect(Collectors.toList());
            return resultList.subList(0, retrievedNumber);
        }
        return getRandomMovies(number);
    }

    public List<Movie> getSimilarMovies(int number, int movieId) {
        List<RecommendedItem> recommendedItems = new ArrayList<>();
        List<Integer> watched = getWatched();
        List<Integer> liked = getLiked();
        try {
            recommendedItems = getItemRecommender().mostSimilarItems(movieId, number + watched.size() + liked.size());
        } catch (TasteException e) {
            e.printStackTrace();
        }
        Set<Integer> recommendedMovies = recommendedItems.stream().map(r -> (int) r.getItemID())
                .collect(Collectors.toSet());
        recommendedMovies.removeAll(watched);
        recommendedMovies.removeAll(liked);

        int retrievedNumber = recommendedMovies.size() < number ? recommendedMovies.size() : number;
        List<Movie> resultList = recommendedMovies.stream().map(i -> movieService.find(i)).collect(Collectors.toList());
        return resultList.subList(0, retrievedNumber);

    }

    public List<Movie> getMoviesWithGenre(int number, Genre genre) {
        List<Integer> likedMovies = getLiked();
        List<Integer> watchedMovies = getWatched();

        List<Integer> listMoviesWithGenre = movieService.getRandomWithGenre(
                number + likedMovies.size() + watchedMovies.size(), genre);
        Set<Integer> setMoviesWithGenre = new TreeSet<Integer>(listMoviesWithGenre);

        setMoviesWithGenre.removeAll(likedMovies);
        setMoviesWithGenre.removeAll(watchedMovies);

        int retrievedNumber = setMoviesWithGenre.size() < number ? setMoviesWithGenre.size() : number;

        List<Movie> resultList = setMoviesWithGenre.stream().map(i -> movieService.find(i))
                .collect(Collectors.toList());
        return resultList.subList(0, retrievedNumber);
    }

    private List<Movie> getRandomMovies(int number) {
        List<Integer> likedMovies = getLiked();
        List<Integer> watchedMovies = getWatched();

        List<Integer> listMovies = movieService.getRandom(number + likedMovies.size() + watchedMovies.size());
        Set<Integer> setMovies = new TreeSet<Integer>(listMovies);

        setMovies.removeAll(likedMovies);
        setMovies.removeAll(watchedMovies);

        int retrievedNumber = setMovies.size() < number ? setMovies.size() : number;

        List<Movie> resultList = setMovies.stream().map(i -> movieService.find(i)).collect(Collectors.toList());
        return resultList.subList(0, retrievedNumber);
    }

}
