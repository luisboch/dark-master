/**
 * SimpleBox2dObject.class
 */

package com.pucpr.game.states.game.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.pucpr.game.AppManager;


public class SimpleBox2dObject extends B2Object {

    public SimpleBox2dObject(Body body, World world, AppManager manager) {
        this.box2dBody = body;
        init(world, manager);
    }

    
    @Override
    protected void create() {
    }

    @Override
    public TextureRegion getTextureRegion() {
        return null;
    }

}
