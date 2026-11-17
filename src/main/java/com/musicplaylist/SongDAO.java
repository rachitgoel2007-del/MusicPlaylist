package com.musicplaylist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    public void addSong(Song song) throws Exception {
        String sql = "INSERT INTO songs(title, artist, duration) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, song.getTitle());
            ps.setString(2, song.getArtist());
            ps.setInt(3, song.getDuration());
            ps.executeUpdate();
        }
    }

    public List<Song> getAllSongs() throws Exception {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                songs.add(new Song(rs.getInt("id"), rs.getString("title"),
                        rs.getString("artist"), rs.getInt("duration")));
            }
        }
        return songs;
    }

    public List<Song> searchSongs(String keyword) throws Exception {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE title LIKE ? OR artist LIKE ? ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    songs.add(new Song(rs.getInt("id"), rs.getString("title"),
                            rs.getString("artist"), rs.getInt("duration")));
                }
            }
        }
        return songs;
    }

    public Song getSongById(int id) throws Exception {
        String sql = "SELECT * FROM songs WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Song(rs.getInt("id"), rs.getString("title"),
                            rs.getString("artist"), rs.getInt("duration"));
                }
            }
        }
        return null;
    }

    public boolean deleteSong(int id) throws Exception {
        String sql = "DELETE FROM songs WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
