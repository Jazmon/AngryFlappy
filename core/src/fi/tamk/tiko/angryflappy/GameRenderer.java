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
    private static final String TAG = GameRenderer.class.getName();

    private OrthographicCamera camera;
    //private OrthographicCamera guiCamera;
    private Texture background;
    private SpriteBatch batch;

    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private GameWorld world;

    public GameRenderer(GameWorld world) {
        this.world = world;
        init();
    }

    private void init() {
        font = new BitmapFont();
        font.setScale(1.50f, 1.50f);
        font.setColor(Color.BLUE);
        background = new Texture("background.jpg");

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
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
            font.draw(batch, "GAME OVER", 0, 0);
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
                            ", speedY: " + world.getDoge().getSpeed().y,
                    -Constants.VIEWPORT_WIDTH / 2 + 30,
                    -Constants.VIEWPORT_HEIGHT / 2 + 30);
        }

        font.draw(batch, "lives: " + world.getDoge().getLives(),
                -Constants.VIEWPORT_WIDTH / 2 + 30, Constants.VIEWPORT_HEIGHT / 2 - 30);
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
