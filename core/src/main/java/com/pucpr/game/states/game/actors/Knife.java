/**
 * Weapon.class
 */
package com.pucpr.game.states.game.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pucpr.game.AppManager;
import com.pucpr.game.states.game.basic.BasicGameScreen;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since May 15, 2016
 */
public class Knife extends B2Object {

    private TextureRegion texture;
    private final BodyDef.BodyType bodyType;
    private Float startHitAngle = null;
    private Long startHistTms = null;

    public Knife() {
        bodyType = BodyDef.BodyType.DynamicBody;
    }

    public Knife(BodyDef.BodyType bodyType) {
        this.bodyType = bodyType;
    }

    @Override
    protected void create() {
        final BodyDef def = new BodyDef();
        def.type = this.bodyType;
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

    public Float getStartHitAngle() {
        return startHitAngle;
    }

    public void setStartHitAngle(Float startHitAngle) {
        this.startHitAngle = startHitAngle;

        if (startHitAngle != null) {
            startHistTms = System.currentTimeMillis();
        }
    }

    /**
     * Check {@link BasicGameScreen#hit() } and {@link BasicGameScreen#calculateBullets()
     * }
     *
     * @return
     */
    public boolean isComplete() {

        if (startHitAngle == null) {
            return false;
        }

        if (System.currentTimeMillis() - startHistTms > 1000) {
            return true;
        }

        final float startAngle = startHitAngle / 90 / MathUtils.degRad;
//        System.out.println("StartAngle:" + startAngle + ", CurrentAngle:" + getBox2dBody().getAngle());
        final float fullMovimentAngle = 2.5f;
        if (startAngle != 1.7f) {
            return getBox2dBody().getAngle() > (startHitAngle + fullMovimentAngle);
        } else {
            return getBox2dBody().getAngle() > (startHitAngle - fullMovimentAngle);
        }
    }

}
