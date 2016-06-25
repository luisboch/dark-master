/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.game.states.game.locations;

import com.pucpr.game.states.game.basic.BasicGameScreen;

/**
 *
 * @author Luis Carlos
 */
public class Village extends BasicGameScreen {

    public Village() {
        super("village.tmx");
    }

    @Override
    public void create() {
        super.create();
        manager.setState(new Congrulations());
    }

    @Override
    public void render() {
        super.render();
    }

}
