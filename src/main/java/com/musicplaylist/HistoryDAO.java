package com.musicplaylist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class HistoryDAO {
    public void addHistory(Song song) throws Exception {
        String sql = "INSERT INTO play_history(song_id, title, artist) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, song.getId());
            ps.setString(2, song.getTitle());
            ps.setString(3, song.getArtist());
            ps.executeUpdate();
        }
    }

    public void showHistory() throws Exception {
        String sql = "SELECT title, artist, played_at FROM play_history ORDER BY id DESC";
        try (Connection con = DBConnection.getConnection();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            System.out.println("\n--- Listening History ---");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(rs.getString("title") + " - "
                        + rs.getString("artist") + " | " + rs.getTimestamp("played_at"));
            }
            if (!found) System.out.println("No listening history yet.");
        }
    }
}
