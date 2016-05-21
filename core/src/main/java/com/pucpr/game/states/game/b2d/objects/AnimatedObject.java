/**
 * AnimatedObject.class
 */
package com.pucpr.game.states.game.b2d.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since May 15, 2016
 */
public abstract class AnimatedObject extends B2Object {

    protected boolean walking = false;
    private static final float linearVelReg = 10;

    @Override
    public Float getAngle() {
        return 0f;
    }

    protected void create(float radius, boolean bullet, Float linearDamping, BodyDef.BodyType bodyType) {
        create(0, bullet, linearDamping, bodyType, null);
    }

    protected void create(float radius, boolean bullet, Float linearDamping, BodyDef.BodyType bodyType, Float mass) {
        BodyDef def = new BodyDef();
        def.type = bodyType == null ? BodyDef.BodyType.DynamicBody : bodyType;
        box2dBody = world.createBody(def);
        if (linearDamping != null) {
            box2dBody.setLinearDamping(1.5f);
        }
        final CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        final Fixture bodyFixture = box2dBody.createFixture(circleShape, 1);
        if (mass != null) {
            bodyFixture.setDensity(mass);
        }
        circleShape.dispose();
        box2dBody.setBullet(bullet);
        loadAnimation();
    }

    protected abstract void loadAnimation();

    public boolean isWalking() {
        return walking;
    }

    public void setWalking(boolean walking) {
        this.walking = walking;
    }

    @Override
    public void tick() {
//        float angularVelocity = this.box2dBody.getAngularVelocity();
//        if (Math.abs(angularVelocity) > linearVelReg) {
//            if (angularVelocity > 0) {
//                angularVelocity -= linearVelReg;
//            } else {
//                angularVelocity += linearVelReg;
//            }
//        } else if (Math.abs(angularVelocity) > 0) {
//            angularVelocity = 0f;
//        }
//
//        box2dBody.setAngularVelocity(angularVelocity);

    }

}
