
package com.musicplaylist.service;

import com.musicplaylist.model.Song;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class PlaybackService {

    private final Queue<Song> queue = new LinkedList<>();
    private final Stack<Song> history = new Stack<>();

    private Song currentSong;
    private Thread playbackThread;

    private volatile boolean paused = false;
    private volatile boolean stopped = true;

    private String playbackState = "Stopped";

    public synchronized void addToQueue(Song song) {
        if (song != null) {
            queue.add(song);
            System.out.println("Added to playback queue: " + song.getTitle());
        }
    }

    public synchronized void play(Song song) {
        if (song == null) {
            System.out.println("No song selected.");
            return;
        }

        stopCurrentThread();

        currentSong = song;
        paused = false;
        stopped = false;
        playbackState = "Playing";

        startPlaybackThread(song);
    }

    public synchronized boolean playNext() {

        Song nextSong = queue.poll();

        if (nextSong == null) {
            stop();
            System.out.println("Playback queue is empty.");
            return false;
        }

        if (currentSong != null) {
            history.push(currentSong);
        }

        play(nextSong);
        return true;
    }

    public synchronized boolean playPrevious() {

        if (history.isEmpty()) {
            System.out.println("No previous song available.");
            return false;
        }

        Song previousSong = history.pop();

        if (currentSong != null) {
            queue.add(currentSong);
        }

        play(previousSong);
        return true;
    }

    private void startPlaybackThread(Song song) {

        playbackThread = new Thread(() -> {

            System.out.println("Now playing: "
                    + song.getTitle() + " - " + song.getArtist());

            for (int second = 1;
                 second <= song.getDurationSeconds() && !stopped;
                 second++) {

                while (paused && !stopped) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (stopped) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            synchronized (this) {
                if (!stopped && currentSong == song) {
                    playbackState = "Stopped";
                    stopped = true;
                    System.out.println("Finished: " + song.getTitle());
                }
            }

        }, "music-playback");

        playbackThread.setDaemon(true);
        playbackThread.start();
    }

    public synchronized void pause() {
        if (!stopped && !paused) {
            paused = true;
            playbackState = "Paused";
            System.out.println("Playback paused.");
        } else {
            System.out.println("No active playback to pause.");
        }
    }

    public synchronized void resume() {
        if (paused && !stopped) {
            paused = false;
            playbackState = "Playing";
            System.out.println("Playback resumed.");
        } else {
            System.out.println("Playback is not paused.");
        }
    }

    public synchronized void stop() {
        stopCurrentThread();
        currentSong = null;
        paused = false;
        stopped = true;
        playbackState = "Stopped";

        System.out.println("Playback stopped.");
    }

    private void stopCurrentThread() {
        stopped = true;
        paused = false;

        if (playbackThread != null && playbackThread.isAlive()) {
            playbackThread.interrupt();
        }
    }

    public synchronized String getPlaybackState() {
        return playbackState;
    }

    public synchronized Song getCurrentSong() {
        return currentSong;
    }

    public synchronized void removeSong(int id) {

        queue.removeIf(song -> song.getId() == id);
        history.removeIf(song -> song.getId() == id);

        if (currentSong != null && currentSong.getId() == id) {
            stop();
            System.out.println("Deleted song was playing, playback stopped.");
        }

        System.out.println("Deleted song removed from playback queue and history.");
    }

    public synchronized void showQueue() {
        System.out.println("\n===== PLAYBACK QUEUE =====");

        if (queue.isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }

        for (Song song : queue) {
            System.out.println(song);
        }
    }
}
