package bg.unisofia.fmi.ai.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private User user1;
    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    @Before
    public void beforeEachSetRating() {
        user1 = new User(1);
        movie1 = new Movie(1, null, null);
        movie2 = new Movie(2, null, null);
        movie3 = new Movie(3, null, null);
    }

    @Test
    public void testGetRating() {
        user1.rate(movie1, 2);
        user1.rate(movie2, 1);

        assertThat(user1.getRating(movie1.getId())).isEqualTo(2);
        assertThat(user1.getRating(movie2.getId())).isEqualTo(1);
        assertThat(user1.getRating(movie3.getId())).isEqualTo(0);
    }

}
