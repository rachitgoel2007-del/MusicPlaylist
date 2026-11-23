
package com.musicplaylist.service;

import com.musicplaylist.db.DBConnection;
import com.musicplaylist.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {

    // Check duplicate song
    public boolean isDuplicate(String title, String artist)
            throws SQLException {

        String sql = "SELECT id FROM songs "
                   + "WHERE LOWER(TRIM(title)) = LOWER(TRIM(?)) "
                   + "AND LOWER(TRIM(artist)) = LOWER(TRIM(?))";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, title);
            p.setString(2, artist);

            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    // Add single song
    public Song add(Song song) throws SQLException {

        if (isDuplicate(song.getTitle(), song.getArtist())) {
            throw new SQLException(
                    "This song already exists for this artist."
            );
        }

        String sql = "INSERT INTO songs(title, artist, duration_seconds, genre) "
                   + "VALUES(?,?,?,?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            p.setString(1, song.getTitle());
            p.setString(2, song.getArtist());
            p.setInt(3, song.getDurationSeconds());
            p.setString(4, song.getGenre());

            p.executeUpdate();

            try (ResultSet keys = p.getGeneratedKeys()) {
                if (keys.next()) {
                    song.setId(keys.getInt(1));
                }
            }
        }

        return song;
    }

    // Add multiple songs using transaction
    public boolean addMultiple(List<Song> songs) throws SQLException {

        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs to add.");
            return false;
        }

        // Check invalid details and duplicates inside the batch
        for (int i = 0; i < songs.size(); i++) {

            Song song = songs.get(i);

            if (song.getTitle() == null || song.getTitle().trim().isEmpty()
                    || song.getArtist() == null
                    || song.getArtist().trim().isEmpty()
                    || song.getGenre() == null
                    || song.getGenre().trim().isEmpty()
                    || song.getDurationSeconds() <= 0) {

                throw new SQLException("Invalid song details.");
            }

            if (isDuplicate(song.getTitle(), song.getArtist())) {
                throw new SQLException(
                        "Song already exists: " + song.getTitle()
                );
            }

            for (int j = 0; j < i; j++) {

                Song previous = songs.get(j);

                if (previous.getTitle().trim().equalsIgnoreCase(
                        song.getTitle().trim())
                        && previous.getArtist().trim().equalsIgnoreCase(
                        song.getArtist().trim())) {

                    throw new SQLException(
                            "Duplicate song in batch: " + song.getTitle()
                    );
                }
            }
        }

        String sql = "INSERT INTO songs(title, artist, duration_seconds, genre) "
                   + "VALUES(?,?,?,?)";

        try (Connection c = DBConnection.getConnection()) {

            c.setAutoCommit(false);

            try (PreparedStatement p = c.prepareStatement(sql)) {

                for (Song song : songs) {

                    p.setString(1, song.getTitle());
                    p.setString(2, song.getArtist());
                    p.setInt(3, song.getDurationSeconds());
                    p.setString(4, song.getGenre());

                    p.executeUpdate();
                }

                c.commit();
                return true;

            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    // Display all songs
    public List<Song> getAll() throws SQLException {

        List<Song> songs = new ArrayList<>();

        String sql = "SELECT * FROM songs ORDER BY id";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            while (r.next()) {
                songs.add(map(r));
            }
        }

        return songs;
    }

    // Search songs
    public List<Song> search(String query) throws SQLException {

        List<Song> songs = new ArrayList<>();

        String sql = "SELECT * FROM songs "
                   + "WHERE title LIKE ? OR artist LIKE ? ORDER BY title";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            String pattern = "%" + query + "%";

            p.setString(1, pattern);
            p.setString(2, pattern);

            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    songs.add(map(r));
                }
            }
        }

        return songs;
    }

    // Filter songs by genre
    public List<Song> filterByGenre(String genre) throws SQLException {

        List<Song> songs = new ArrayList<>();

        String sql = "SELECT * FROM songs "
                   + "WHERE LOWER(genre) = LOWER(?) ORDER BY title";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, genre);

            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    songs.add(map(r));
                }
            }
        }

        return songs;
    }

    // Recommend songs by genre or artist

public List<Song> getRecommendations(int songId) throws SQLException {

    List<Song> songs = new ArrayList<>();

    String sql = "SELECT * FROM songs "
               + "WHERE id <> ? AND ("
               + "LOWER(TRIM(genre)) = LOWER(TRIM((SELECT genre FROM songs WHERE id = ?))) "
               + "OR LOWER(TRIM(artist)) = LOWER(TRIM((SELECT artist FROM songs WHERE id = ?)))) "
               + "ORDER BY CASE "
               + "WHEN LOWER(TRIM(genre)) = LOWER(TRIM((SELECT genre FROM songs WHERE id = ?))) "
               + "THEN 0 ELSE 1 END, title";

    try (Connection c = DBConnection.getConnection();
         PreparedStatement p = c.prepareStatement(sql)) {

        p.setInt(1, songId);
        p.setInt(2, songId);
        p.setInt(3, songId);
        p.setInt(4, songId);

        try (ResultSet r = p.executeQuery()) {

            while (r.next()) {
                songs.add(map(r));
            }
        }
    }

    return songs;
    }


    // Delete song
    public boolean delete(int id) throws SQLException {

        String sql = "DELETE FROM songs WHERE id=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }

    // Convert database row into Song
    private Song map(ResultSet r) throws SQLException {

        return new Song(
                r.getInt("id"),
                r.getString("title"),
                r.getString("artist"),
                r.getInt("duration_seconds"),
                r.getString("genre")
        );
    }
}
