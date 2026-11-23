
package com.musicplaylist.model;

public class Song {
    private int id;
    private String title;
    private String artist;
    private int durationSeconds;
    private String genre;

    public Song(int id, String title, String artist, int durationSeconds, String genre) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationSeconds = durationSeconds;
        this.genre = genre;
    }

    public Song(String title, String artist, int durationSeconds, String genre) {
        this(0, title, artist, durationSeconds, genre);
    }

    public Song(int id, String title, String artist, int durationSeconds) {
        this(id, title, artist, durationSeconds, "Unknown");
    }

    public Song(String title, String artist, int durationSeconds) {
        this(0, title, artist, durationSeconds, "Unknown");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return id + ". " + title + " - " + artist
                + " (" + durationSeconds + " sec) | Genre: " + genre;
    }
}
