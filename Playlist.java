// Playlist.java
// Manages the list of songs using ArrayList with File Persistence
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Playlist {
    private ArrayList<Song> songs;
    private final String FILE_NAME = "songs.txt";

    // Constructor
    public Playlist() {
        songs = new ArrayList<Song>();
    }

    // Check if ID already exists
    public boolean isDuplicateId(int id) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getId() == id) {
                return true;
            }
        }
        return false;
    }

    // 1. Add Song
    public void addSong(Song song) {
        if (isDuplicateId(song.getId())) {
            System.out.println("Error: Song with ID " + song.getId() + " already exists!");
            return;
        }
        songs.add(song);
        System.out.println("Song added successfully!");
    }

    // 2. Remove Song
    public boolean removeSong(int id) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getId() == id) {
                System.out.println("Removed: " + songs.get(i).getName());
                songs.remove(i);
                return true;
            }
        }
        System.out.println("Song with ID " + id + " not found!");
        return false;
    }

    // 3. Display Playlist
    public void displayPlaylist() {
        if (songs.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }
        System.out.println("\n--- Current Playlist ---");
        for (int i = 0; i < songs.size(); i++) {
            System.out.print((i + 1) + ". ");
            songs.get(i).displaySong();
        }
    }

    // 4. Search Song (by Name or Artist)
    public void searchSong(String search) {
        if (songs.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }

        boolean found = false;
        search = search.toLowerCase();

        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            if (s.getName().toLowerCase().contains(search) || s.getArtist().toLowerCase().contains(search)) {
                s.displaySong();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching songs found.");
        }
    }

    // 5. Shuffle Playlist
    public void shufflePlaylist() {
        if (songs.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }
        Collections.shuffle(songs);
        System.out.println("Playlist shuffled successfully!");
    }

    // 6. Sort by Name alphabetically (Bubble Sort)
    public void sortByName() {
        for (int i = 0; i < songs.size() - 1; i++) {
            for (int j = 0; j < songs.size() - i - 1; j++) {
                if (songs.get(j).getName().compareToIgnoreCase(songs.get(j + 1).getName()) > 0) {
                    Song temp = songs.get(j);
                    songs.set(j, songs.get(j + 1));
                    songs.set(j + 1, temp);
                }
            }
        }
        System.out.println("Playlist sorted by song name.");
    }

    // 7. Sort by Artist alphabetically (Bubble Sort)
    public void sortByArtist() {
        for (int i = 0; i < songs.size() - 1; i++) {
            for (int j = 0; j < songs.size() - i - 1; j++) {
                if (songs.get(j).getArtist().compareToIgnoreCase(songs.get(j + 1).getArtist()) > 0) {
                    Song temp = songs.get(j);
                    songs.set(j, songs.get(j + 1));
                    songs.set(j + 1, temp);
                }
            }
        }
        System.out.println("Playlist sorted by artist name.");
    }

    // 8. Sort by Duration shortest to longest (Bubble Sort)
    public void sortByDuration() {
        for (int i = 0; i < songs.size() - 1; i++) {
            for (int j = 0; j < songs.size() - i - 1; j++) {
                if (songs.get(j).getDuration() > songs.get(j + 1).getDuration()) {
                    Song temp = songs.get(j);
                    songs.set(j, songs.get(j + 1));
                    songs.set(j + 1, temp);
                }
            }
        }
        System.out.println("Playlist sorted by duration.");
    }

    // 9. Save Playlist to Text File (File Persistence)
    public void saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));
            for (int i = 0; i < songs.size(); i++) {
                Song s = songs.get(i);
                writer.println(s.getId() + "," + s.getName() + "," + s.getArtist() + "," + s.getDuration());
            }
            writer.close();
            System.out.println("Playlist saved to " + FILE_NAME + " successfully!");
        } catch (Exception e) {
            System.out.println("Error saving playlist to file: " + e.getMessage());
        }
    }

    // 10. Load Playlist from Text File (File Persistence)
    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return; // No saved file yet
        }

        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String artist = parts[2].trim();
                        double duration = Double.parseDouble(parts[3].trim());
                        songs.add(new Song(id, name, artist, duration));
                    }
                }
            }
            fileScanner.close();
            System.out.println("Loaded " + songs.size() + " songs from " + FILE_NAME + ".");
        } catch (Exception e) {
            System.out.println("Error loading songs from file: " + e.getMessage());
        }
    }

    // Helper methods
    public int getSize() {
        return songs.size();
    }

    public boolean isEmpty() {
        return songs.isEmpty();
    }

    public Song getSong(int index) {
        if (index >= 0 && index < songs.size()) {
            return songs.get(index);
        }
        return null;
    }
}
