/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.locations;

import com.pucpr.game.Keys;
import com.pucpr.game.PlayerStatus;
import com.pucpr.game.handlers.Action;
import com.pucpr.game.states.game.actors.B2Object;
import com.pucpr.game.states.game.actors.GateFirstLevel;
import com.pucpr.game.states.game.basic.BasicGameScreen;

/**
 *
 * @author luis
 */
public class TutorialScreen extends BasicGameScreen {

    public TutorialScreen() {
        super("firstlevel.tmx");
    }

    @Override
    protected void configure(final B2Object actor, boolean block) {
        if (actor instanceof GateFirstLevel) {
            final GateFirstLevel gate = (GateFirstLevel) actor;
            gate.addAction(new Action() {
                @Override
                public void doAction() {
                    if (!gate.isOpened() && PlayerStatus.isKey(Keys.KEY_COD157767_TOOK)) {
                        gate.setOpened(true);
                        world.destroyBody(gate.getBox2dBody());
                    }
                }
            });
        }

    }

}
