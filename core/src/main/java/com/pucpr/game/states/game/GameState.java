/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;
import com.pucpr.game.states.AppState;
import com.pucpr.game.states.game.basic.BasicGameScreen;
import com.pucpr.game.states.game.basic.GameData;
import com.pucpr.game.states.game.basic.ScreenInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public class GameState implements AppState {

    private AppManager manager;

    private BasicGameScreen screen;
    private ScreenInfo screenInfo;
    private GameData gameData;
    private Stage stage = new Stage();
    private int slowVelocityCtrl = 0;

    private long startedChangeScreen;
    private BasicGameScreen nextGameScreen;
    private String nextScreenGateName;

    @Override
    public void render() {
        final boolean skipFrame = GameConfig.skipFrames ? slowVelocityCtrl != GameConfig.skipFramesQty : false;

        if (GameConfig.skipFrames) {
            if (slowVelocityCtrl == GameConfig.skipFramesQty) {
                slowVelocityCtrl = -1;
            }

            slowVelocityCtrl++;
        }

        stage.act(Gdx.graphics.getDeltaTime());

        if (!skipFrame) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (nextGameScreen != null) {

                if (System.currentTimeMillis() - startedChangeScreen > GameConfig.waitScreenToChange) {
                    changeScreen();
                }

            } else if (screen != null) {
                screen.render();
            }

            if (screenInfo != null) {
                screenInfo.render();
            }

            stage.draw();
        }
    }

    @Override
    public void setManager(AppManager s) {
        this.manager = s;
    }

    @Override
    public void create() {

        if (gameData == null) {
            gameData = new GameData();
        }

        if (screenInfo == null) {
            screenInfo = new ScreenInfo(this, manager);

        }

        loadCurrentScreen();

    }

    private void loadCurrentScreen() {
        try {
            final String className = gameData.getString("game.current.screen");
            final Class clazz = Class.forName(className);
            setScreen((BasicGameScreen) clazz.newInstance());
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    public BasicGameScreen getScreen() {
        return screen;
    }

    public AppManager getManager() {
        return manager;
    }

    public Stage getStage() {
        return stage;
    }

    public ScreenInfo getScreenInfo() {
        return screenInfo;
    }

    public void setScreen(BasicGameScreen screen) {
        setScreen(screen, null);
    }

    public void setScreen(BasicGameScreen screen, String gateName) {

        nextGameScreen = screen;
        startedChangeScreen = System.currentTimeMillis();
        GameConfig.SOUND_MANAGER.setRunning(false);
        nextScreenGateName = gateName;
        if (this.screen == null) {
            changeScreen();
        }

    }

    private void changeScreen() {

        if (this.screen != null) {
            this.screen.close();
        }

        this.screenInfo.hideTimeOut();
        this.screen = nextGameScreen;
        screen.setManager(manager);
        screen.setGameState(this);
        screen.setStage(stage);
        screen.create();
        
        if (nextScreenGateName != null && !nextScreenGateName.equals("")) {
            screen.movePlayerToGate(nextScreenGateName);
        }
        nextGameScreen = null;

    }

}
