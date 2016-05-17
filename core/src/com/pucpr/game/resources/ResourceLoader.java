/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luis
 */
public class ResourceLoader {

    private int percent;
    private final Map<String, List<String>> resources = new HashMap();
    private Handler handler;
    private final State current = new State();
    private final State groupState = new State();
    private final State total = new State();
    private boolean loading = false;
    private boolean loaded = false;

    final Map<String, FileHandle> loadedResources = new HashMap();
    final Map<String, Sound> loadedAudios = new HashMap();
    final Map<String, Texture> loadedTextures = new HashMap();

    public ResourceLoader() {
        resources.put("skin", new ArrayList());
        resources.get("skin").add("data/uiskin.json");

        resources.put("sprites", new ArrayList<String>());

        // Player sprites
//        resources.get("sprites").add("data/images/sprites/RPG Fantasy/simple.png");
//        resources.get("sprites").add("data/images/sprites/tales of phantasia/ArcheKlaineOW.png");
        resources.get("sprites").add("data/images/sprites/tales of phantasia/ClessAlveinOW-otm.png");
        resources.get("sprites").add("data/images/sprites/tales of phantasia/Weapons.png");
//        resources.get("sprites").add("data/images/sprites/tales of phantasia/ChesterBarklightOW.png");
//        resources.get("sprites").add("data/images/sprites/tales of phantasia/SoE_Rat.png");
//        resources.get("sprites").add("data/images/sprites/tales of phantasia/SOItems.png");
        resources.get("sprites").add("data/images/sprites/tales of phantasia/SO_VelcantBoss-otm.png");

        //Audios 
        resources.put("audio", new ArrayList());
        resources.get("audio").add("data/audio/sfx/running/house.mp3");
        resources.get("audio").add("data/audio/sfx/running/gravel.mp3");
        resources.get("audio").add("data/audio/sfx/running/castle.mp3");
        resources.get("audio").add("data/audio/sfx/walking/house.mp3");
        resources.get("audio").add("data/audio/sfx/walking/gravel.mp3");
        resources.get("audio").add("data/audio/sfx/walking/castle.mp3");
    }

    public void load() {
        Thread t = new Thread() {
            @Override
            public void run() {
                loading = true;
                total.quantity = 0;

                for (String k : resources.keySet()) {

                    groupState.quantity = 0;

                    final List<String> l = resources.get(k);

                    update(percent, k, l.size(), groupState.quantity);

                    for (final String s : l) {
                        final FileHandle file = Gdx.files.internal(s);
                        loadedResources.put(s, file);

                        if (k.equals("audio")) {

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    loadedAudios.put(s, Gdx.audio.newSound(file));
                                }
                            });
                        } else if (k.equals("sprites")) {

                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    loadedTextures.put(s, new Texture(file));
                                }
                            });
                        }
                        current.quantity++;
                        total.quantity++;
                    }

                    groupState.quantity++;
                    int lPercent = ((Float) ((current.quantity.floatValue() / total.quantity.floatValue()) * 100)).intValue();

                    if (lPercent != percent) {
                        update(lPercent, k, l.size(), groupState.quantity);
                        percent = lPercent;
                    }
                }

            }

        };

        int lPercent = total.quantity == 0 ? 0 : ((Float) ((current.quantity.floatValue() / total.quantity.floatValue()) * 100)).intValue();
        update(lPercent, "Starting", 0, 0);

        loading = false;
        loaded = true;

        t.start();
    }

    private void update(final int percent, final String k, final int groupQty, final int groupState) {
        if (handler != null) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    handler.update(percent, k, groupQty, groupState);
                }
            });
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public static interface Handler {

        void update(int percent, String group, int groupQty, int groupState);
    }

    private static class State {

        Integer quantity = 0;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public Map<String, FileHandle> getLoadedResources() {
        return loadedResources;
    }

    public Sound getSound(String key) {
        return loadedAudios.get(key);
    }

    public Object getResource(String key) {
        return getLoadedResources().get(key);
    }

    public Texture getTexture(String key) {
        return loadedTextures.get(key);
    }
}
