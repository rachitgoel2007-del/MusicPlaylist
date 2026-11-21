
package com.musicplaylist.service;

import com.musicplaylist.model.Song;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistFileManager {

    // Save playlist songs in a text file
    public void savePlaylist(List<Song> songs, String fileName)
            throws IOException {

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(fileName))) {

            for (Song song : songs) {
                writer.write(song.getId() + "\t"
                        + song.getTitle() + "\t"
                        + song.getArtist() + "\t"
                        + song.getDurationSeconds());

                writer.newLine();
            }
        }
    }

    // Load songs from the saved file
    public List<Song> loadPlaylist(String fileName)
            throws IOException {

        List<Song> songs = new ArrayList<>();

        try (BufferedReader reader =
                new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\t", 4);

                if (data.length == 4) {
                    try {
                        int id = Integer.parseInt(data[0]);
                        String title = data[1];
                        String artist = data[2];
                        int duration = Integer.parseInt(data[3]);

                        Song song = new Song(
                                id, title, artist, duration);

                        songs.add(song);

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid song data skipped.");
                    }
                }
            }
        }

        return songs;
    }
}
