package com.musicplaylist;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {
    public static void initialize() throws Exception {
        // The database itself must exist before this method runs.
        String songs = "CREATE TABLE IF NOT EXISTS songs ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "title VARCHAR(150) NOT NULL, "
                + "artist VARCHAR(150) NOT NULL, "
                + "duration INT NOT NULL)";
        String history = "CREATE TABLE IF NOT EXISTS play_history ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "song_id INT, title VARCHAR(150) NOT NULL, "
                + "artist VARCHAR(150) NOT NULL, "
                + "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(songs);
            statement.executeUpdate(history);
        }
    }
}
