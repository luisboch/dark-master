/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
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
    private long lastMili;
    private boolean showMenu = false;
    private Conversation conversation;
    private Container timeOutContainer;
    private TextButton timeOutInfo;
    private final PlayerStatus status = PlayerStatus.getInstance();
    private final GameState gameState;

    public ScreenInfo(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void render() {

        if (conversation != null) {
            conversation.render();
        }

        if (showTimeOut) {
            timeOut -= (System.currentTimeMillis() - lastMili);
            lastMili = System.currentTimeMillis();
            timeOutInfo.setText(String.valueOf(timeOut));
        }
    }

    public void showTimeOut(long timeOut) {
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

        container.setDebug(true, true);
        container.setWidth(200f);
        container.setHeight(50f);
//        container.setX(0 - (Gdx.graphics.getWidth() / 2) + 60);
//        container.setY((Gdx.graphics.getWidth() / 2) - 20);

        final Stage stage = gameState.getStage();

        container.setX((Gdx.graphics.getWidth() / 2) - 100);
        container.setY(500);
        stage.addActor(container);
        timeOutContainer = container;

        lastMili = System.currentTimeMillis();
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
}
