/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.pucpr.game.AppManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public class SoundManager {

    private AppManager manager;
    private boolean walking;
    private boolean running;

    private String musicName;

    private boolean playMusic = false;
    private boolean playFX = true;
    private boolean enableAudio = true;

    private String ambient = "house";

    private Music music;

    private Sound runningWalkingSound;
    private Sound getItemSound;
    private Sound background;

    public SoundManager(AppManager manager) {
        this.manager = manager;
    }

    public boolean isWalking() {
        return walking;
    }

    public void setWalking(boolean walking) {
        checkMoveAudioChanged(walking, walking ? false : running);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        checkMoveAudioChanged(running ? false : walking, running);
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        setMusicName(musicName, true);
    }

    public void setMusicName(final String musicName, final boolean looping) {

        if (this.musicName != null && this.musicName.equals(musicName)) {
            // Trying to set same music, maybe we need to start a new sound.
            return;
        }

        this.musicName = musicName;

        final Thread t = new Thread("SoundManager-ChangeMusic") {
            @Override
            public void run() {
                stopMusic();
                music = manager.getResourceLoader().getMusic("data/audio/music/" + musicName + ".mp3");
                music.setLooping(looping);
                startMusic();
            }
        };

        t.start();
    }

    public boolean isPlayMusic() {
        return playMusic;
    }

    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
        if (!playMusic) {
            stopMusic();
        } else {
            startMusic();
        }
    }

    public synchronized void stopMusic() {
        if (music != null && music.isPlaying()) {

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    music.setVolume(music.getVolume() - 0.1f);
                }
            });

            do {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        music.setVolume(music.getVolume() - 0.1f);
                    }
                });

            } while (music.getVolume() > 0.1f);

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    music.setVolume(0);
                }
            });

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    music.stop();
                    music.dispose();
                }
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }

        }
    }

    public void startMusic() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (music != null && !music.isPlaying()) {
                    music.play();
                }
            }
        });
    }

    public boolean isPlayFX() {
        return playFX;
    }

    public void setPlayFX(boolean playFX) {
        this.playFX = playFX;
    }

    public boolean isEnableAudio() {
        return enableAudio;
    }

    public void setEnableAudio(boolean enableAudio) {
        this.enableAudio = enableAudio;
    }

    private void checkMoveAudioChanged(boolean walking, boolean running) {
        boolean changed = false;

        if (this.running != running) {
            changed = true;
            this.running = running;
        }

        if (this.walking != walking) {
            changed = true;
            this.walking = walking;
        }

        if (changed) {
            moveAudioChanged();
        }
    }

    private void moveAudioChanged() {

        if (runningWalkingSound != null) {
            runningWalkingSound.stop();
        }

        if (isPlayFX() && (isRunning() || isWalking())) {
            String song = null;
            try {

                if (isRunning()) {
                    song = "running/";
                } else if (isWalking()) {
                    song = "walking/";
                }

                runningWalkingSound = manager.getResourceLoader().getSound("data/audio/sfx/" + song + ambient + ".mp3");
                runningWalkingSound.loop();
            } catch (Exception ex) {
                throw new IllegalStateException("Failed to load ambient data/audio/sfx/" + song + ambient + ", walking/running sound not found!", ex);
            }
        }
    }

    public void playLevelSound(int level) {
        setMusicName("background-fase-" + level, true);
    }

    public void playGetItemSound() {

        if (getItemSound != null) {
            getItemSound.stop();
            getItemSound.play();
        } else {
            try {
                getItemSound = manager.getResourceLoader().getSound("data/audio/sfx/resources/get-item.mp3");
                getItemSound.play();
            } catch (Exception ex) {
                throw new IllegalStateException("Failed to load sound data/audio/sfx/resources/get-item.mp3!", ex);
            }
        }
    }
}
