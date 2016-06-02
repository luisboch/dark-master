/**
 * GateFirstLevel.class
 */
package com.pucpr.game.states.game.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jun 1, 2016
 */
public class GateFirstLevel extends B2Object {

    boolean isOpened = false;

    @Override
    public TextureRegion getTextureRegion() {
        if (isOpened) {
            
            return // Textura do portão aberto;
        } else {
            return // Textura do portão fechado;
        }
    }

    public boolean isIsOpened() {
        return isOpened;
    }

    public void setIsOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

}
