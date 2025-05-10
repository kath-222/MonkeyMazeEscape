package com.badlogic.mazegame.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.mazegame.Model.*;
import com.badlogic.mazegame.ScoreDAO;
import com.badlogic.mazegame.View.UIManager;

public class GameController {

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Player player;
    private Monster monster;
    private UIManager uiManager;

    private Texture monsterTexture, monsterTexture2, monsterTexture3;
    private ZigZagMovement zigzagMovementStrategy;
    private Array<Vector2> zigzagPath;

    private Level[] levels;
    private int currentLevelIndex;

    private Array<Fruit> fruits;
    private Array<Wall> walls;

    private long lastBananaChangeTime;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean showStartScreen = true;

    private Texture bananaTexture, appleTexture, watermelonTexture, grapeTexture;

    private int lives = 3;
    private int score = 0;

    public GameController() {

    }

    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Initialize player
        Texture characterTexture = new Texture("Picture1.png");
        player = new Player(characterTexture, 200, 180, 60, 60);

        // Initialize monsters
        monsterTexture = new Texture("monster1111.png");
        monsterTexture2 = new Texture("monster22.png");
        monsterTexture3 = new Texture("monster3.png");

        monster = new Monster(monsterTexture);
        monster.setStrategy(new VerticalMovement());

        // Initialize walls and fruits
        walls = new Array<>();
        fruits = new Array<>();

        // Initialize levels
        Texture chestTexture = new Texture("chest.png");
        levels = new Level[]{
            new Level(new Texture("Untitled design.png"), new Chest(chestTexture, 670, 340, 100, 100), monsterTexture),
            new Level(new Texture("picnic.png"), new Chest(chestTexture, 390, 350, 90, 90), monsterTexture2),
            new Level(new Texture("lava.png"), new Chest(chestTexture, 145, 180, 90, 90), monsterTexture3)
        };
        levels[0].setActive(true);
        currentLevelIndex = 0;

        // Initialize fruits
        bananaTexture = new Texture("banana-removebg-preview.png");
        appleTexture = new Texture("apple.png");
        watermelonTexture = new Texture("watermelon.png");
        grapeTexture = new Texture("grape.png");

        spawnFruits(1);
        setupWalls();

        // Zigzag movement path
        zigzagPath = new Array<>();
        zigzagPath.add(new Vector2(100, 100));
        zigzagPath.add(new Vector2(300, 200));
        zigzagPath.add(new Vector2(500, 100));
        zigzagPath.add(new Vector2(700, 200));
        zigzagPath.add(new Vector2(500, 300));
        zigzagPath.add(new Vector2(300, 400));
        zigzagPath.add(new Vector2(100, 300));
        zigzagMovementStrategy = new ZigZagMovement(zigzagPath);

        uiManager = new UIManager();

        score = ScoreDAO.loadScore();
        lastBananaChangeTime = TimeUtils.nanoTime();
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        if (showStartScreen) {
            uiManager.drawStartScreen(batch);
            batch.end();
            handleStartScreen();
            return;
        }

        if (gameOver) {
            uiManager.drawGameOverScreen(batch);
            batch.end();
            handleRestart();
            return;
        }

        if (gameWon) {
            uiManager.drawWinningScreen(batch, ScoreManager.getInstance().getScore());
            batch.end();
            handleWinningScreen();
            return;
        }

