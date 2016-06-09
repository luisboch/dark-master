/**
 * GateFirstLevel.class
 */
package com.pucpr.game.states.game.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pucpr.game.AppManager;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Jun 1, 2016
 */
public class GateFirstLevel extends B2Object {

    boolean opened = false;
    private TextureRegion textureOpen;
    private TextureRegion textureClosed;

    public void create() {
        setName("GateFirstLevel");
        loadAnimation();
    }

    public void loadAnimation() {

        final Texture sheet = manager.getResourceLoader().
                getTexture("data/images/sprites/maps/hyptosis.png"); // #9

        final TextureRegion[][] allFrames = TextureRegion.split(sheet, sheet.getWidth() / 15, sheet.getHeight() / 30);              // #10

        textureClosed = allFrames[0][0];
        textureOpen = allFrames[1][3];
    }

    public void createBox2dBody(World world) {
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2(2f, 1f);
        polygon.setAsBox(2f, 1f, size, 0.0f);
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        this.box2dBody = world.createBody(def);
        this.box2dBody.createFixture(polygon, 1);
        box2dBody.setUserData(this);
    }

    @Override
    public void init(World world, AppManager manager) {
        this.manager = manager;
        createBox2dBody(world);
        create();
    }

    @Override
    public TextureRegion getTextureRegion() {
        if (isOpened()) {
            return textureOpen;
        } else {
            return textureClosed;
        }
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

}
