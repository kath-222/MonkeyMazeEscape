package com.badlogic.mazegame.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Monster {
    private Sprite sprite;
    private MonsterMovementStrategy strategy;

    public Monster(Texture texture) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(80, 80);
        this.sprite.setPosition(630, 400); // Default starting position
    }

    public void setStrategy(MonsterMovementStrategy strategy) {
        this.strategy = strategy;
    }

    public MonsterMovementStrategy getStrategy() {
        return strategy;
    }

    public void move(float deltaTime) {
        if (strategy != null) {
            strategy.move(this, deltaTime);
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(sprite.getTexture(), sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void setTexture(Texture texture) {
        sprite.setTexture(texture);
    }

    public void setRegion(TextureRegion region) {
        sprite.setRegion(region);
    }

    public Rectangle getBoundingRectangle() {
        return sprite.getBoundingRectangle();
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void translateX(float amount) {
        sprite.translateX(amount);
    }

    public void translateY(float amount) {
        sprite.translateY(amount);
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public void dispose() {
        if (getTexture() != null) {
            getTexture().dispose();
        }
    }


}
