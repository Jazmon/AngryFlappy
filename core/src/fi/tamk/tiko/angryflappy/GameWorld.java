package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by Atte Huhtakangas on 2.2.2015 16:31.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class GameWorld {

    // Game Objects
    private Doge doge;
    private Array<Projectile> projectiles;
    private Array<Enemy> enemies;
    private Array<Wow> wows;

    // Assets
    private Music music;
    private Ground ground;
    private boolean drawDebug;

    // Game Logic
    private int enemiesCount;
    private int score;


    public GameWorld() {
        drawDebug = false;
        ground = new Ground();

        // Create input handlers
        InputHandler inputHandler = new InputHandler(this);
        GestureDetector.GestureListener gestureListener = new GestureHandler(this);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(gestureListener));
        inputMultiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Load the music track
        music = Gdx.audio.newMusic(Gdx.files.internal("music/DogeMusic.mp3"));

        init();
    }

    private void init() {

        if (projectiles != null)
            projectiles.clear();

        projectiles = new Array<>();

        if (wows != null)
            wows.clear();
        wows = new Array<>();

        enemiesCount = 5;
        score = 0;

        if (doge != null)
            doge.dispose();
        doge = new Doge(ground);

        spawnBirds();

        music.setLooping(true);
        music.play();
    }

    public void reset() {
        init();
    }

    public void update(float deltaTime) {
        // Update positions etc. for game objects
        if (doge.isAlive()) {
            doge.update(deltaTime);
            for (Enemy enemy : enemies)
                enemy.update(deltaTime);
            for (Enemy enemy : enemies) {
                Projectile projectile = enemy.shoot();
                if (projectile != null)
                    projectiles.add(projectile);
            }
            for (Wow wow : wows)
                wow.update(deltaTime);
            for (Projectile projectile : projectiles)
                projectile.update(deltaTime);

            // Check the collision
            checkCollision();

            // Remove dead objects
            removeDeadObjects();
        }

    }

    private void removeDeadObjects() {
        Iterator<Enemy> eIt = enemies.iterator();
        while (eIt.hasNext()) {
            Enemy enemy = eIt.next();
            if (!enemy.isAlive()) {
                enemy.dispose();
                eIt.remove();
            }
        }

        Iterator<Projectile> pIt = projectiles.iterator();
        while (pIt.hasNext()) {
            Projectile projectile = pIt.next();
            if (!projectile.isAlive()) {
                projectile.dispose();
                pIt.remove();
            }
        }

        Iterator<Wow> wIt = wows.iterator();
        while (wIt.hasNext()) {
            Wow wow = wIt.next();
            if (!wow.isAlive()) {
                wow.dispose();
                wIt.remove();
            }
        }
    }

    private void checkCollision() {
        // check if enemy hits doge
        for (Enemy enemy : enemies) {
            if (enemy.getBounds().overlaps(doge.getBounds())) {
                enemy.die();
                doge.die();
            }
        }

        // check if wow hits bird
        for (Wow wow : wows) {
            for (Enemy enemy : enemies) {
                if (wow.getBounds().overlaps(enemy.getBounds())) {
                    wow.die();
                    enemy.die();
                    score += 100;
                }
            }
        }

        // check if projectile hits doge
        for (Projectile projectile : projectiles) {
            if (projectile.getBounds().overlaps(doge.getBounds())) {
                projectile.die();
                doge.die();
            }
        }
    }

    public void render(SpriteBatch batch) {
        // Draw the doge
        doge.draw(batch);
        // Draw the enemies
        for (Enemy enemy : enemies)
            enemy.draw(batch);
        // Draw the wow projectiles
        for (Wow wow : wows)
            wow.draw(batch);
        // Draw the bird shit projectiles
        for (Projectile projectile : projectiles)
            projectile.draw(batch);
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        ground.renderDebug(shapeRenderer);
        doge.drawDebug(shapeRenderer);

        for (Enemy enemy : enemies)
            enemy.drawDebug(shapeRenderer);
        for (Wow wow : wows)
            wow.drawDebug(shapeRenderer);
        for (Projectile projectile : projectiles)
            projectile.drawDebug(shapeRenderer);

    }

    public void dispose() {
        doge.dispose();
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
        for (Projectile projectile : projectiles) {
            projectile.dispose();
        }
        for (Wow wow : wows) {
            wow.dispose();
        }
        enemies.clear();
        enemies = null;
        projectiles.clear();
        projectiles = null;
        wows.clear();
        wows = null;
        music.dispose();
    }

    public Doge getDoge() {
        return doge;
    }

    public void setDrawDebug() {
        this.drawDebug = !drawDebug;
    }

    public boolean isDrawDebug() {
        return drawDebug;
    }

    public void spawnBirds() {
        if (enemies != null)
            enemies.clear();

        enemies = new Array<>();
        for (int i = 0; i < enemiesCount; i++) {
            enemies.add(new Enemy());
        }
    }

    public void addWow(Wow wow) {
        wows.add(wow);
    }

    public void spawnMoreBirds() {
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy());
        }
    }

    public int getScore() {
        return score;
    }
}

