/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;
import com.pucpr.game.states.game.basic.Conversation;

/**
 *
 * @author luis
 */
public abstract class B2Object extends Actor{

    private Action action;
    protected Body box2dBody;
    protected World world;
    protected AppManager manager;

    protected abstract void create();

    public void init(World world, AppManager manager) {
        this.world = world;
        this.manager = manager;

        create();

        if (this.box2dBody != null) {
            this.box2dBody.setUserData(this);
        }
    }

    public abstract TextureRegion getTextureRegion();

    public Body getBox2dBody() {
        return box2dBody;
    }

    public void setBox2dBody(Body box2dBody) {
        this.box2dBody = box2dBody;
    }

    public Float getAngle() {
        return box2dBody.getAngle();
    }

    @Override
    public String toString() {
        return "B2Object{" + "name=" + getName()
                + ", X: " + getBox2dBody().getPosition().x
                + ", Y: " + getBox2dBody().getPosition().y + '}';
    }

    public float getScale() {
        return GameConfig.GAME_SCALE;
    }

    public Conversation contact(Player player) {
        return null;
    }

    public void tick() {

    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setPos(float x, float y) {
        final Vector2 pos = new Vector2(x, y);
        getBox2dBody().setTransform(pos, 0);
    }

    public void setPos(Vector2 pos) {
        setPos(pos, 0);
    }

    public void setPos(Vector2 pos, float angle) {
        getBox2dBody().setTransform(pos, angle);
    }

    public static interface Action {

        void doAction();
    }
}
