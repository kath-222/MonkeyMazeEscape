package com.badlogic.mazegame.Model;

public class VerticalMovement implements MonsterMovementStrategy{
    private boolean movingDown = true;



    @Override
    public void move(Monster monster, float deltaTime) {
        float speed = 100 * deltaTime;

        if (monster.getY() >= 380) {
            movingDown = true;
        }
        if (monster.getY() <= 0) {
            movingDown = false;
        }

        if (movingDown) {
            monster.translateY(-speed);
        } else {
            monster.translateY(speed);
        }
    }
}
