package com.badlogic.mazegame.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Chest implements GameComponent {

    private Sprite chestSprite;

    public Chest(Texture chestTexture, float x, float y, float width, float height) {
        chestSprite = new Sprite(chestTexture);
        chestSprite.setSize(width, height);
        chestSprite.setPosition(x, y);
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return chestSprite.getBoundingRectangle();
    }

    @Override
    public void draw(SpriteBatch batch) {
        chestSprite.draw(batch);
    }


    @Override
    public boolean checkCollision(Rectangle otherBoundingRectangle) {
        return this.getBoundingRectangle().overlaps(otherBoundingRectangle);
    }
}
