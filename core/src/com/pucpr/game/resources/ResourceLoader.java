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
<<<<<<< HEAD:core/src/com/pucpr/game/resources/ResourceLoader.java
<<<<<<< HEAD:core/src/com/pucpr/game/resources/ResourceLoader.java
    final Map<String, Texture> loadedTextures = new HashMap();
=======
>>>>>>> 757b79b... Improved class structure and fixed some movimentation bugs...:core/src/com/mygdx/game/resources/ResourceLoader.java
=======
    final Map<String, Texture> loadedTextures = new HashMap();
>>>>>>> 89e476d... added animmations to player.:core/src/com/mygdx/game/resources/ResourceLoader.java

    public ResourceLoader() {
        resources.put("skin", new ArrayList());
        resources.get("skin").add("data/uiskin.json");
<<<<<<< HEAD
<<<<<<< HEAD:core/src/com/pucpr/game/resources/ResourceLoader.java

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
=======
        
=======

>>>>>>> c19b50f... basic conversation structure finished.
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
<<<<<<< HEAD:core/src/com/pucpr/game/resources/ResourceLoader.java
        
>>>>>>> 757b79b... Improved class structure and fixed some movimentation bugs...:core/src/com/mygdx/game/resources/ResourceLoader.java
=======
>>>>>>> 89e476d... added animmations to player.:core/src/com/mygdx/game/resources/ResourceLoader.java
        resources.get("audio").add("data/audio/sfx/running/house.mp3");
        resources.get("audio").add("data/audio/sfx/running/gravel.mp3");
        resources.get("audio").add("data/audio/sfx/running/castle.mp3");
        resources.get("audio").add("data/audio/sfx/walking/house.mp3");
        resources.get("audio").add("data/audio/sfx/walking/gravel.mp3");
        resources.get("audio").add("data/audio/sfx/walking/castle.mp3");
    }

    public void load() {

        loading = true;
        total.quantity = 0;

        for (String key : resources.keySet()) {
            final List<String> val = resources.get(key);
            for (String s : val) {
                total.quantity++;
            }
        }

        for (String k : resources.keySet()) {
            groupState.quantity = 0;
            final List<String> l = resources.get(k);
<<<<<<< HEAD
<<<<<<< HEAD:core/src/com/pucpr/game/resources/ResourceLoader.java

            update(percent, k, l.size(), groupState.quantity);

=======
            
            update(percent, k, l.size(), groupState.quantity);
            
>>>>>>> 89e476d... added animmations to player.:core/src/com/mygdx/game/resources/ResourceLoader.java
            for (String s : l) {
                final FileHandle file = Gdx.files.internal(s);
                loadedResources.put(s, file);
<<<<<<< HEAD:core/src/com/pucpr/game/resources/ResourceLoader.java

                if (k.equals("audio")) {
                    loadedAudios.put(s, Gdx.audio.newSound(file));
                } else if (k.equals("sprites")) {
                    loadedTextures.put(s, new Texture(file));
                }

=======
                
                if(k.equals("audio")){
=======

            update(percent, k, l.size(), groupState.quantity);

            for (String s : l) {
                final FileHandle file = Gdx.files.internal(s);
                loadedResources.put(s, file);

                if (k.equals("audio")) {
>>>>>>> c19b50f... basic conversation structure finished.
                    loadedAudios.put(s, Gdx.audio.newSound(file));
                } else if (k.equals("sprites")) {
                    loadedTextures.put(s, new Texture(file));
                }
<<<<<<< HEAD
                
>>>>>>> 757b79b... Improved class structure and fixed some movimentation bugs...:core/src/com/mygdx/game/resources/ResourceLoader.java
=======

>>>>>>> c19b50f... basic conversation structure finished.
                current.quantity++;
                groupState.quantity++;
                int lPercent = ((Float) ((current.quantity.floatValue() / total.quantity.floatValue()) * 100)).intValue();

                if (lPercent != percent) {
                    update(lPercent, k, l.size(), groupState.quantity);
                    percent = lPercent;
                }
            }
        }

        int lPercent = total.quantity == 0 ? 100 : ((Float) ((current.quantity.floatValue() / total.quantity.floatValue()) * 100)).intValue();
        update(lPercent, "Starting", 0, 0);

        loading = false;
        loaded = true;

    }

    private void update(int percent, String k, int groupQty, int groupState) {
        if (handler != null) {
            handler.update(percent, k, groupQty, groupState);
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
<<<<<<< HEAD
<<<<<<< HEAD:core/src/com/pucpr/game/resources/ResourceLoader.java

    public Sound getSound(String key) {
        return loadedAudios.get(key);
    }

=======
    
    public Sound getSound(String key){
        return loadedAudios.get(key);
    }
    
>>>>>>> 757b79b... Improved class structure and fixed some movimentation bugs...:core/src/com/mygdx/game/resources/ResourceLoader.java
    public Object getResource(String key) {
        return getLoadedResources().get(key);
    }
<<<<<<< HEAD:core/src/com/pucpr/game/resources/ResourceLoader.java

    public Texture getTexture(String key) {
        return loadedTextures.get(key);
    }
=======
    
    public Texture getTexture(String key){
        return loadedTextures.get(key);
    }
    
    
>>>>>>> 89e476d... added animmations to player.:core/src/com/mygdx/game/resources/ResourceLoader.java
=======

    public Sound getSound(String key) {
        return loadedAudios.get(key);
    }

    public Object getResource(String key) {
        return getLoadedResources().get(key);
    }

    public Texture getTexture(String key) {
        return loadedTextures.get(key);
    }
>>>>>>> c19b50f... basic conversation structure finished.

}
