/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.basic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pucpr.game.states.game.actors.B2Object;

/**
 *
 * @author luis
 */
public class Message {

    private B2Object object;
    private Texture image;
    private String message;

    private Actor actor;
    private final long milliseconds;
    private final StopValidator validator;

    public Message(B2Object object, Texture image, String message) {
        this(null, 5000l);
        this.object = object;
        this.image = image;
        this.message = message;
    }

    public Message(B2Object object, Texture image, String message, long milliseconds) {
        this(null, milliseconds);
        this.object = object;
        this.image = image;
        this.message = message;
    }

    public Message(StopValidator validator, long milliseconds) {
        this.milliseconds = milliseconds;
        this.validator = validator;
    }

    public Message(B2Object object, String message, StopValidator validator) {
        this(validator, 5000l);
        this.object = object;
        this.message = message;
    }

    public Message(B2Object object, String message, long milliseconds, StopValidator validator) {
        this.object = object;
        this.message = message;
        this.milliseconds = milliseconds;
        this.validator = validator;
    }

    public Message(B2Object object, String message, long milliseconds) {
        this(null, milliseconds);
        this.object = object;
        this.message = message;
    }

    public Message(B2Object object, String message) {
        this(null, 5000l);
        this.object = object;
        this.message = message;
    }

    public B2Object getObject() {
        return object;
    }

    public void setObject(B2Object object) {
        this.object = object;
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public boolean canStop() {
        return validator == null ? true : validator.canStop();
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }

    public static interface StopValidator {

        boolean canStop();
    }

}
