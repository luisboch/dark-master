/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.basic;

import com.pucpr.game.PlayerStatus;
import com.pucpr.game.states.game.locations.TutorialScreen;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luis
 */
public class GameData {

    private final Map<String, Object> data = new HashMap<String, Object>();

    public GameData() {
        data.put("game.current.screen", TutorialScreen.class.getCanonicalName());
        data.put("game.player.status", PlayerStatus.getInstance());
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Integer getInt(String key) {
        return (Integer) data.get(key);
    }

    public String getString(String key) {
        return (String) data.get(key);
    }

    public Float getFloat(String key) {
        return (Float) data.get(key);
    }

    public void set(String key, Object val) {
        data.put(key, val);
    }

}
