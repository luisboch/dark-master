/**
 * Crate.class
 */

package com.pucpr.game.states.game.b2d.objects;

import com.badlogic.gdx.physics.box2d.World;
import com.pucpr.game.AppManager;


public class Crate extends Box {

    public Crate(World world, AppManager manager) {
        super(world, manager, 7, 1, 0.5f);
        name = "Crate";
    }

}
