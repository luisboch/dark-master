/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.locations;

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
//        gameState.getScreenInfo().showTimeOut(15000);

    }

    @Override
    public void render() {
        super.render();

        if (gameState.getScreenInfo().isTimeOver()) {
            gameState.setScreen(new TutorialScreen());
        }

    }

}
