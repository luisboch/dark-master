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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;
import com.pucpr.game.Keys;
import com.pucpr.game.PlayerStatus;
import com.pucpr.game.states.game.basic.Conversation;
import com.pucpr.game.states.game.basic.Message;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author luis
 */
public class Tutorial extends AnimatedObject {

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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Tutorial(World world, AppManager manager) {
        super(world, manager);
        name = "Professor";
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
        super.create(0.5f, false, 1.5f, 10, 20, BodyDef.BodyType.StaticBody);
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
        if (!status.is(Keys.SWORD_TAKED)) {

            final Conversation conversation = new Conversation(player, this);
            conversation.addMessage(
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 5339b41... basic conversation structure finished.
                    new Message(this, "Voce deve pegar a espada para \n"
                            + "iniciarmos seu treinamento!", new Message.StopValidator() {
                        @Override
                        public boolean canStop() {
                            return status.is(Keys.SWORD_TAKED);
                        }
                    }));

            conversation.addMessage(new Message(player, "Pronto, peguei!", 2000));

<<<<<<< HEAD
            return conversation;
        } else {
            final Conversation conversation = new Conversation(player, this, null, 1000);
            conversation.addMessage(new Message(this, "Legal, voce pegou "));
            conversation.addMessage(new Message(player, "Voce vai me ensinar?"));
            conversation.addMessage(new Message(this, "Claro...", 1000));
            conversation.addMessage(new Message(player,
                    "Claro, vamos iniciar, \nvocê precisa bater caixa. \n"
=======
                    new Message(this, null, "Você deve pegar a espada para \n"
                            + "iniciarmos seu treinamento!"));
            conversation.addMessage(new Message(player, "Ok...", 2000));
=======
>>>>>>> 5339b41... basic conversation structure finished.
            return conversation;
        } else {
            final Conversation conversation = new Conversation(player, this, null, 1000);
            conversation.addMessage(new Message(this, "Legal, voce pegou "));
            conversation.addMessage(new Message(player, "Voce vai me ensinar?"));
            conversation.addMessage(new Message(this, "Claro...", 1000));
            conversation.addMessage(new Message(player,
<<<<<<< HEAD
                    "Sim, pra começar, você precisa bater caixa. \n"
>>>>>>> c19b50f... basic conversation structure finished.
=======
                    "Claro, vamos iniciar, \nvocê precisa bater caixa. \n"
>>>>>>> 5339b41... basic conversation structure finished.
                    + "Para isso, vá até ela e use \n"
                    + "sua recém adquirida espada!", new Message.StopValidator() {
                @Override
                public boolean canStop() {
                    return status.is(Keys.SIMPLE_HIT_TEST);
                }
            }));

            conversation.addMessage(new Message(this, "Legal, você conseguiu!", 2000));
            conversation.addMessage(new Message(this, "Agora podemos lutar!", 2000));

            return conversation;

        }
    }

}
