
package com.musicplaylist;

import com.musicplaylist.db.DatabaseSetup;
import com.musicplaylist.model.Playlist;
import com.musicplaylist.model.Song;
import com.musicplaylist.service.HistoryDAO;
import com.musicplaylist.service.PlaybackService;
import com.musicplaylist.service.PlaylistFileManager;
import com.musicplaylist.service.PlaylistUndoService;
import com.musicplaylist.service.SongDAO;
import com.musicplaylist.service.SongSearchService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner input = new Scanner(System.in);

    private static final SongDAO songDAO = new SongDAO();
    private static final HistoryDAO historyDAO = new HistoryDAO();
    private static final PlaybackService playback = new PlaybackService();

    private static final Playlist playlist =
            new Playlist("My Playlist");

    private static final PlaylistUndoService undoService =
            new PlaylistUndoService();

    private static final SongSearchService searchService =
            new SongSearchService();

    private static final PlaylistFileManager fileManager =
            new PlaylistFileManager();

    public static void main(String[] args) {

        try {
            DatabaseSetup.createTables();
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.out.println("Check MySQL configuration.");
            return;
        }

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
                    case 7 -> playNextFromPlaylist();
                    case 8 -> playPreviousFromPlaylist();

                    case 9 -> playback.pause();
                    case 10 -> playback.resume();
                    case 11 -> playback.stop();

                    case 12 -> {
                        undoService.saveState(playlist);
                        playlist.shuffle();
                        System.out.println("Playlist shuffled.");
                    }

                    case 13 -> {
                        undoService.saveState(playlist);
                        playlist.sortByTitle();
                        System.out.println("Sorted by title.");
                    }

                    case 14 -> {
                        undoService.saveState(playlist);
                        playlist.sortByArtist();
                        System.out.println("Sorted by artist.");
                    }

                    case 15 -> showHistory();
                    case 16 -> removeSongFromPlaylist();
                    case 17 -> undoPlaylistChange();

                    case 18 -> addToPlaybackQueue();
                    case 19 -> playNextFromQueue();
                    case 20 -> playPreviousFromQueue();

                    case 21 -> playback.showQueue();

                    case 22 -> System.out.println(
                            "Playback state: " + playback.getPlaybackState()
                    );

                    case 23 -> filterSongsByGenre();
                    case 24 -> showRecommendations();

                    case 25 -> advancedSearch();
                    case 26 -> sortByArtistAndTitle();
                    case 27 -> sortByDuration();
                    case 28 -> filterByDuration();

                    case 29 -> savePlaylist();
                    case 30 -> loadPlaylist();

                    case 31 -> showMostPlayed();
                    case 32 -> showTotalListeningTime();
                    case 33 -> addMultipleSongs();

                    case 0 -> {
                        playback.stop();
                        running = false;
                    }

                    default -> System.out.println("Invalid choice.");
                }

            } catch (SQLException e) {
                System.out.println("Database operation failed: "
                        + e.getMessage());

            } catch (IOException e) {
                System.out.println("File operation failed: "
                        + e.getMessage());
            }
        }

        System.out.println("Thank you for using Music Playlist Engine!");
    }

    private static void showMenu() {

        System.out.println("\n========== MAIN MENU ==========");

        System.out.println("1 Add song | 2 View songs | 3 Search");
        System.out.println("4 Delete song");

        System.out.println("5 Add to playlist | 6 View playlist");
        System.out.println("7 Playlist Next | 8 Playlist Previous");

        System.out.println("9 Pause | 10 Resume | 11 Stop");
        System.out.println("12 Shuffle | 13 Sort by title");
        System.out.println("14 Sort by artist");

        System.out.println("15 Listening history");
        System.out.println("16 Remove playlist song");
        System.out.println("17 Undo last change");

        System.out.println("18 Add to playback queue");
        System.out.println("19 Queue Next | 20 Queue Previous");
        System.out.println("21 View queue | 22 Playback state");

        System.out.println("23 Filter by genre");
        System.out.println("24 Song recommendations");

        System.out.println("25 Advanced partial search");
        System.out.println("26 Sort by artist and title");
        System.out.println("27 Sort by duration");
        System.out.println("28 Filter by duration");

        System.out.println("29 Save playlist | 30 Load playlist");

        System.out.println("31 Most played songs");
        System.out.println("32 Total listening time");
        System.out.println("33 Bulk add songs");

        System.out.println("0 Exit");
        System.out.println("===============================");
    }

    private static void addSong() throws SQLException {

        System.out.print("Title: ");
        String title = input.nextLine().trim();

        System.out.print("Artist: ");
        String artist = input.nextLine().trim();

        int duration = readInt("Duration in seconds: ");

        System.out.print("Genre: ");
        String genre = input.nextLine().trim();

        if (title.isEmpty() || artist.isEmpty()
                || genre.isEmpty() || duration <= 0) {

            System.out.println("Enter valid song details.");
            return;
        }

        Song song = new Song(title, artist, duration, genre);

        System.out.println("Added: " + songDAO.add(song));
    }

    private static void showSongs() throws SQLException {
        printSongs(songDAO.getAll());
    }

    private static void searchSongs() throws SQLException {

        System.out.print("Search title/artist: ");
        String keyword = input.nextLine().trim();

        printSongs(songDAO.search(keyword));
    }

    private static void deleteSong() throws SQLException {

        int id = readInt("Song ID to delete: ");

        boolean deleted = songDAO.delete(id);

        if (deleted) {

            playlist.removeSong(id);
            playback.removeSong(id);
            undoService.removeSong(id);

            System.out.println("Song deleted successfully.");
            System.out.println("Song removed from playlist, playback and undo history.");

        } else {
            System.out.println("Song ID not found.");
        }
    }

    private static void addToPlaylist() throws SQLException {

        int id = readInt("Song ID to add: ");

        for (Song song : songDAO.getAll()) {

            if (song.getId() == id) {

                undoService.saveState(playlist);
                playlist.addSong(song);

                System.out.println("Added to playlist.");
                return;
            }
        }

        System.out.println("Song ID not found.");
    }

    private static void showPlaylist() {

        System.out.println("\n" + playlist.getName());
        printSongs(playlist.getSongs());
    }

    private static void playNextFromPlaylist() throws SQLException {
        play(playlist.next());
    }

    private static void playPreviousFromPlaylist() throws SQLException {
        play(playlist.previous());
    }

    private static void play(Song song) throws SQLException {

        if (song != null) {
            historyDAO.record(song.getId());
        }

        playback.play(song);
    }

    private static void removeSongFromPlaylist() {

        int id = readInt("Song ID to remove: ");

        boolean found = false;

        for (Song song : playlist.getSongs()) {

            if (song.getId() == id) {
                found = true;
                break;
            }
        }

        if (found) {

            undoService.saveState(playlist);
            playlist.removeSong(id);

            System.out.println("Song removed from playlist.");

        } else {
            System.out.println("Song not found in playlist.");
        }
    }

    private static void undoPlaylistChange() {

        if (undoService.undo(playlist)) {
            System.out.println("Last playlist change undone.");
        } else {
            System.out.println("No previous change to undo.");
        }
    }

    private static void addToPlaybackQueue() throws SQLException {

        int id = readInt("Enter song ID: ");

        for (Song song : songDAO.getAll()) {

            if (song.getId() == id) {
                playback.addToQueue(song);
                System.out.println("Song added to playback queue.");
                return;
            }
        }

        System.out.println("Song ID not found.");
    }

    private static void playNextFromQueue() throws SQLException {

        if (playback.playNext()) {
            recordCurrentSong();
        }
    }

    private static void playPreviousFromQueue() throws SQLException {

        if (playback.playPrevious()) {
            recordCurrentSong();
        }
    }

    private static void recordCurrentSong() throws SQLException {

        Song song = playback.getCurrentSong();

        if (song != null) {
            historyDAO.record(song.getId());
        }
    }

    private static void showHistory() throws SQLException {

        List<String> history = historyDAO.getRecent();

        if (history.isEmpty()) {
            System.out.println("No listening history yet.");
        } else {
            for (String row : history) {
                System.out.println(row);
            }
        }
    }

    private static void filterSongsByGenre() throws SQLException {

        System.out.print("Enter genre: ");
        String genre = input.nextLine().trim();

        if (genre.isEmpty()) {
            System.out.println("Please enter a genre.");
            return;
        }

        printSongs(songDAO.filterByGenre(genre));
    }

    private static void showRecommendations() throws SQLException {

        int id = readInt("Enter song ID for recommendations: ");

        List<Song> recommendations =
                songDAO.getRecommendations(id);

        System.out.println("\n===== RECOMMENDED SONGS =====");

        if (recommendations.isEmpty()) {
            System.out.println("No similar songs found.");
        } else {
            printSongs(recommendations);
        }
    }

    private static void advancedSearch() throws SQLException {

        System.out.print("Enter title or artist keyword: ");
        String keyword = input.nextLine().trim();

        List<Song> allSongs = songDAO.getAll();

        List<Song> result =
                searchService.searchSongs(allSongs, keyword);

        System.out.println("\n===== SEARCH RESULTS =====");
        printSongs(result);
    }

    private static void sortByArtistAndTitle() throws SQLException {

        List<Song> songs =
                searchService.sortByArtistAndTitle(songDAO.getAll());

        System.out.println("\n===== SORTED BY ARTIST AND TITLE =====");
        printSongs(songs);
    }

    private static void sortByDuration() throws SQLException {

        List<Song> songs =
                searchService.sortByDurationAndTitle(songDAO.getAll());

        System.out.println("\n===== SORTED BY DURATION =====");
        printSongs(songs);
    }

    private static void filterByDuration() throws SQLException {

        int min = readInt("Minimum duration in seconds: ");
        int max = readInt("Maximum duration in seconds: ");

        if (min < 0 || max < min) {
            System.out.println("Invalid duration range.");
            return;
        }

        List<Song> songs = searchService.filterByDuration(
                songDAO.getAll(), min, max
        );

        System.out.println("\n===== DURATION FILTER RESULTS =====");
        printSongs(songs);
    }

    private static void savePlaylist() throws IOException {

        if (playlist.getSongs().isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }

        System.out.print("Enter file name (example: myplaylist.txt): ");
        String fileName = input.nextLine().trim();

        if (fileName.isEmpty()) {
            System.out.println("File name cannot be empty.");
            return;
        }

        fileManager.savePlaylist(playlist.getSongs(), fileName);

        System.out.println("Playlist saved successfully.");
    }

    private static void loadPlaylist() throws IOException, SQLException {

        System.out.print("Enter file name to load: ");
        String fileName = input.nextLine().trim();

        if (fileName.isEmpty()) {
            System.out.println("File name cannot be empty.");
            return;
        }

        List<Song> loadedSongs = fileManager.loadPlaylist(fileName);

        if (loadedSongs.isEmpty()) {
            System.out.println("No valid songs found in file.");
            return;
        }

        List<Song> databaseSongs = songDAO.getAll();
        List<Song> validSongs = new ArrayList<>();

        for (Song loadedSong : loadedSongs) {

            boolean exists = false;

            for (Song databaseSong : databaseSongs) {

                if (loadedSong.getId() == databaseSong.getId()) {
                    validSongs.add(databaseSong);
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                System.out.println("Skipped deleted song: " + loadedSong.getTitle());
            }
        }

        if (validSongs.isEmpty()) {
            System.out.println("No existing songs found in the database.");
            return;
        }

        undoService.saveState(playlist);
        playlist.restoreSongs(validSongs);

        System.out.println("Playlist loaded successfully.");
        printSongs(playlist.getSongs());
    }

    private static void showMostPlayed() throws SQLException {

        List<String> songs = historyDAO.getMostPlayed();

        System.out.println("\n===== MOST PLAYED SONGS =====");

        if (songs.isEmpty()) {
            System.out.println("No listening data available.");
        } else {
            for (String song : songs) {
                System.out.println(song);
            }
        }
    }

    private static void showTotalListeningTime() throws SQLException {

        long totalSeconds = historyDAO.getTotalListeningTime();

        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        System.out.println("\n===== TOTAL LISTENING TIME =====");
        System.out.println(hours + " hours, "
                + minutes + " minutes, " + seconds + " seconds");
    }

    private static void addMultipleSongs() throws SQLException {

        int count = readInt("How many songs do you want to add? ");

        if (count <= 0) {
            System.out.println("Enter a valid number.");
            return;
        }

        List<Song> songs = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            System.out.println("\nSong " + (i + 1));

            System.out.print("Title: ");
            String title = input.nextLine().trim();

            System.out.print("Artist: ");
            String artist = input.nextLine().trim();

            int duration = readInt("Duration in seconds: ");

            System.out.print("Genre: ");
            String genre = input.nextLine().trim();

            if (title.isEmpty() || artist.isEmpty()
                    || genre.isEmpty() || duration <= 0) {

                System.out.println("Invalid song details. Bulk add cancelled.");
                return;
            }

            songs.add(new Song(title, artist, duration, genre));
        }

        if (songDAO.addMultiple(songs)) {
            System.out.println("All songs added successfully.");
        } else {
            System.out.println("Songs could not be added.");
        }
    }

    private static void printSongs(List<Song> songs) {

        if (songs.isEmpty()) {
            System.out.println("No songs found.");
        } else {
            for (Song song : songs) {
                System.out.println(song);
            }
        }
    }

    private static int readInt(String prompt) {

        while (true) {

            System.out.print(prompt);

            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }
}
