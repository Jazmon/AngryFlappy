package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Atte Huhtakangas on 2.2.2015 16:31.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class GameWorld {
    private Doge doge;
    private InputHandler inputHandler;
    private Music music;
    private Array<Enemy> enemies;
    private Ground ground;
    private GestureDetector.GestureListener gestureListener;
    private InputMultiplexer inputMultiplexer;
    private int enemiesCount;
    private boolean drawDebug;

    public GameWorld() {
        ground = new Ground();
        inputHandler = new InputHandler(this);
        gestureListener = new GestureHandler(this);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(gestureListener));
        inputMultiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(inputMultiplexer);
        drawDebug = false;

        music = Gdx.audio.newMusic(Gdx.files.internal("music/DogeMusic.mp3"));

        init();
    }

    private void init() {
        Gdx.input.setInputProcessor(inputMultiplexer);

        enemiesCount = 5;
        doge = new Doge(ground);
        spawnBirds();
        music.setLooping(true);
        music.play();
    }

    public void reset() {
        init();
    }

    public void update(float deltaTime) {

        // remove dead
        for (int i = 0; i < enemies.size; i++) {
            if (!enemies.get(i).isAlive()) {
                enemies.get(i).dispose();
                enemies.removeIndex(i);
            }
        }

        checkCollision();
        doge.update(deltaTime);

        for (Enemy enemy : enemies)
            enemy.update(deltaTime);
    }

    private void checkCollision() {
        // check if enemy hits doge
        for (Enemy enemy : enemies) {
            if (enemy.getBounds().overlaps(doge.getBounds())) {
                doge.die();
            }
        }

        // check if wow hits bird
        Array<Wow> wows = doge.getWows();
        for (Wow wow : wows) {
            for (Enemy enemy : enemies) {
                if (wow.getBounds().overlaps(enemy.getBounds())) {
                    wow.die();
                    enemy.die();
                }
            }
        }

        // check if projectile hits doge
        for (Enemy enemy : enemies) {
            for (Projectile projectile : enemy.getProjectiles()) {
                if (projectile.getBounds().overlaps(doge.getBounds())) {
                    doge.die();
                    projectile.die();
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        doge.draw(batch);
        for (Enemy enemy : enemies)
            enemy.draw(batch);

    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(ground.getRect().x, ground.getRect().y,
                ground.getRect().width, ground.getRect().height);
        shapeRenderer.rect(doge.getBounds().x, doge.getBounds().y,
                doge.getBounds().width, doge.getBounds().height);
    }

    public void dispose() {
        doge.dispose();
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
        enemies.clear();
        enemies = null;
        music.dispose();
    }

    public Doge getDoge() {
        return doge;
    }

    public void setDrawDebug() {
        this.drawDebug = drawDebug ? false : true;
    }

    public boolean isDrawDebug() {
        return drawDebug;
    }

    public void spawnBirds() {
        enemies = new Array<Enemy>();
        for (int i = 0; i < enemiesCount; i++) {
            enemies.add(new Enemy());
        }
    }

    public void spawnMoreBirds() {
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy());
        }
    }
}

