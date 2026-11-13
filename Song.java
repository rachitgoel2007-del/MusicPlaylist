// Song.java
// Represents a single song in the playlist
public class Song {
    private int id;
    private String name;
    private String artist;
    private double duration;

    // Constructor to initialize song details
    public Song(int id, String name, String artist, double duration) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public double getDuration() {
        return duration;
    }

    // Method to print song details
    public void displaySong() {
        System.out.println("ID: " + id + " | " + name + " | " + artist + " | " + duration + " mins");
    }
}
