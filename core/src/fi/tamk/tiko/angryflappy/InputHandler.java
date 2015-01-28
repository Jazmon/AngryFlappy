package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Atte Huhtakangas on 27.1.2015 23:45.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class InputHandler implements InputProcessor {
    public static final String TAG = InputHandler.class.getName();
    private Doge doge;
    private boolean directionKeyPressed;

    public InputHandler(Doge doge) {
        this.doge = doge;
        directionKeyPressed = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                if (!directionKeyPressed) {
                    directionKeyPressed = true;
                    doge.moveLeft();
                    Gdx.app.debug(TAG, "LEFT");
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                if (!directionKeyPressed) {
                    directionKeyPressed = true;
                    doge.moveRight();
                    Gdx.app.debug(TAG, "RIGHT");
                }
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                directionKeyPressed = false;
                doge.onStopMoving();
                Gdx.app.debug(TAG, "stop LEFT");

                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                directionKeyPressed = false;
                doge.onStopMoving();
                Gdx.app.debug(TAG, "stop RIGHT");
                break;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
