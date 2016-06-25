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

    public void setMusicName(final String musicName, boolean looping) {

        this.musicName = musicName;

        final Thread t = new Thread("SoundManager-ChangeMusic") {
            @Override
            public void run() {
                stopMusic();
                music = Gdx.audio.newMusic(Gdx.files.internal("data/music/" + musicName + ".mp3"));
                music.setLooping(true);
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
            do {
                music.setVolume(music.getVolume() - 0.1f);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                }
            } while (music.getVolume() > 0f);
        }
    }

    public void startMusic() {
        if (music != null && !music.isPlaying()) {
            music.play();
        }
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
            movementAudioChanged();
        }
    }

    private void movementAudioChanged() {

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
                throw new IllegalStateException("Failed to load amibient data/audio/sfx/" + song + ambient + ", walking/running sound not found!", ex);
            }
        }
    }

    public void backgroundSound() {

        try {
            background = manager.getResourceLoader().getSound("data/audio/sfx/resources/background-fase1.mp3");
            background.loop();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load amibient data/audio/sfx/background-fase1, walking/running sound not found!", ex);
        }

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
                throw new IllegalStateException("Failed to load amibient data/audio/sfx/resources/get-item.mp3, walking/running sound not found!", ex);
            }
        }
    }
}
