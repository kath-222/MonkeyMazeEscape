package com.badlogic.mazegame.Model;


import com.badlogic.gdx.graphics.Texture;

public class Level {
    private boolean isActive;
    private Texture background;
    private Chest chest;
    private Texture monsterTexture;

    public Level(Texture bg, Chest chest, Texture monsterTexture) {
        this.background = bg;
        this.chest = chest;
        this.monsterTexture = monsterTexture;
        this.isActive = false;
    }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }

    public Texture getBackground() { return background; }
    public Chest getChest() { return chest; }
    public Texture getMonsterTexture() { return monsterTexture; }
}


