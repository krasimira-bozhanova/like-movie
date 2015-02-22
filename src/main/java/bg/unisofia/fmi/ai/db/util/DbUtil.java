package bg.unisofia.fmi.ai.db.util;

import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class DbUtil {

    private DbUtil() {
    }

    public static ConnectionSource getConnectionSource() {
        try {
            return new JdbcConnectionSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        } catch (SQLException e) {
            throw new RuntimeException("Problems initializing database objects", e);
        }
    }
}
