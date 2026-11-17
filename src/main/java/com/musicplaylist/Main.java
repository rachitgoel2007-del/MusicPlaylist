package com.musicplaylist;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SongDAO songDAO = new SongDAO();
        HistoryDAO historyDAO = new HistoryDAO();
        PlaybackService player = new PlaybackService();

        try {
            DatabaseSetup.initialize();
            System.out.println("Connected to MySQL successfully!");
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.out.println("Check MySQL is running and update src/main/resources/db.properties.");
            return;
        }

        while (true) {
            System.out.println("\n===== MUSIC PLAYLIST =====");
            System.out.println("1. Add song");
            System.out.println("2. View all songs");
            System.out.println("3. Search song");
            System.out.println("4. Delete song");
            System.out.println("5. Play song");
            System.out.println("6. Pause playback");
            System.out.println("7. Resume playback");
            System.out.println("8. Stop playback");
            System.out.println("9. View listening history");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 0) {
                    player.stop();
                    System.out.println("Goodbye!");
                    break;
                }

                switch (choice) {
                    case 1:
                        System.out.print("Song title: ");
                        String title = sc.nextLine();
                        System.out.print("Artist: ");
                        String artist = sc.nextLine();
                        System.out.print("Duration in seconds: ");
                        int duration = Integer.parseInt(sc.nextLine());
                        if (duration <= 0) {
                            System.out.println("Duration must be greater than zero.");
                            break;
                        }
                        songDAO.addSong(new Song(title, artist, duration));
                        System.out.println("Song added successfully.");
                        break;

                    case 2:
                        printSongs(songDAO.getAllSongs());
                        break;

                    case 3:
                        System.out.print("Enter title or artist to search: ");
                        printSongs(songDAO.searchSongs(sc.nextLine()));
                        break;

                    case 4:
                        System.out.print("Enter song ID to delete: ");
                        int deleteId = Integer.parseInt(sc.nextLine());
                        System.out.println(songDAO.deleteSong(deleteId)
                                ? "Song deleted." : "Song ID not found.");
                        break;

                    case 5:
                        System.out.print("Enter song ID to play: ");
                        int playId = Integer.parseInt(sc.nextLine());
                        Song song = songDAO.getSongById(playId);
                        if (song == null) {
                            System.out.println("Song ID not found.");
                        } else {
                            historyDAO.addHistory(song);
                            player.play(song);
                        }
                        break;

                    case 6:
                        player.pause();
                        break;
                    case 7:
                        player.resume();
                        break;
                    case 8:
                        player.stop();
                        break;
                    case 9:
                        historyDAO.showHistory();
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
        sc.close();
    }

    private static void printSongs(List<Song> songs) {
        if (songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }
        System.out.println("\nID | TITLE | ARTIST | DURATION");
        for (Song song : songs) {
            System.out.println(song);
        }
    }
}
