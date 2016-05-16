/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.b2d.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;
import com.pucpr.game.states.game.basic.Conversation;

/**
 *
 * @author luis
 */
public abstract class B2Object {
    private Action action;
    protected String name;
    protected Body box2dBody;
    protected final World world;
    protected final AppManager manager;
    protected abstract void create();

    public B2Object(World world, AppManager manager) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAngle() {
        return box2dBody.getAngle();
    }

    @Override
    public String toString() {
        return "B2Object{" + "name=" + name
                + ", X: " + getBox2dBody().getPosition().x
                + ", Y: " + getBox2dBody().getPosition().y + '}';
    }

    public float getScale() {
        return GameConfig.GAME_SCALE;
    }
    
    public Conversation contact(Player player) {
        return null;
    }

    
    public void tick(){
        
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    
    
    public static interface Action {
        void doAction();
    }
}
