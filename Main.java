// Main.java
// Menu-driven program for Music Playlist Engine
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Playlist playlist = new Playlist();
        ArrayList<Song> history = new ArrayList<Song>();

        // Load previously saved songs from file on startup (File Persistence)
        playlist.loadFromFile();

        // Tracks current playing song index
        int currentIndex = -1;
        if (!playlist.isEmpty()) {
            currentIndex = 0;
        }

        while (true) {
            System.out.println("\n========== MUSIC PLAYLIST ENGINE ==========");
            System.out.println("1. Add Song");
            System.out.println("2. Remove Song");
            System.out.println("3. Display Playlist");
            System.out.println("4. Play Current Song");
            System.out.println("5. Next Song");
            System.out.println("6. Previous Song");
            System.out.println("7. Shuffle Playlist");
            System.out.println("8. Recently Played History");
            System.out.println("9. Search Song");
            System.out.println("10. Sort Playlist");
            System.out.println("11. Save Playlist to File");
            System.out.println("12. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer after reading integer

            switch (choice) {
                case 1:
                    // Add Song
                    System.out.print("Enter Song ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // clear buffer

                    System.out.print("Enter Song Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Artist Name: ");
                    String artist = scanner.nextLine();

                    System.out.print("Enter Duration (in mins, e.g. 3.5): ");
                    double duration = scanner.nextDouble();
                    scanner.nextLine(); // clear buffer

                    Song newSong = new Song(id, name, artist, duration);
                    playlist.addSong(newSong);

                    // If it is the first song, point to it
                    if (currentIndex == -1) {
                        currentIndex = 0;
                    }
                    break;

                case 2:
                    // Remove Song
                    if (playlist.isEmpty()) {
                        System.out.println("Playlist is empty.");
                        break;
                    }

                    System.out.print("Enter Song ID to remove: ");
                    int removeId = scanner.nextInt();
                    scanner.nextLine(); // clear buffer

                    boolean removed = playlist.removeSong(removeId);

                    // Adjust pointer after removing
                    if (removed) {
                        if (playlist.isEmpty()) {
                            currentIndex = -1;
                        } else if (currentIndex >= playlist.getSize()) {
                            currentIndex = playlist.getSize() - 1;
                        }
                    }
                    break;

                case 3:
                    // Display Playlist
                    playlist.displayPlaylist();
                    break;

                case 4:
                    // Play Current Song
                    if (playlist.isEmpty()) {
                        System.out.println("Playlist is empty. Add songs first!");
                    } else {
                        if (currentIndex == -1) {
                            currentIndex = 0;
                        }
                        playSong(playlist.getSong(currentIndex), history);
                    }
                    break;

                case 5:
                    // Next Song
                    if (playlist.isEmpty()) {
                        System.out.println("Playlist is empty.");
                    } else if (currentIndex + 1 < playlist.getSize()) {
                        currentIndex++;
                        playSong(playlist.getSong(currentIndex), history);
                    } else {
                        System.out.println("You are at the end of the playlist. No next song.");
                    }
                    break;

                case 6:
                    // Previous Song
                    if (playlist.isEmpty()) {
                        System.out.println("Playlist is empty.");
                    } else if (currentIndex > 0) {
                        currentIndex--;
                        playSong(playlist.getSong(currentIndex), history);
                    } else {
                        System.out.println("You are at the beginning of the playlist. No previous song.");
                    }
                    break;

                case 7:
                    // Shuffle Playlist
                    if (playlist.isEmpty()) {
                        System.out.println("Playlist is empty.");
                    } else {
                        playlist.shufflePlaylist();
                        currentIndex = 0; // reset pointer to first song after shuffle
                    }
                    break;

                case 8:
                    // Recently Played History (last 10 songs)
                    if (history.isEmpty()) {
                        System.out.println("No recently played songs.");
                    } else {
                        System.out.println("\n--- Recently Played (Last " + history.size() + ") ---");
                        for (int i = 0; i < history.size(); i++) {
                            System.out.print((i + 1) + ". ");
                            history.get(i).displaySong();
                        }
                    }
                    break;

                case 9:
                    // Search Song
                    if (playlist.isEmpty()) {
                        System.out.println("Playlist is empty.");
                    } else {
                        System.out.print("Enter song name or artist: ");
                        String query = scanner.nextLine();
                        playlist.searchSong(query);
                    }
                    break;

                case 10:
                    // Sort Playlist
                    if (playlist.isEmpty()) {
                        System.out.println("Playlist is empty.");
                        break;
                    }

                    System.out.println("\n--- Sort Playlist ---");
                    System.out.println("1. By Song Name");
                    System.out.println("2. By Artist Name");
                    System.out.println("3. By Duration");
                    System.out.print("Enter choice: ");
                    int sortChoice = scanner.nextInt();
                    scanner.nextLine(); // clear buffer

                    if (sortChoice == 1) {
                        playlist.sortByName();
                        currentIndex = 0;
                        playlist.displayPlaylist();
                    } else if (sortChoice == 2) {
                        playlist.sortByArtist();
                        currentIndex = 0;
                        playlist.displayPlaylist();
                    } else if (sortChoice == 3) {
                        playlist.sortByDuration();
                        currentIndex = 0;
                        playlist.displayPlaylist();
                    } else {
                        System.out.println("Invalid sort choice.");
                    }
                    break;

                case 11:
                    // Save to File manually
                    playlist.saveToFile();
                    break;

                case 12:
                    // Exit (auto-saves before exiting)
                    playlist.saveToFile();
                    System.out.println("Thank you for using Music Playlist Engine. Bye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please choose from 1 to 12.");
            }
        }
    }

    // Helper method to play song and store in history
    public static void playSong(Song song, ArrayList<Song> history) {
        System.out.print("Now Playing: ");
        song.displaySong();

        // Add to history list
        history.add(song);

        // Keep maximum 10 songs in history
        if (history.size() > 10) {
            history.remove(0); // remove oldest song
        }
    }
}
