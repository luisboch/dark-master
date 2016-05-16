/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.basic;

import com.pucpr.game.PlayerStatus;
import com.pucpr.game.states.game.GameState;
import com.pupr.game.states.BasicAppState;

/**
 *
 * @author luis
 */
public class ScreenInfo extends BasicAppState {

    private boolean showMenu = false;
    private Conversation conversation;
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
    
    
}
