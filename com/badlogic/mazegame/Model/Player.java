package com.badlogic.mazegame.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player {

    private Sprite sprite;

    public Player(Texture texture, float x, float y, float width, float height) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height);
        this.sprite.setPosition(x, y);
    }

    public void move(float speed, Array<Wall> walls) {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) dy += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += speed;

        float oldX = sprite.getX();
        float oldY = sprite.getY();

        sprite.translate(dx, dy);

        Rectangle playerBounds = sprite.getBoundingRectangle();

        for (Wall wall : walls) {
            if (wall.checkCollision(playerBounds)) {
                sprite.setPosition(oldX, oldY);
                break;
            }
        }

        if (sprite.getX() < 0 || sprite.getX() + sprite.getWidth() > Gdx.graphics.getWidth()
            || sprite.getY() < 0 || sprite.getY() + sprite.getHeight() > Gdx.graphics.getHeight()) {
            sprite.setPosition(oldX, oldY);
        }
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void draw(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void dispose() {
        if (sprite.getTexture() != null) {
            sprite.getTexture().dispose();
        }
    }


}
