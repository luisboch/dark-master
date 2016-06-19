/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;
import com.pucpr.game.handlers.Action;
import com.pucpr.game.states.game.basic.Conversation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luis
 */
public abstract class B2Object extends Actor {

    private MapProperties properies;
    private final List<Action> actions = new ArrayList<Action>();
    private final List<Action> touchActions = new ArrayList<Action>();
    protected Body box2dBody;
    protected World world;
    protected AppManager manager;

    private boolean destroyOnHit = false;

    protected void create() {
    }

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
        if (this.box2dBody != null) {
            this.box2dBody.setUserData(this);
        }
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
        return new Action() {
            @Override
            public void doAction() {
                for (Action a : actions) {
                    a.doAction();
                }
            }
        };
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void rmAction(Action action) {
        removeAction(action);
    }

    public void removeAction(Action action) {
        actions.add(action);
    }

    public Action getTouchAction() {
        return new Action() {
            @Override
            public void doAction() {
                for (Action a : touchActions) {
                    a.doAction();
                }
            }
        };
    }

    public void addTouchAction(Action action) {
        touchActions.add(action);
    }

    public void rmTouchAction(Action action) {
        removeTouchAction(action);
    }

    public void removeTouchAction(Action action) {
        touchActions.add(action);
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

    public MapProperties getProperies() {
        return properies;
    }

    public void setProperies(MapProperties properies) {
        this.properies = properies;
    }

    public boolean isDestroyOnHit() {
        return destroyOnHit;
    }

    public void setDestroyOnHit(boolean destroyOnHit) {
        this.destroyOnHit = destroyOnHit;
    }

}
