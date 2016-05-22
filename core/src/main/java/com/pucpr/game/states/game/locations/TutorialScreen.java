/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.locations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pucpr.game.Keys;
import com.pucpr.game.PlayerStatus;
import com.pucpr.game.states.game.actors.B2Object;
import com.pucpr.game.states.game.actors.Tutorial;
import com.pucpr.game.states.game.actors.Knife;
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
    protected void configure(final B2Object actor) {
        if (actor instanceof Knife) {
            actor.setAction(new B2Object.Action() {
                @Override
                public void doAction() {
                    actors.remove(actor);
                    world.destroyBody(actor.getBox2dBody());
                    PlayerStatus.getInstance().set(Keys.SWORD_TAKED, true);
                }
            });
        }
    }

}