        batch.draw(getCurrentLevel().getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        handleInput();
        monster.move(Gdx.graphics.getDeltaTime());

        if (monster.getTexture() != getCurrentLevel().getMonsterTexture()) {
            monster.setRegion(new TextureRegion(getCurrentLevel().getMonsterTexture()));
        }

        if (currentLevelIndex == 1) {
            monster.setStrategy(new ChasePlayerMovement(player.getSprite()));
        } else if (currentLevelIndex == 2 && !(monster.getStrategy() instanceof ZigZagMovement)) {
            monster.setStrategy(zigzagMovementStrategy);
        }

        player.draw(batch);
        for (Fruit fruit : fruits) {
            fruit.draw(batch);
        }
        monster.draw(batch);
        getCurrentLevel().getChest().draw(batch);

        uiManager.renderUI(batch, ScoreManager.getInstance().getScore(), lives);

        batch.end();

        checkCollisions();
        setupWalls();

        if (TimeUtils.nanoTime() - lastBananaChangeTime > 4 * 1000000000L) {
            changeBananaPositions();
            lastBananaChangeTime = TimeUtils.nanoTime();
        }
    }

    public void dispose() {
        batch.dispose();
        player.dispose();
        monster.dispose();
        uiManager.dispose();
    }

    private void handleInput() {
        float speed = 200 * Gdx.graphics.getDeltaTime();
        player.move(speed, walls);
    }

    private void checkCollisions() {
        Rectangle charBounds = player.getSprite().getBoundingRectangle();

        for (int i = fruits.size - 1; i >= 0; i--) {
            if (fruits.get(i).checkCollision(charBounds)) {
                fruits.removeIndex(i);
            }
        }

        if (charBounds.overlaps(getCurrentLevel().getChest().getBoundingRectangle())) {
            if (currentLevelIndex < levels.length - 1) {
                currentLevelIndex++;
                levels[currentLevelIndex].setActive(true);
                Vector2 pos = getStartingPositionForLevel(currentLevelIndex);
                player.setPosition(pos.x, pos.y);
                spawnFruits(currentLevelIndex + 1);
                setupWalls();
            } else {
                gameWon = true;
                ScoreDAO.saveScore(ScoreManager.getInstance().getScore());
            }
        }

        if (charBounds.overlaps(monster.getBoundingRectangle())) {
            lives--;
            if (lives > 0) {
                Vector2 pos = getStartingPositionForLevel(currentLevelIndex);
                player.setPosition(pos.x, pos.y);
            } else {
                gameOver = true;
                ScoreDAO.saveScore(ScoreManager.getInstance().getScore());
            }
        }
    }

    private void handleStartScreen() {
        if (uiManager.isStartButtonClicked()) {
            showStartScreen = false;
        }
    }

