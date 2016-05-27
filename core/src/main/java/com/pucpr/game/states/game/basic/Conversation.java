/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.pucpr.game.states.game.actors.B2Object;
import com.pucpr.game.states.game.actors.Player;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luis
 */
public class Conversation {

    private static final Conversation EMPTY = new Conversation(null, null);

    private final Player player;
    private final B2Object target;
    private final List<Message> conversation = new ArrayList<Message>();
    private Message currentMessage;
    private int currentIndex = -1;
    private final int timeBetweenMsg;
    private ScreenInfo info;
    private boolean aborted = false;
    private boolean finished = false;
    private Long startedMessage;

    private Runnable onFinish;

    private SpriteBatch batch;
    /**
     * When this is null, this conversation is showing message, when stopped
     * show, this will be current date (long). When time is done, we set this
     * time to null, and show the next message.
     */
    private Long waitMessageWithoutMessage;

    public Conversation(Player player, B2Object target) {
        this(player, target, null);
    }

    public Conversation(Player player, B2Object target, List<Message> messages) {
        this(player, target, messages, 1000);
    }

    public Conversation(Player player, B2Object target, List<Message> messages, int timeBetweenMsg) {
        this.player = player;
        this.target = target;
        this.timeBetweenMsg = timeBetweenMsg;

        if (messages != null) {
            this.conversation.addAll(messages);
        }

        /**
         */
    }

    public void setSpriteBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public void render() {
        if (!aborted && !finished) {
            if (waitMessageWithoutMessage != null) {
                if ((System.currentTimeMillis() - waitMessageWithoutMessage) > timeBetweenMsg) {
                    next();
                    showMessage();
                }
            }
            if (startedMessage != null) {
                if (currentMessage.canStop() && (System.currentTimeMillis() - startedMessage) > currentMessage.getMilliseconds()) {
                    hideMessage();
                } else {

//                    batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//                    batch.begin();
//
//                    ((Table) currentMessage.getActor()).draw(batch, 1);
//                    batch.end();
                }
            }
        }
    }

    public void addMessage(Message m) {

        if (player == null) {
            throw new IllegalStateException("This conversation is empty and read-only!");
        }

        this.conversation.add(m);
    }

    public void next() {
        currentIndex++;
        if (currentIndex < conversation.size()) {
            currentMessage = conversation.get(currentIndex);
        } else {
            // finished
            finished = true;
            if (onFinish != null) {
                onFinish.run();
            }
        }
    }

    public void hideMessage() {
        startedMessage = null;
        waitMessageWithoutMessage = System.currentTimeMillis();

        final Container container = (Container) (currentMessage != null ? currentMessage.getActor() : null);

        if (container != null) {
            final Stage stage = info.getGameState().getStage();
            stage.getActors().removeValue(container, true);
            container.remove();
        }

    }

    public void showMessage() {
        if (!finished && !aborted) {
            startedMessage = System.currentTimeMillis();
            waitMessageWithoutMessage = null;
            final Container container = new Container();
            final Table table = new Table(info.getGameState().getManager().getSkin());
            container.setActor(table);
            container.setOrigin(Align.topLeft);
            table.row().align(Align.left);

            if (currentMessage.getImage() != null) {
                final Texture img = currentMessage.getImage();
                table.add(new Image(img)).center();
            } else {
                final TextButton button = new TextButton(currentMessage.getObject().getName(), table.getSkin());
                button.setWidth(150);
                table.add(button).left();
                table.row().align(Align.left);
            }

            final TextButton button = new TextButton(currentMessage.getMessage(), table.getSkin());
            button.setWidth(150);
            table.add(button).left();

            table.setWidth(150);
            table.setHeight(100);
            table.pad(0).defaults().expandX().space(0);
            container.setDebug(true, true);
            container.setWidth(250f);
            container.setHeight(100f);
            container.setX((Gdx.graphics.getWidth() / 2) -100);
            container.setY(500);

            currentMessage.setActor(container);
            currentMessage.doAction();

            final Stage stage = info.getGameState().getStage();
            stage.addActor(container);
        }
    }

    public void setInfo(ScreenInfo info) {
        this.info = info;
    }

    public boolean isReadOnly() {
        return player == null;
    }

    public void setOnFinish(Runnable onFinish) {
        this.onFinish = onFinish;
    }

    void start() {
        currentIndex++;
        if (currentIndex < conversation.size()) {
            currentMessage = conversation.get(currentIndex);
            showMessage();
        } else {
            // finished
            finished = true;
            if (onFinish != null) {
                onFinish.run();
            }
        }

    }

    public void abort() {
        hideMessage();
        aborted = true;
    }
}
