package com.musicplaylist.service;

import com.musicplaylist.db.DBConnection;
import com.musicplaylist.model.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryDAO {
    public void record(int songId) throws SQLException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement("INSERT INTO play_history(song_id) VALUES(?)")) {
            p.setInt(1, songId); p.executeUpdate();
        }
    }
    public List<String> getRecent() throws SQLException {
        List<String> rows = new ArrayList<>();
        String sql = "SELECT s.title, s.artist, h.played_at FROM play_history h JOIN songs s ON s.id=h.song_id ORDER BY h.played_at DESC LIMIT 20";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql); ResultSet r = p.executeQuery()) {
            while (r.next()) rows.add(r.getString("title") + " - " + r.getString("artist") + " | " + r.getTimestamp("played_at"));
        }
        return rows;
    }
}
