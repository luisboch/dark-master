/**
 * Weapon.class
 */
package com.pucpr.game.states.game.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since May 15, 2016
 */
public class ActorAction extends B2Object {

    private final BodyDef.BodyType bodyType;

    public ActorAction() {
        bodyType = BodyDef.BodyType.StaticBody;
    }

    public ActorAction(BodyDef.BodyType bodyType) {
        this.bodyType = bodyType;
    }

    @Override
    public TextureRegion getTextureRegion() {
        return null;
    }
}
