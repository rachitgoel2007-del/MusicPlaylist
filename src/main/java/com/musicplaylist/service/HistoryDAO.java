
package com.musicplaylist.service;

import com.musicplaylist.db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryDAO {

    // Save song playback history
    public void record(int songId) throws SQLException {

        String sql = "INSERT INTO play_history(song_id) VALUES(?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, songId);
            p.executeUpdate();
        }
    }

    // Get last 20 played songs
    public List<String> getRecent() throws SQLException {

        List<String> rows = new ArrayList<>();

        String sql = "SELECT s.title, s.artist, h.played_at "
                   + "FROM play_history h JOIN songs s ON s.id = h.song_id "
                   + "ORDER BY h.played_at DESC LIMIT 20";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            while (r.next()) {
                rows.add(r.getString("title") + " - "
                        + r.getString("artist") + " | "
                        + r.getTimestamp("played_at"));
            }
        }

        return rows;
    }

    // Get top 10 most played songs
    public List<String> getMostPlayed() throws SQLException {

        List<String> songs = new ArrayList<>();

        String sql = "SELECT s.title, s.artist, COUNT(h.id) AS play_count "
                   + "FROM play_history h JOIN songs s ON s.id = h.song_id "
                   + "GROUP BY s.id, s.title, s.artist "
                   + "ORDER BY play_count DESC LIMIT 10";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            while (r.next()) {
                songs.add(r.getString("title") + " - "
                        + r.getString("artist") + " | Plays: "
                        + r.getInt("play_count"));
            }
        }

        return songs;
    }

    // Calculate total listening time
    public long getTotalListeningTime() throws SQLException {

        String sql = "SELECT COALESCE(SUM(s.duration_seconds), 0) AS total_time "
                   + "FROM play_history h JOIN songs s ON s.id = h.song_id";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            if (r.next()) {
                return r.getLong("total_time");
            }
        }

        return 0;
    }
}
