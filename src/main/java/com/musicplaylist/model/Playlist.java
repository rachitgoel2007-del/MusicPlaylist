
package com.musicplaylist.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {

    private final String name;
    private final List<Song> songs = new ArrayList<>();
    private int currentIndex = -1;

    public Playlist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return Collections.unmodifiableList(songs);
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public boolean removeSong(int id) {

        for (int i = 0; i < songs.size(); i++) {

            if (songs.get(i).getId() == id) {

                songs.remove(i);

                if (songs.isEmpty()) {
                    currentIndex = -1;
                } else if (i < currentIndex) {
                    currentIndex--;
                } else if (currentIndex >= songs.size()) {
                    currentIndex = songs.size() - 1;
                }

                return true;
            }
        }

        return false;
    }

    public void restoreSongs(List<Song> oldSongs) {
        songs.clear();
        songs.addAll(oldSongs);
        currentIndex = -1;
    }

    public Song next() {

        if (songs.isEmpty()) {
            return null;
        }

        currentIndex = (currentIndex + 1) % songs.size();

        return songs.get(currentIndex);
    }

    public Song previous() {

        if (songs.isEmpty()) {
            return null;
        }

        currentIndex = (currentIndex - 1 + songs.size()) % songs.size();

        return songs.get(currentIndex);
    }

    public void shuffle() {
        Collections.shuffle(songs);
        currentIndex = -1;
    }

    public void sortByTitle() {
        songs.sort((a, b) ->
                a.getTitle().compareToIgnoreCase(b.getTitle()));
        currentIndex = -1;
    }

    public void sortByArtist() {
        songs.sort((a, b) ->
                a.getArtist().compareToIgnoreCase(b.getArtist()));
        currentIndex = -1;
    }
}
