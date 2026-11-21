package com.musicplaylist.model;

public class Song {
    private int id;
    private String title;
    private String artist;
    private int durationSeconds;

    public Song(int id, String title, String artist, int durationSeconds) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationSeconds = durationSeconds;
    }

    public Song(String title, String artist, int durationSeconds) {
        this(0, title, artist, durationSeconds);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDurationSeconds() { return durationSeconds; }

    @Override
    public String toString() {
        return id + ". " + title + " - " + artist + " (" + durationSeconds + " sec)";
    }
}
