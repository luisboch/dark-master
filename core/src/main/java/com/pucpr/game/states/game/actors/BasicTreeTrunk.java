/**
 * TreeTrunk1.class
 */
package com.pucpr.game.states.game.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jun 11, 2016
 */
public class BasicTreeTrunk extends B2Object {

    boolean destroyed = false;
    TextureRegion textureRegion;
    private final int NUM_COLUMNS = 8;
    private final int NUM_ROWS = 16;

    private int x, y;

    public BasicTreeTrunk(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected void create() {
        final Texture texture = manager.getResourceLoader().
                getTexture("data/images/sprites/RPG Maker/trees.png");
        final TextureRegion[][] split = TextureRegion.split(texture, texture.getWidth() / NUM_COLUMNS, texture.getHeight() / NUM_ROWS);
        textureRegion = split[y][x];
        createBox2dBody();
    }

    @Override
    public TextureRegion getTextureRegion() {
        if (destroyed) {
            return null;
        }
        return textureRegion;
    }

    private void createBox2dBody() {
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2(0, 0);
        polygon.setAsBox(1f, 0.5f, size, 0.0f);

        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        box2dBody = world.createBody(bd);
        box2dBody.createFixture(polygon, 1);
        box2dBody.setUserData(this);
    }

}
