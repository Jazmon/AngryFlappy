package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;


public class AngryFlappy implements ApplicationListener {
    public static final String TAG = AngryFlappy.class.getName();

    private GameWorld world;
    private GameRenderer renderer;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        world = new GameWorld();
        renderer = new GameRenderer(world);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        world.update(deltaTime);
        renderer.render();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        renderer.dispose();
        world.dispose();
    }
}
