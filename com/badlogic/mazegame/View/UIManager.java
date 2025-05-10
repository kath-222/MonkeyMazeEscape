package com.badlogic.mazegame.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class UIManager {

    private BitmapFont font;
    private Texture heartTexture;
    private Texture bananaIconTexture;
    private Texture starTexture;
    private Texture startButtonTexture;
    private Sprite restartButton;
    private Texture startScreenTexture;

    private Rectangle startButtonBounds;

    public UIManager() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        heartTexture = new Texture("heart.png");
        bananaIconTexture = new Texture("banana-removebg-preview.png");
        starTexture = new Texture("star.png");
        startButtonTexture = new Texture("start_button.png");
        restartButton = new Sprite(new Texture("restart.png"));
        restartButton.setSize(150, 70);
        restartButton.setPosition(Gdx.graphics.getWidth() / 2 - 75, Gdx.graphics.getHeight() / 2 - 100);

        startScreenTexture = new Texture("start-screen.png");

        startButtonBounds = new Rectangle(350, 135, 100, 50);
    }

    public void renderUI(SpriteBatch batch, int score, int lives) {
        batch.draw(bananaIconTexture, 20, Gdx.graphics.getHeight() - 43, 40, 40);
        font.draw(batch, "X " + score, 70, Gdx.graphics.getHeight() - 13);

        batch.draw(heartTexture, 140, Gdx.graphics.getHeight() - 43, 40, 40);
        font.draw(batch, "X " + lives, 193, Gdx.graphics.getHeight() - 13);
    }

    public void dispose() {
        if (heartTexture != null) heartTexture.dispose();
        if (bananaIconTexture != null) bananaIconTexture.dispose();
        if (starTexture != null) starTexture.dispose();
        if (startButtonTexture != null) startButtonTexture.dispose();
        if (startScreenTexture != null) startScreenTexture.dispose();
        if (restartButton.getTexture() != null) restartButton.getTexture().dispose();
        if (font != null) font.dispose();
    }


    public void drawGameOverScreen(SpriteBatch batch) {
        font.getData().setScale(4);
        font.draw(batch, "GAME OVER", Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2);
        font.getData().setScale(2);
        font.draw(batch, "Press ENTER to restart", Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 50);

    }

    public void drawWinningScreen(SpriteBatch batch, int score) {
        font.setColor(Color.YELLOW);
        font.getData().setScale(3);
        font.draw(batch, "You Win!", Gdx.graphics.getWidth() / 2 - 80, Gdx.graphics.getHeight() / 2 + 100);

        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        font.draw(batch, "Score: " + score, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 + 50);

        int stars = 1;
        if (score >= 50 && score <= 80) stars = 2;
        else if (score > 90) stars = 3;

        for (int i = 0; i < stars; i++) {
            batch.draw(starTexture, Gdx.graphics.getWidth() / 2 - 80 + (i * 60), Gdx.graphics.getHeight() / 2 - 20, 50, 50);
        }

        restartButton.draw(batch);
    }

    public void drawStartScreen(SpriteBatch batch) {
        batch.draw(startScreenTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(startButtonTexture, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height);
    }

    public boolean isStartButtonClicked() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y;  // Convert to world coordinates
            return startButtonBounds.contains(touchPos.x, touchPos.y);
        }
        return false;
    }

    public boolean isRestartButtonClicked() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y;
            return restartButton.getBoundingRectangle().contains(touchPos.x, touchPos.y);
        }
        return false;
    }
}
