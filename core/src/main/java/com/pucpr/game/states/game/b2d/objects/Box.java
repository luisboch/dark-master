/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.b2d.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;

public class Box extends AnimatedObject {

    private final TextureRegion textureRegion;
    private final Float textureSize = 32f;
    private final float scale;

    public Box(World world, AppManager manager, int row, int col, float scale) {

        name = "Box";
        Texture t = manager.getResourceLoader().getTexture("data/images/sprites/util/japanese-village.png");
        TextureRegion[][] split = TextureRegion.split(t, textureSize.intValue(), textureSize.intValue());
        textureRegion = split[row][col];

        float diff = (GameConfig.PPM - textureSize);
        float auxScale = (1f + (diff / textureSize));

        this.scale = auxScale * scale;
        
        init(world, manager);

    }

    @Override
    protected void create() {
        // next we create 50 boxes at random locations above the ground
        // body. First we create a nice polygon representing a box 2 meters
        // wide and high.
        final PolygonShape boxPoly = new PolygonShape();
        boxPoly.setAsBox(this.scale, this.scale);

        final BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = BodyType.StaticBody;
        box2dBody = world.createBody(boxBodyDef);
        box2dBody.createFixture(boxPoly, 1);
    }

    @Override
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    protected void loadAnimation() {
    }

}
