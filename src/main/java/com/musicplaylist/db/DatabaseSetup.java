
package com.musicplaylist.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    public static void createTables() throws SQLException {

        String songs = "CREATE TABLE IF NOT EXISTS songs ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "title VARCHAR(150) NOT NULL, "
                + "artist VARCHAR(150) NOT NULL, "
                + "duration_seconds INT NOT NULL, "
                + "genre VARCHAR(80) DEFAULT 'Unknown')";

        String history = "CREATE TABLE IF NOT EXISTS play_history ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "song_id INT NOT NULL, "
                + "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE)";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(songs);
            statement.executeUpdate(history);

            // Add genre column to an older songs table if it does not exist.
            try {
                statement.executeUpdate(
                    "ALTER TABLE songs ADD COLUMN genre VARCHAR(80) DEFAULT 'Unknown'"
                );
            } catch (SQLException e) {
                 // Error 1060 means the genre column already exists.
                if (e.getErrorCode() != 1060) {
                    throw e;
                }
            }
        }
    }
}
