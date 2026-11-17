package com.musicplaylist;

public class Song {
    private int id;
    private String title;
    private String artist;
    private int duration;

    public Song(int id, String title, String artist, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    public Song(String title, String artist, int duration) {
        this(0, title, artist, duration);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDuration() { return duration; }

    @Override
    public String toString() {
        return id + " | " + title + " | " + artist + " | " + duration + " sec";
    }
}
