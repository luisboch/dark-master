/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.locations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.pucpr.game.Keys;
import com.pucpr.game.NumberUtils;
import com.pucpr.game.PlayerStatus;
import com.pucpr.game.states.game.b2d.objects.B2Object;
import com.pucpr.game.states.game.b2d.objects.Box;
import com.pucpr.game.states.game.b2d.objects.Crate;
import com.pucpr.game.states.game.b2d.objects.Stone1;
import com.pucpr.game.states.game.b2d.objects.Stone2;
import com.pucpr.game.states.game.b2d.objects.Tutorial;
import com.pucpr.game.states.game.b2d.objects.Weapon;
import com.pucpr.game.states.game.basic.BasicGameScreen;

/**
 *
 * @author luis
 */
public class TutorialScreen extends BasicGameScreen {

    @Override
    protected void createBoxes() {

        for (int i = 0; i < 5; i++) {

            final Box box;
            final int random = NumberUtils.randInt(1, 3);

            if (random == 1) {
                box = new Crate(world, manager);
            } else if (random == 2) {
                box = new Stone1(world, manager);
            } else {
                box = new Stone2(world, manager);
            }
            
            final Vector2 position = box.getBox2dBody().getPosition();
            position.set(- 10 * i, -10 * i);
            box.getBox2dBody().setTransform(position, 0);
            objects.add(box);
        }

        final Tutorial t = new Tutorial(world, manager);
        Vector2 pos = t.getBox2dBody().getPosition();
        pos.set(-20, -15);
        t.getBox2dBody().setTransform(pos, 0);
        objects.add(t);

        final Weapon w = new Weapon(world, manager, BodyDef.BodyType.StaticBody);
        Vector2 wPos = w.getBox2dBody().getPosition();
        wPos.set(-20, -16);
        w.getBox2dBody().setTransform(wPos, 0);
        w.setAction(new B2Object.Action() {
            @Override
            public void doAction() {
                objects.remove(w);
                world.destroyBody(w.getBox2dBody());
                PlayerStatus.getInstance().set(Keys.SWORD_TAKED, true);
            }
        });
        
        
        objects.add(w);
    }

}
