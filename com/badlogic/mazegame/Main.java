package com.badlogic.mazegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.mazegame.Controller.GameController;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private GameController gameController;

    @Override
    public void create() {
        gameController = new GameController();
        gameController.create();
    }

    @Override
    public void render() {
        gameController.render();
    }

    @Override
    public void dispose() {
        gameController.dispose();
    }
}
