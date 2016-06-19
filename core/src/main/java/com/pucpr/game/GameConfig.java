/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game;

import com.pucpr.game.resources.SoundManager;

/**
 *
 * @author luis
 */
public class GameConfig {

    /**
     * 1 metter on box 2d represents 32 pixels on game screen
     */
    public static final float PPM = 32;
    public static final float GAME_SCALE = 1f;
    public static SoundManager SOUND_MANAGER;
    public static final boolean showDebug = true;
    public static final int skipFramesQty = 60;
    public static final boolean skipFrames = false;
}
