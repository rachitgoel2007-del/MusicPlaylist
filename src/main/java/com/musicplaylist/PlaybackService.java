
package com.musicplaylist;

public class PlaybackService {

    private Thread playbackThread;
    private volatile boolean paused = false;
    private volatile boolean stopped = false;

    public synchronized void play(Song song) {

        if (playbackThread != null && playbackThread.isAlive()) {
            stop();
        }

        paused = false;
        stopped = false;

        playbackThread = new Thread(() -> {

            System.out.println("\nNow playing: " + song.getTitle()
                    + " by " + song.getArtist());

            int duration = Math.max(5, Math.min(song.getDuration(), 30));

            for (int second = 1; second <= duration; second++) {

                synchronized (this) {

                    while (paused && !stopped) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    if (stopped) {
                        return;
                    }
                }

                System.out.println("Playing... " + second + " sec");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    if (stopped) {
                        return;
                    }
                }
            }

            System.out.println("Playback finished.");

        });

        playbackThread.setName("Music-Playback-Thread");
        playbackThread.start();
    }

    public synchronized void pause() {
        if (playbackThread != null && playbackThread.isAlive()) {
            paused = true;
            System.out.println("Playback paused.");
        }
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
        System.out.println("Playback resumed.");
    }

    public synchronized void stop() {
        if (playbackThread != null && playbackThread.isAlive()) {
            stopped = true;
            paused = false;
            notifyAll();
            playbackThread.interrupt();
            System.out.println("Playback stopped.");
        }
    }
}
