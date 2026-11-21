package com.musicplaylist.service;

import com.musicplaylist.db.DBConnection;
import com.musicplaylist.model.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    public Song add(Song song) throws SQLException {
        String sql = "INSERT INTO songs(title, artist, duration_seconds) VALUES(?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, song.getTitle()); p.setString(2, song.getArtist()); p.setInt(3, song.getDurationSeconds()); p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) { if (keys.next()) song.setId(keys.getInt(1)); }
        }
        return song;
    }
    public List<Song> getAll() throws SQLException {
        List<Song> songs = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement("SELECT * FROM songs ORDER BY id"); ResultSet r = p.executeQuery()) {
            while (r.next()) songs.add(map(r));
        }
        return songs;
    }
    public List<Song> search(String query) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE title LIKE ? OR artist LIKE ? ORDER BY title";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            String pattern = "%" + query + "%"; p.setString(1, pattern); p.setString(2, pattern);
            try (ResultSet r = p.executeQuery()) { while (r.next()) songs.add(map(r)); }
        }
        return songs;
    }
    public boolean delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM songs WHERE id=?")) {
            p.setInt(1, id); return p.executeUpdate() > 0;
        }
    }
    private Song map(ResultSet r) throws SQLException {
        return new Song(r.getInt("id"), r.getString("title"), r.getString("artist"), r.getInt("duration_seconds"));
    }
}
