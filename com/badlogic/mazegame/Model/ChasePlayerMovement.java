package com.badlogic.mazegame.Model;


import com.badlogic.gdx.graphics.g2d.Sprite;


public class ChasePlayerMovement implements MonsterMovementStrategy{
    private Sprite player;

    public ChasePlayerMovement(Sprite player) {
        this.player = player;
    }

    @Override
    public void move(Monster monster, float deltaTime) {

        float speed = 20 * deltaTime;

        float deltaX = player.getX() - monster.getX();
        float deltaY = player.getY() - monster.getY();
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 1) {
            monster.translateX((deltaX / distance) * speed);
            monster.translateY((deltaY / distance) * speed);
        }
    }
}
