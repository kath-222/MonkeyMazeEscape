package com.badlogic.mazegame;
import java.io.*;

public class ScoreDAO {
    private static final String FILE_NAME = "score.txt";

    public static void saveScore(int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(Integer.toString(score));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int loadScore() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return Integer.parseInt(line);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
