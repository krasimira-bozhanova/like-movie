package bg.unisofia.fmi.ai.db.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import bg.unisofia.fmi.ai.data.Movie;
import bg.unisofia.fmi.ai.data.Rating;
import bg.unisofia.fmi.ai.data.User;

public class DatasetsUtil {

    private DatasetsUtil() {
    }

    private static PrintWriter getWriter(String filename) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filename, true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PrintWriter writer = new PrintWriter(new BufferedWriter(fileWriter));
        return writer;
    }

    public static void storeUser(User user) {
        List<String> properties = new ArrayList<String>();
        properties.add(user.getId().toString());
        properties.add("");
        properties.add("");
        properties.add("");
        properties.add("");
        properties.add(user.getUsername());
        properties.add(user.getPassword());

        PrintWriter writer = getWriter("src/main/resources/datasets/u.user");

        writer.println(String.join("|", properties));
        writer.close();

    }

    public static void storeMovie(Movie movie) {

    }

    public static void storerating(Rating rating) {

    }

}
