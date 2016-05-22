/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.b2d.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.pucpr.game.AppManager;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author luis
 */
public class Player extends AnimatedObject {

    private static final int FRAME_COLS = 19;         // Sprite sheet columns size;
    private static final int FRAME_ROWS = 4;         // Sprite shet rows size;

    private static final float textureWidth = 25f;
    private static final float textureHeight = 40f;

    private boolean running = false;
    private boolean inConversation = false;

    private static float textureScale = 1f;

    SpriteBatch spriteBatch;            // #6
    TextureRegion currentFrame;           // #7

    float stateTime;

    private Map<Direction, Animation> walkAnimation;
    private Map<Direction, Animation> runningAnimation;
    private Map<Direction, Animation> waitingAnimation;

    Direction direction = Direction.DOWN;

    private Weapon currentWeapon = null;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Player(World world, AppManager manager) {
        init(world, manager);
        name = "Player";
    }

    @Override
    protected void create() {
        super.create(0.6f, false, null, BodyDef.BodyType.DynamicBody, 5f);
    }

    @Override
    public TextureRegion getTextureRegion() {

        stateTime += Gdx.graphics.getDeltaTime();

        if (isWalking()) {
            currentFrame = walkAnimation.get(direction).getKeyFrame(stateTime, true);
        } else if (isRunning()) {
            currentFrame = runningAnimation.get(direction).getKeyFrame(stateTime, true);
        } else {
            currentFrame = waitingAnimation.get(direction).getKeyFrame(stateTime, true);
        }

        return currentFrame;

    }

    @Override
    protected void loadAnimation() {
        runningAnimation = new EnumMap<Direction, Animation>(Direction.class);
        walkAnimation = new EnumMap<Direction, Animation>(Direction.class);
        waitingAnimation = new EnumMap<Direction, Animation>(Direction.class);

        final Texture sheet = manager.getResourceLoader().
                getTexture("data/images/sprites/tales of phantasia/ClessAlveinOW-otm.png"); // #9

        final float walkVelocity = 1f;
        final float runningVelocity = 0.2f;

        final TextureRegion[][] allFrames = TextureRegion.split(sheet, sheet.getWidth() / FRAME_COLS, sheet.getHeight() / FRAME_ROWS);              // #10

        final TextureRegion[] staticDownFrames = new TextureRegion[]{allFrames[0][1]};
        final TextureRegion[] staticRightFrames = new TextureRegion[]{allFrames[3][1]};
        final TextureRegion[] staticLeftFrames = new TextureRegion[]{allFrames[0][4]};
        final TextureRegion[] staticUpFrames = new TextureRegion[]{allFrames[0][7]};

        waitingAnimation.put(Direction.DOWN, new Animation(01f, staticDownFrames));
        waitingAnimation.put(Direction.LEFT, new Animation(01f, staticLeftFrames));
        waitingAnimation.put(Direction.RIGHT, new Animation(01f, staticRightFrames));
        waitingAnimation.put(Direction.UP, new Animation(01f, staticUpFrames));

        final TextureRegion[] moveDownFrames = new TextureRegion[]{allFrames[0][0], allFrames[0][1], allFrames[0][2]};
        final TextureRegion[] moveRightFrames = new TextureRegion[]{allFrames[3][0], allFrames[3][1], allFrames[3][2]};
        final TextureRegion[] moveLeftFrames = new TextureRegion[]{allFrames[0][3], allFrames[0][4], allFrames[0][5]};
        final TextureRegion[] moveUpFrames = new TextureRegion[]{allFrames[0][6], allFrames[0][7], allFrames[0][8]};

        walkAnimation.put(Direction.DOWN, new Animation(walkVelocity, moveDownFrames));
        walkAnimation.put(Direction.RIGHT, new Animation(walkVelocity, moveRightFrames));
        walkAnimation.put(Direction.LEFT, new Animation(walkVelocity, moveLeftFrames));
        walkAnimation.put(Direction.UP, new Animation(walkVelocity, moveUpFrames));

        runningAnimation.put(Direction.DOWN, new Animation(runningVelocity, moveDownFrames));
        runningAnimation.put(Direction.RIGHT, new Animation(runningVelocity, moveRightFrames));
        runningAnimation.put(Direction.LEFT, new Animation(runningVelocity, moveLeftFrames));
        runningAnimation.put(Direction.UP, new Animation(runningVelocity, moveUpFrames));

        stateTime = 0f;
    }

    @Override
    public float getScale() {
        return textureScale;
    }

    @Override
    public Float getAngle() {
        return 0f;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
        this.walking = false;
    }

    public void setWalking(boolean walking) {
        this.walking = walking;
        this.running = false;
    }

    public boolean isInConversation() {
        return inConversation;
    }

    public void setInConversation(boolean inConversation) {
        this.inConversation = inConversation;
    }

    @Override
    public void tick() {
        // Ignored so far..
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

}
