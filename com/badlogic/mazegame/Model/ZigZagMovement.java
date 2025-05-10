package com.badlogic.mazegame.Model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ZigZagMovement implements MonsterMovementStrategy{
    private Array<Vector2> path;
    private int currentTargetIndex = 0;
    private float zigzagSpeed = 200f;

    public ZigZagMovement(Array<Vector2> zigzagPath) {
        this.path = zigzagPath;
    }

    @Override
    public void move(Monster monster, float deltaTime) {
        if (path.size > 1) {
            Vector2 target = path.get(currentTargetIndex);
            float deltaX = target.x - monster.getX();
            float deltaY = target.y - monster.getY();
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distance > 1) {
                monster.translateX((deltaX / distance) * zigzagSpeed * deltaTime);
                monster.translateY((deltaY / distance) * zigzagSpeed * deltaTime);
            } else {
                currentTargetIndex = (currentTargetIndex + 1) % path.size;
            }
        }
    }
}
