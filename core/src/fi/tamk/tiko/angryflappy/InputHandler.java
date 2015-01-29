package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Atte Huhtakangas on 27.1.2015 23:45.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class InputHandler implements InputProcessor {
    public static final String TAG = InputHandler.class.getName();
    private Doge doge;
    private boolean directionKeyPressed;
    private OrthographicCamera camera;
    private Array<Integer> pointers;
    private Vector3 touchpos;

    public InputHandler(Doge doge, OrthographicCamera camera) {
        this.doge = doge;
        directionKeyPressed = false;
        this.camera = camera;
        touchpos = new Vector3();
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
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchpos.set(screenX, screenY, 0);
        touchpos = camera.unproject(touchpos);

        if(touchpos.x <= 0 && touchpos.y < 0) {
            doge.moveLeft();
        } else if(touchpos.x > 0 && touchpos.y < 0) {
            doge.moveRight();
        }

        if(touchpos.y <= 0)
            doge.shoot();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        doge.onStopMoving();
        doge.stopShooting();
        return true;
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

    public Vector3 getTouchpos() {
        return touchpos;
    }
}
