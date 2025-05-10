package com.badlogic.mazegame.Model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public interface GameComponent {

    // Method to draw the component
    void draw(SpriteBatch batch);

    // Method to check if the component collides with a given rectangle
    boolean checkCollision(Rectangle otherBoundingRectangle);

    // Method to get the bounding rectangle of the component for collision detection
    Rectangle getBoundingRectangle();
}
