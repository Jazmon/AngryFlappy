package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;


public class AngryFlappy extends Game {
    //public static final String TAG = AngryFlappy.class.getName();

    private GameWorld world;
    private GameRenderer renderer;


    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        world = new GameWorld();
        renderer = new GameRenderer(world);

        // Create input handlers
        InputHandler inputHandler = new InputHandler(world, renderer);
        GestureDetector.GestureListener gestureListener = new GestureHandler(world, renderer);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(gestureListener));
        inputMultiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(inputMultiplexer);
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
