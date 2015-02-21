package bg.unisofia.fmi.ai.imports;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import bg.unisofia.fmi.ai.Main;

public class DataImporter {

    public DataImporter(final Path datasetPath) throws IOException {
        try (Stream<String> lines = Files.lines(datasetPath, Charset.defaultCharset());
                Jedis jedis = Main.pool.getResource()) {

            lines.forEachOrdered(line -> {
                final String[] lineParts = line.split("\t");
                final String userId = "user_" + lineParts[0];
                final String movieId = "movie_" + lineParts[1];
                final double score = Double.parseDouble(lineParts[2]);

                final Transaction t = jedis.multi();
                t.zadd(movieId, score, userId);
                t.zadd(userId, score, movieId);

                t.exec();
            });
        }
    }
}
