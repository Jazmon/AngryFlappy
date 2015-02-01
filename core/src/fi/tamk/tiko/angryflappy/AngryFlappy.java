package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;


public class AngryFlappy implements ApplicationListener {
    public static final String TAG = AngryFlappy.class.getName();
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Doge doge;
    private InputHandler inputHandler;
    private BitmapFont font;
    private Texture background;
    private Music music;
    //private Array<Wow> wows;
    //private Enemy enemy;
    private Array<Enemy> enemies;
    private ShapeRenderer shapeRenderer;
    private Ground ground;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        ground = new Ground();
        doge = new Doge(ground);
        inputHandler = new InputHandler(doge, camera);
        Gdx.input.setInputProcessor(inputHandler);
        font = new BitmapFont();
        font.setScale(1.50f, 1.50f);
        font.setColor(Color.BLUE);
        background = new Texture("background.jpg");
        music = Gdx.audio.newMusic(Gdx.files.internal("music/DogeMusic.mp3"));
        music.setLooping(true);
        //music.play();
        enemies = new Array<Enemy>();

        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy());
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    private void checkCollision(){
        // check if enemy hits doge
        for(Enemy enemy : enemies) {
            if(enemy.getBounds().overlaps(doge.getBounds())) {
                //doge.die();
            }
        }

        // check if wow hits bird
        Array<Wow> wows = doge.getWows();
        for(Wow wow : wows) {
            for(Enemy enemy : enemies) {
                if(wow.getBounds().overlaps(enemy.getBounds())) {
                    wow.die();
                    enemy.die();
                }
            }
        }

        // check if projectile hits doge
        for(Enemy enemy : enemies) {
            for(Projectile projectile : enemy.getProjectiles()) {
                if(projectile.getBounds().overlaps(doge.getBounds())) {
                   // doge.die();
                    projectile.die();
                }
            }
        }
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(!doge.isAlive()) {
            batch.begin();
            batch.draw(background, -Constants.VIEWPORT_WIDTH / 2,
                    -Constants.VIEWPORT_HEIGHT / 2,
                    Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
            font.setScale(3.0f, 3.0f);
            font.draw(batch, "GAME OVER", 0,0);
            batch.end();
            return;
        }

        float deltaTime = Gdx.graphics.getDeltaTime();

        // remove dead
        for(int i = 0; i < enemies.size; i++) {
            if(!enemies.get(i).isAlive()) {
                enemies.get(i).dispose();
                enemies.removeIndex(i);
            }
        }

        checkCollision();
        doge.update(deltaTime);

        for(Enemy enemy : enemies)
            enemy.update(deltaTime);

        batch.begin();
        batch.draw(background, -Constants.VIEWPORT_WIDTH / 2,
                -Constants.VIEWPORT_HEIGHT / 2,
                Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT);
        doge.draw(batch);
        //enemy.draw(batch);
        for(Enemy enemy : enemies)
            enemy.draw(batch);

        font.draw(batch, "Speed.x:" + doge.getSpeed().x +
                ", speedY: " + doge.getSpeed().y,
                -Constants.VIEWPORT_WIDTH / 2 + 30,
                -Constants.VIEWPORT_HEIGHT / 2 + 30);
        font.draw(batch, "touchpos.x: " + inputHandler.getTouchpos().x
                + ", y: " + inputHandler.getTouchpos().y,
                Constants.VIEWPORT_WIDTH / 2 - 600,
                Constants.VIEWPORT_HEIGHT / 2 - 30);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(ground.getRect().x, ground.getRect().y,
                ground.getRect().width, ground.getRect().height);
        shapeRenderer.rect(doge.getBounds().x, doge.getBounds().y,
                doge.getBounds().width,doge.getBounds().height);
        shapeRenderer.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        doge.dispose();
        for(Enemy enemy : enemies) {
            enemy.dispose();
        }
        enemies.clear();
        enemies = null;
        background.dispose();
        music.dispose();
        font.dispose();
    }


}
