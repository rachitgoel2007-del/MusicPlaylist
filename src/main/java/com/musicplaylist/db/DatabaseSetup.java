package com.musicplaylist.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    public static void createTables() throws SQLException {
        String songs = "CREATE TABLE IF NOT EXISTS songs (id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(150) NOT NULL, artist VARCHAR(150) NOT NULL, duration_seconds INT NOT NULL)";
        String history = "CREATE TABLE IF NOT EXISTS play_history (id INT AUTO_INCREMENT PRIMARY KEY, song_id INT NOT NULL, played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE)";
        try (Connection connection = DBConnection.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(songs);
            statement.executeUpdate(history);
        }
    }
}
