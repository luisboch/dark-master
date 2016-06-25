/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.locations;

import com.pucpr.game.GameConfig;
import com.pucpr.game.Keys;
import com.pucpr.game.PlayerStatus;
import com.pucpr.game.states.game.basic.BasicGameScreen;

/**
 *
 * @author Luis Carlos
 */
public class RushTest extends BasicGameScreen {

    private long started;
    private boolean timedOut = false;

    public RushTest() {
        super("rushtestLevel.tmx");
    }

    @Override
    public void create() {
        super.create();
        if (!PlayerStatus.isKey(Keys.RUSH_TEST_DONE)) {
            gameState.getScreenInfo().showTimeOut(30000l);
        }

        GameConfig.SOUND_MANAGER.playLevelSound(2, GameConfig.defaultMusicVolume);
    }

    @Override
    public void render() {
        super.render();

        if (!PlayerStatus.isKey(Keys.RUSH_TEST_DONE) && gameState.getScreenInfo().isTimeOver()) {
            final TutorialScreen prevScreen = new TutorialScreen();
            gameState.setScreen(prevScreen, TutorialScreen.GATE_2);
        }

    }

    @Override
    public void close() {
        super.close();
        if (PlayerStatus.isKey(Keys.RUSH_TEST_ISCOMPLETE) && !PlayerStatus.isKey(Keys.RUSH_TEST_DONE)) {
            gameState.getScreenInfo().showImage("data/images/sprites/util/ok.png", 6000);
            PlayerStatus.getInstance().set(Keys.RUSH_TEST_DONE, true);
        } else {

        }
    }

}
