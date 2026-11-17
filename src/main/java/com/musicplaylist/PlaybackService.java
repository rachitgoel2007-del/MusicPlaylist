package com.musicplaylist;

public class PlaybackService {

    private Thread playbackThread;
    private boolean playing = false;

    public void playSong(String songName) {

        if (playing) {
            System.out.println("A song is already playing!");
            return;
        }

        playing = true;

        playbackThread = new Thread(new Runnable() {
            public void run() {
                System.out.println("Now Playing: " + songName);

                for (int i = 1; i <= 10; i++) {

                    if (!playing) {
                        System.out.println("Playback stopped.");
                        return;
                    }

                    System.out.println("Playing... " + i + " sec");

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Playback interrupted.");
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                playing = false;
                System.out.println("Song finished!");
            }
        });

        playbackThread.start();
    }

    public void stopSong() {
        playing = false;

        if (playbackThread != null) {
            playbackThread.interrupt();
        }
    }

    public boolean isPlaying() {
        return playing;
    }
}