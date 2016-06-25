/**
 * Congrulations.class
 */

package com.pucpr.game.states.game.locations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pucpr.game.AppManager;
import com.pucpr.game.states.AppState;
import com.pucpr.game.states.menu.MenuState;

/**
 *
 * @author Luis Boch 
 * @email luis.c.boch@gmail.com
 * @since Jun 25, 2016
 */
public class Congrulations implements AppState, InputProcessor  {

    private AppManager manager;

    private Stage stage;
    private Table container;

    public Congrulations() {
    }

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        stage = new Stage();

        Gdx.graphics.setVSync(false);

        container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        final Table table = new Table();

        final ScrollPane scroll = new ScrollPane(table, manager.getSkin());

        table.pad(10).defaults().expandX().space(4);
        table.row();
        Label label = new Label("Parabens!", manager.getSkin());
        table.add(label).expandX().fillX();

        container.add(label).center().fill().colspan(1);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void setManager(AppManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        manager.setState(new MenuState());
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        manager.setState(new MenuState());
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        manager.setState(new MenuState());
        Gdx.input.setInputProcessor(this);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
