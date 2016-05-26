/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.pucpr.game.Keys;
import com.pucpr.game.PlayerStatus;
import com.pucpr.game.states.game.basic.Conversation;
import com.pucpr.game.states.game.basic.Message;
import com.pucpr.game.handlers.StopValidator;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author luis
 */
public class Guard extends CircleObject {

    private static final int FRAME_COLS = 8;         // Sprite sheet columns size;
    private static final int FRAME_ROWS = 5;         // Sprite shet rows size;

    private static final float textureWidth = 65f;
    private static final float textureHeight = 90f;

    private boolean running = false;
    private boolean walking = false;
    private boolean inConversation = false;

    private static float textureScale = 1.5f;

    SpriteBatch spriteBatch;            // #6
    TextureRegion currentFrame;           // #7

    float stateTime;                                        // #8

    private Fixture playerPhysicsFixture;
    private Fixture playerSensorFixture;

    private Map<Direction, Animation> walkAnimation;
    private Map<Direction, Animation> runningAnimation;
    private Map<Direction, Animation> waitingAnimation;

    Direction direction = Direction.DOWN;

    public Guard() {
        setName("Guard");
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
    public Float getAngle() {
        return 0f;
    }

    @Override
    protected void create() {
        super.create(2.75f, false, 1.5f, BodyDef.BodyType.StaticBody);
    }

    @Override
    protected void loadAnimation() {
        runningAnimation = new EnumMap<Direction, Animation>(Direction.class);
        walkAnimation = new EnumMap<Direction, Animation>(Direction.class);
        waitingAnimation = new EnumMap<Direction, Animation>(Direction.class);

        final Texture sheet = manager.getResourceLoader().
                getTexture("data/images/sprites/tales of phantasia/SO_VelcantBoss-otm.png"); // #9

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
        final TextureRegion[] moveUpFrames = new TextureRegion[]{allFrames[1][0], allFrames[1][1], allFrames[1][2]};
        final TextureRegion[] moveRightFrames = new TextureRegion[]{allFrames[1][4], allFrames[1][5], allFrames[1][6]};
        final TextureRegion[] moveLeftFrames = new TextureRegion[]{allFrames[2][0], allFrames[2][1], allFrames[2][2]};

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

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
        this.walking = false;
    }

    @Override
    public boolean isWalking() {
        return walking;
    }

    /**
     *
     * @param walking
     */
    @Override
    public void setWalking(boolean walking) {
        this.walking = walking;
        this.running = false;
    }

    @Override
    public Conversation contact(Player player) {
        final PlayerStatus status = PlayerStatus.getInstance();
        final Conversation conversation = new Conversation(player, this);
        if (status.is(Keys.KEY_COD157767_TAKED)) {
            conversation.addMessage(new Message(this, "Pode passar!", 2000));
            return conversation;
        } else {
            conversation.addMessage(new Message(this, "Voce precisa da chave para avancar!"));
            conversation.addMessage(new Message(this, "Por que nao tenta o outro rapaz?"));
            return conversation;

        }
    }

}
