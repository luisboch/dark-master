/**
 * Weapon.class
 */
package com.pucpr.game.states.game.b2d.objects;

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
 * @since May 15, 2016
 */
public class Weapon extends B2Object {

    private TextureRegion texture;

    public Weapon(World world, AppManager manager) {
        init(world, manager);
    }

    @Override
    protected void create() {
        final BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        box2dBody = world.createBody(def);
        box2dBody.setLinearDamping(3.5f);
        box2dBody.setAngularDamping(3.5f);

        final PolygonShape pol = new PolygonShape();
        pol.setAsBox(0.2f, 0.8f);
        box2dBody.createFixture(pol, 1);
        pol.dispose();
        box2dBody.setBullet(false);
        box2dBody.setAwake(false);

        loadAnimation();
    }

    private void loadAnimation() {
        final Texture sheet = manager.getResourceLoader().
                getTexture("data/images/sprites/tales of phantasia/Weapons.png"); // #9

        final TextureRegion[][] allFrames = TextureRegion.split(sheet, sheet.getWidth() / 6, sheet.getHeight() / 6);              // #10
        texture = allFrames[0][0];
    }

    @Override
    public TextureRegion getTextureRegion() {
        return texture;
    }

    @Override
    public float getScale() {
        return 1.5f;
    }


}
