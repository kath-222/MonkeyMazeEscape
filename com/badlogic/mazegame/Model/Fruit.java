package com.badlogic.mazegame.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Fruit implements GameComponent {
    private Sprite sprite;
    private boolean collected = false;

    public Fruit(Texture texture, float x, float y) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(50, 50); // Adjust size based on your game style
        this.sprite.setPosition(x, y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!collected) {
            sprite.draw(batch);
        }
    }

    @Override
    public boolean checkCollision(Rectangle otherBoundingRectangle) {
        if (!collected && sprite.getBoundingRectangle().overlaps(otherBoundingRectangle)) {
            collected = true;
            ScoreManager.getInstance().addPoints(10); // Increase score when collected
            return true;
        }
        return false;
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return sprite.getBoundingRectangle();
    }

    public Sprite getSprite() {
        return sprite;
    }

}
