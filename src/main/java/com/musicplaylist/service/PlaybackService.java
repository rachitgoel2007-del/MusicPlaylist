package com.musicplaylist.service;

import com.musicplaylist.model.Playlist;
import com.musicplaylist.model.Song;

public class PlaybackService {
    private volatile boolean paused;
    private volatile boolean stopped;
    public void play(Song song) {
        if (song == null) { System.out.println("Playlist is empty."); return; }
        paused = false; stopped = false;
        Thread worker = new Thread(() -> {
            System.out.println("Now playing: " + song.getTitle() + " - " + song.getArtist());
            for (int second = 1; second <= song.getDurationSeconds() && !stopped; second++) {
                while (paused && !stopped) {
                    try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                }
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
            }
            if (!stopped) System.out.println("Finished: " + song.getTitle());
        }, "music-playback");
        worker.setDaemon(true);
        worker.start();
    }
    public void pause() { paused = true; System.out.println("Playback paused (if a song is playing)."); }
    public void resume() { paused = false; System.out.println("Playback resumed (if a song is playing)."); }
    public void stop() { stopped = true; paused = false; System.out.println("Playback stopped."); }
}
