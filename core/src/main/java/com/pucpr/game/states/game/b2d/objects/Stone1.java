/**
 * Crate.class
 */

package com.pucpr.game.states.game.b2d.objects;

import com.badlogic.gdx.physics.box2d.World;
import com.pucpr.game.AppManager;


public class Stone1 extends Box {
    public Stone1(World world, AppManager manager) {
        super(world, manager, 1 , 0, 0.4f);
        name = "Crate";
    }
}
