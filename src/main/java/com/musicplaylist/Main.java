package com.musicplaylist;

import com.musicplaylist.db.DatabaseSetup;
import com.musicplaylist.model.Playlist;
import com.musicplaylist.model.Song;
import com.musicplaylist.service.HistoryDAO;
import com.musicplaylist.service.PlaybackService;
import com.musicplaylist.service.SongDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);
    private static final SongDAO songDAO = new SongDAO();
    private static final HistoryDAO historyDAO = new HistoryDAO();
    private static final PlaybackService playback = new PlaybackService();
    private static final Playlist playlist = new Playlist("My Playlist");

    public static void main(String[] args) {
        try { DatabaseSetup.createTables(); }
        catch (SQLException e) { System.out.println("Database connection failed: " + e.getMessage()); System.out.println("Check MySQL is running and configure MUSIC_DB_URL, MUSIC_DB_USER and MUSIC_DB_PASSWORD."); return; }
        System.out.println("\n===== MUSIC PLAYLIST ENGINE =====");
        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Choose: ");
            try {
                switch (choice) {
                    case 1 -> addSong();
                    case 2 -> showSongs();
                    case 3 -> searchSongs();
                    case 4 -> deleteSong();
                    case 5 -> addToPlaylist();
                    case 6 -> showPlaylist();
                    case 7 -> playNext();
                    case 8 -> playPrevious();
                    case 9 -> { playback.pause(); }
                    case 10 -> playback.resume();
                    case 11 -> playback.stop();
                    case 12 -> { playlist.shuffle(); System.out.println("Playlist shuffled."); }
                    case 13 -> { playlist.sortByTitle(); System.out.println("Sorted by title."); }
                    case 14 -> { playlist.sortByArtist(); System.out.println("Sorted by artist."); }
                    case 15 -> showHistory();
                    case 0 -> { playback.stop(); running = false; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) { System.out.println("Database error: " + e.getMessage()); }
        }
        System.out.println("Thank you for using Music Playlist Engine!");
    }
    private static void showMenu() {
        System.out.println("\n1 Add song | 2 View songs | 3 Search | 4 Delete song");
        System.out.println("5 Add song to playlist | 6 View playlist | 7 Next | 8 Previous");
        System.out.println("9 Pause | 10 Resume | 11 Stop | 12 Shuffle");
        System.out.println("13 Sort by title | 14 Sort by artist | 15 Listening history | 0 Exit");
    }
    private static void addSong() throws SQLException {
        System.out.print("Title: "); String title = input.nextLine().trim();
        System.out.print("Artist: "); String artist = input.nextLine().trim();
        int duration = readInt("Duration in seconds: ");
        if (title.isEmpty() || artist.isEmpty() || duration <= 0) { System.out.println("Enter valid song details."); return; }
        System.out.println("Added: " + songDAO.add(new Song(title, artist, duration)));
    }
    private static void showSongs() throws SQLException { printSongs(songDAO.getAll()); }
    private static void searchSongs() throws SQLException { System.out.print("Search title/artist: "); printSongs(songDAO.search(input.nextLine().trim())); }
    private static void deleteSong() throws SQLException { int id = readInt("Song ID to delete: "); System.out.println(songDAO.delete(id) ? "Song deleted." : "Song ID not found."); }
    private static void addToPlaylist() throws SQLException {
        int id = readInt("Song ID to add: ");
        for (Song song : songDAO.getAll()) if (song.getId() == id) { playlist.addSong(song); System.out.println("Added to playlist."); return; }
        System.out.println("Song ID not found.");
    }
    private static void showPlaylist() { System.out.println("\n" + playlist.getName()); printSongs(playlist.getSongs()); }
    private static void playNext() throws SQLException { play(playlist.next()); }
    private static void playPrevious() throws SQLException { play(playlist.previous()); }
    private static void play(Song song) throws SQLException { if (song != null) historyDAO.record(song.getId()); playback.play(song); }
    private static void showHistory() throws SQLException { List<String> history = historyDAO.getRecent(); if (history.isEmpty()) System.out.println("No listening history yet."); else for (String row : history) System.out.println(row); }
    private static void printSongs(List<Song> songs) { if (songs.isEmpty()) System.out.println("No songs found."); else for (Song song : songs) System.out.println(song); }
    private static int readInt(String prompt) { while (true) { System.out.print(prompt); try { int value = Integer.parseInt(input.nextLine().trim()); return value; } catch (NumberFormatException e) { System.out.println("Please enter a number."); } } }
}
