package com.badlogic.mazegame.Model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Wall implements GameComponent {

    private Rectangle bounds;


    public Wall(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);

    }

    @Override
    public void draw(SpriteBatch batch) {

    }


    @Override
    public boolean checkCollision(Rectangle otherBoundingRectangle) {
        return bounds.overlaps(otherBoundingRectangle);
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return bounds;
    }

}
