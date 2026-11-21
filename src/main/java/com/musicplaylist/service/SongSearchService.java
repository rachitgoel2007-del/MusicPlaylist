
package com.musicplaylist.service;

import com.musicplaylist.model.Song;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongSearchService {

    // Advanced partial search by title and artist
    public List<Song> searchSongs(List<Song> songs, String keyword) {
        List<Song> result = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }

        keyword = keyword.toLowerCase().trim();

        for (Song song : songs) {
            if (song.getTitle().toLowerCase().contains(keyword)
                    || song.getArtist().toLowerCase().contains(keyword)) {
                result.add(song);
            }
        }

        return result;
    }

    // Sort by artist first, then title
    public List<Song> sortByArtistAndTitle(List<Song> songs) {
        List<Song> result = new ArrayList<>(songs);

        Collections.sort(result, new Comparator<Song>() {
            public int compare(Song s1, Song s2) {
                int artistResult = s1.getArtist()
                        .compareToIgnoreCase(s2.getArtist());

                if (artistResult == 0) {
                    return s1.getTitle()
                            .compareToIgnoreCase(s2.getTitle());
                }

                return artistResult;
            }
        });

        return result;
    }

    // Sort by duration first, then title
    public List<Song> sortByDurationAndTitle(List<Song> songs) {
        List<Song> result = new ArrayList<>(songs);

        Collections.sort(result, new Comparator<Song>() {
            public int compare(Song s1, Song s2) {
                int durationResult = Integer.compare(
                        s1.getDurationSeconds(),
                        s2.getDurationSeconds()
                );

                if (durationResult == 0) {
                    return s1.getTitle()
                            .compareToIgnoreCase(s2.getTitle());
                }

                return durationResult;
            }
        });

        return result;
    }

    // Filter songs within a duration range
    public List<Song> filterByDuration(
            List<Song> songs, int minSeconds, int maxSeconds) {

        List<Song> result = new ArrayList<>();

        if (minSeconds < 0 || maxSeconds < minSeconds) {
            return result;
        }

        for (Song song : songs) {
            int duration = song.getDurationSeconds();

            if (duration >= minSeconds && duration <= maxSeconds) {
                result.add(song);
            }
        }

        return result;
    }
}

