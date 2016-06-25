/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.pucpr.game.AppManager;
import com.pucpr.game.PlayerStatus;
import com.pucpr.game.states.game.GameState;
import com.pucpr.game.states.BasicAppState;

/**
 *
 * @author luis
 */
public class ScreenInfo extends BasicAppState {

    private boolean showTimeOut;
    private Long timeOut;
    private Long timeOutImage;
    private long lastTimeOutMili;
    private long lastImageMili;
    private boolean showImage = false;

    private boolean showMenu = false;
    private Conversation conversation;
    private Container timeOutContainer;
    private Container imageContainer;
    private TextButton timeOutInfo;
    private final PlayerStatus status = PlayerStatus.getInstance();
    private final GameState gameState;

    public ScreenInfo(GameState gameState, AppManager manager) {
        this.gameState = gameState;
        this.manager = manager;
    }

    @Override
    public void render() {

        if (conversation != null) {
            conversation.render();
        }

        if (showTimeOut) {
            timeOut -= (System.currentTimeMillis() - lastTimeOutMili);
            lastTimeOutMili = System.currentTimeMillis();
            timeOutInfo.setText(String.valueOf(timeOut));
        }

        if (showImage) {
            timeOutImage -= (System.currentTimeMillis() - lastImageMili);
            lastImageMili = System.currentTimeMillis();

            if (timeOutImage < 0) {
                hideImage();
            }
        }

    }

    public void showTimeOut(Long timeOut) {
        if (timeOut != null) {
            this.timeOut = timeOut;
            this.showTimeOut = true;
            final Container container = new Container();
            final Table table = new Table(gameState.getManager().getSkin());

            container.setActor(table);
            container.setOrigin(Align.topLeft);

            table.row().align(Align.left);

            timeOutInfo = new TextButton("", table.getSkin());
            timeOutInfo.setWidth(Gdx.graphics.getWidth());
            table.add(timeOutInfo).left();
            timeOutInfo.setText(String.valueOf(timeOut));

            table.setWidth(Gdx.graphics.getWidth());
            table.setHeight(Gdx.graphics.getWidth());
            table.pad(0).defaults().expandX().space(0);
            container.setWidth(200f);
            container.setHeight(50f);

            final Stage stage = gameState.getStage();

            container.setX((Gdx.graphics.getWidth()) - 300);
            container.setY(500);
            stage.addActor(container);
            timeOutContainer = container;

            lastTimeOutMili = System.currentTimeMillis();
        } else {
            hideTimeOut();
        }

    }

    public void hideTimeOut() {
        if (showTimeOut) {
            this.showTimeOut = false;
            this.timeOut = null;

            final Stage stage = gameState.getStage();
            stage.getActors().removeValue(timeOutContainer, true);
            timeOutContainer.remove();
        }
    }

    @Override
    public void create() {
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        this.conversation.setSpriteBatch(gameState.getScreen().getBatch());
        this.conversation.setInfo(this);
        this.conversation.start();
    }

    public Conversation getConversation() {
        return conversation;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isTimeOver() {
        return timeOut != null && timeOut < 0;
    }

    /**
     * Show image on center of screen by the configured time
     *
     * @param path
     * @param ms
     */
    public void showImage(String path, long ms) {
        this.timeOutImage = ms;
        final Container container = new Container();
        final Table table = new Table(gameState.getManager().getSkin());

        container.setActor(table);
        container.setOrigin(Align.topLeft);

        table.row().align(Align.left);

        final Image image = new Image(manager.getResourceLoader().getTexture(path));
        table.add(image).left();

        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getWidth());
        table.pad(0).defaults().expandX().space(0);

        container.setWidth(Gdx.graphics.getWidth());
        container.setHeight(Gdx.graphics.getHeight());

        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getHeight());

//        container.setX(0 - (Gdx.graphics.getWidth() / 2) + 60);
//        container.setY((Gdx.graphics.getWidth() / 2) - 20);
        final Stage stage = gameState.getStage();

//        container.setX();
//        container.setY(500);
        stage.addActor(container);
        imageContainer = container;
        lastImageMili = System.currentTimeMillis();
        showImage = true;
    }

    public void hideImage() {
        if (showImage) {
            this.showImage = false;
            this.timeOutImage = null;

            final Stage stage = gameState.getStage();
            stage.getActors().removeValue(imageContainer, true);
            imageContainer.remove();
        }
    }
}
