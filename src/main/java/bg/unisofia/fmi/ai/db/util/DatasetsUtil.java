package bg.unisofia.fmi.ai.db.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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

    public static void setPreference(int userId, int movieId) {
        PrintWriter writer = getWriter("src/main/resources/datasets/wiki/recommend.data");

        List<String> properties = new ArrayList<String>();
        properties.add(String.valueOf(userId));
        properties.add(String.valueOf(movieId));
        properties.add(String.valueOf(1));

        writer.println(String.join(",", properties));
        writer.close();
    }

    public static void removePreference(int userId, int movieId) {
        File inputFile = new File("src/main/resources/datasets/wiki/recommend.data");
        File tempFile = new File("src/main/resources/datasets/wiki/recommend.temp");

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            writer = new BufferedWriter(new FileWriter(tempFile));
            String lineToRemove = userId + "," + movieId;
            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if(trimmedLine.equals(lineToRemove)) continue;
                writer.write(currentLine + "\n");
            }
            writer.close();
            reader.close();
            boolean successful = tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