    private void handleRestart() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            restartGame();
        }
    }

    private void handleWinningScreen() {
        if (uiManager.isRestartButtonClicked()) {
            restartGame();
        }
    }

    private void restartGame() {
        showStartScreen = true;
        gameOver = false;
        gameWon = false;
        lives = 3;
        score = 0;
        currentLevelIndex = 0;

        ScoreManager.getInstance().resetScore();
        player.setPosition(200, 180);
        monster.setPosition(650, 500);
        monster.setStrategy(new VerticalMovement());

        spawnFruits(1);
        setupWalls();
    }

    private void spawnFruits(int level) {
        fruits.clear();
        if (level == 1) {
            fruits.add(new Fruit(bananaTexture, 130, 16));
            fruits.add(new Fruit(appleTexture, 350, 370));
            fruits.add(new Fruit(grapeTexture, 550, 200));
            fruits.add(new Fruit(watermelonTexture, 700, 100));
            fruits.add(new Fruit(bananaTexture, 450, 300));
            fruits.add(new Fruit(watermelonTexture, 700, 250));
            fruits.add(new Fruit(appleTexture, 100, 180));
            fruits.add(new Fruit(grapeTexture, 250, 50));
        } else if (level == 2) {
            fruits.add(new Fruit(appleTexture, 100, 300));
            fruits.add(new Fruit(grapeTexture, 500, 50));
            fruits.add(new Fruit(bananaTexture, 650, 250));
            fruits.add(new Fruit(watermelonTexture, 200, 120));
            fruits.add(new Fruit(grapeTexture, 350, 200));
            fruits.add(new Fruit(bananaTexture, 550, 170));
            fruits.add(new Fruit(watermelonTexture, 600, 300));
            fruits.add(new Fruit(appleTexture, 700, 50));
        } else if (level == 3) {
            fruits.add(new Fruit(watermelonTexture, 50, 50));
            fruits.add(new Fruit(bananaTexture, 300, 350));
            fruits.add(new Fruit(grapeTexture, 550, 150));
            fruits.add(new Fruit(appleTexture, 750, 200));
            fruits.add(new Fruit(grapeTexture, 100, 200));
            fruits.add(new Fruit(watermelonTexture, 400, 250));
            fruits.add(new Fruit(appleTexture, 650, 350));
            fruits.add(new Fruit(bananaTexture, 500, 100));
        }
    }


    private void setupWalls() {
        walls.clear();

        switch (currentLevelIndex) {
            case 0:
                walls.add(new Wall(0, Gdx.graphics.getHeight() - 50, Gdx.graphics.getWidth(), 50));
                walls.add(new Wall(0, 90, 440, 80));
                walls.add(new Wall(540, 0, 80, 158));
                walls.add(new Wall(716, 160, 80, 158));
                walls.add(new Wall(94, 290, 80, 158));
                walls.add(new Wall(180, 290, 80, 80));
                walls.add(new Wall(450, 370, 169, 80));
                walls.add(new Wall(540, 290, 80, 80));
                walls.add(new Wall(360, 100, 80, 150));
                break;

            case 1:
                walls.add(new Wall(0, Gdx.graphics.getHeight() - 50, Gdx.graphics.getWidth(), 50));
                walls.add(new Wall(140, 230, 320, 50));
                walls.add(new Wall(490, 0, 60, 140));
                walls.add(new Wall(740, 230, 60, 50));
                walls.add(new Wall(0, 270, 40, 170));
                walls.add(new Wall(140, 290, 60, 90));
                walls.add(new Wall(340, 260, 60, 200));
                walls.add(new Wall(200, 345, 60, 40));
                walls.add(new Wall(0, 105, 120, 60));
                walls.add(new Wall(186, 0, 60, 110));
                walls.add(new Wall(250, 70, 170, 55));
                walls.add(new Wall(360, 104, 60, 60));
                walls.add(new Wall(490, 88, 160, 60));
                walls.add(new Wall(600, 85, 60, 160));
                walls.add(new Wall(740, 0, 60, 100));
                walls.add(new Wall(550, 213, 60, 40));
                walls.add(new Wall(475, 345, 60, 140));
                walls.add(new Wall(630, 345, 60, 140));
                break;

            case 2:
                walls.add(new Wall(0, Gdx.graphics.getHeight() - 50, Gdx.graphics.getWidth(), 50));
                walls.add(new Wall(84, 0, 68, 338));
                walls.add(new Wall(225, 67, 68, 270));
                walls.add(new Wall(140, 270, 80, 65));
                walls.add(new Wall(225, 67, 270, 65));
                walls.add(new Wall(480, 67, 68, 270));
                walls.add(new Wall(359, 250, 60, 200));
                walls.add(new Wall(615, 310, 65, 170));
                walls.add(new Wall(615, 315, 120, 65));
                walls.add(new Wall(615, 0, 65, 200));
                walls.add(new Wall(615, 140, 120, 65));
                break;
        }
    }


    private void changeBananaPositions() {
        for (Fruit fruit : fruits) {
            fruit.getSprite().setPosition((float) Math.random() * Gdx.graphics.getWidth(), (float) Math.random() * Gdx.graphics.getHeight());
        }
    }

    private Level getCurrentLevel() {
        return levels[currentLevelIndex];
    }

    private Vector2 getStartingPositionForLevel(int levelIndex) {
        switch (levelIndex) {
            case 0: return new Vector2(200, 180);
            case 1: return new Vector2(100, 30);
            case 2: return new Vector2(10, 100);
            default: return new Vector2(200, 180);
        }
    }
}
