package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Atte Huhtakangas on 2.2.2015 16:31.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class GameRenderer implements Disposable {
    //private static final String TAG = GameRenderer.class.getName();

    private OrthographicCamera camera;
    //private OrthographicCamera guiCamera;
    private Texture background;
    private SpriteBatch batch;

    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private GameWorld world;
    private float velocityDebug;

    public GameRenderer(GameWorld world) {
        this.world = world;
        background = new Texture("background.jpg");
        font = new BitmapFont();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        init();
        velocityDebug = 0;
    }

    public void init() {
        font.setScale(1.50f, 1.50f);
        font.setColor(Color.WHITE);
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();

    }

    public void render() {
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!world.getDoge().isAlive()) {
            batch.begin();
            batch.draw(background, -Constants.VIEWPORT_WIDTH / 2,
                    -Constants.VIEWPORT_HEIGHT / 2,
                    Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
            font.setScale(3.0f, 3.0f);
            font.setColor(217 / 255.0f, 0 / 255.0f, 255 / 255.0f, 255 / 255.0f);
            font.draw(batch, "GAME OVER", -10, -30);
            font.draw(batch, "SCORE: " + world.getScore(), -10, -80);
            font.setColor(Color.WHITE);
            batch.end();
            return;
        }

        batch.begin();
        batch.draw(background, -Constants.VIEWPORT_WIDTH / 2,
                -Constants.VIEWPORT_HEIGHT / 2,
                Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT);

        world.render(batch);

        if (world.isDrawDebug()) {
            font.draw(batch, "Speed.x:" + world.getDoge().getSpeed().x +
                            ", Speed.y: " + world.getDoge().getSpeed().y,
                    -Constants.VIEWPORT_WIDTH / 2 + 10,
                    -Constants.VIEWPORT_HEIGHT / 2 + 30);
            font.draw(batch, "FPS:" + Gdx.graphics.getFramesPerSecond(), Constants.VIEWPORT_WIDTH / 2 - 100, Constants.VIEWPORT_HEIGHT / 2 - 30);
        }
        font.draw(batch, "Lives: " + world.getDoge().getLives(),
                -Constants.VIEWPORT_WIDTH / 2 + 30, Constants.VIEWPORT_HEIGHT / 2 - 30);
        font.draw(batch, "Velocity.y: " + velocityDebug, Constants.VIEWPORT_WIDTH / 2 - 200, -Constants.VIEWPORT_HEIGHT / 2 + 30);
        batch.end();

        if (world.isDrawDebug()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            world.renderDebug(shapeRenderer);
            shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        font.dispose();
    }
}
