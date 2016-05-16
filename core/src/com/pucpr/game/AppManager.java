package com.pucpr.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
<<<<<<< HEAD:core/src/com/pucpr/game/AppManager.java
<<<<<<< HEAD:core/src/com/pucpr/game/AppManager.java
=======
>>>>>>> 2f5a1a8... changed path:core/src/com/pucpr/game/AppManager.java
import com.pucpr.game.resources.ResourceLoader;
import com.pucpr.game.resources.SoundManager;
import com.pucpr.game.states.startup.StartupState;
import com.pupr.game.states.AppState;
<<<<<<< HEAD:core/src/com/pucpr/game/AppManager.java
=======
import com.mygdx.game.resources.ResourceLoader;
import com.mygdx.game.resources.SoundManager;
import com.mygdx.game.states.startup.StartupState;
import com.mygdx.game.states.AppState;
>>>>>>> 757b79b... Improved class structure and fixed some movimentation bugs...:core/src/com/mygdx/game/AppManager.java
=======
>>>>>>> 2f5a1a8... changed path:core/src/com/pucpr/game/AppManager.java

public class AppManager extends ApplicationAdapter {
//	SpriteBatch batch;
//	Texture img;
    private Skin skin;
    private ResourceLoader loader = new ResourceLoader();

    private AppState state;

    @Override
    public void create() {
        GameConfig.SOUND_MANAGER = new SoundManager(this);
        final StartupState startupState = new StartupState();
        
        startupState.addTask(new Runnable() {
            @Override
            public void run() {
                skin = new Skin((FileHandle) loader.getResource("data/uiskin.json"));
            }
        });
        
        setState(startupState);
        
    }

    @Override
    public void render() {

        if (state != null) {
            state.render();
        }
    }

    public final void setState(AppState state) {
        this.state = state;
        if (state != null) {
            state.setManager(this);
            state.create();
        }
    }

    public void quit() {
        Gdx.app.exit();
    }

    public ResourceLoader getResourceLoader() {
        return loader;
    }

    public Skin getSkin() {
        return skin;
    }
}