
package com.musicplaylist.service;

import com.musicplaylist.model.Playlist;
import com.musicplaylist.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PlaylistUndoService {

    private Stack<List<Song>> previousPlaylists = new Stack<>();

    // Store current playlist before making a change
    public void saveState(Playlist playlist) {
        List<Song> songs = new ArrayList<>(playlist.getSongs());
        previousPlaylists.push(songs);
    }

    // Restore previous playlist
    public boolean undo(Playlist playlist) {

        if (previousPlaylists.isEmpty()) {
            return false;
        }

        List<Song> oldSongs = previousPlaylists.pop();
        playlist.restoreSongs(oldSongs);

        return true;
    }

    // Remove deleted songs from all saved undo states
    public void removeSong(int songId) {

        for (List<Song> songs : previousPlaylists) {
            songs.removeIf(song -> song.getId() == songId);
        }
    }
}
