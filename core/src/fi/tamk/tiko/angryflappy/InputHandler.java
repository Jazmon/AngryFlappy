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

// TODO: GestureHandler for flinging
public class InputHandler implements InputProcessor {
    public static final String TAG = InputHandler.class.getName();
    private Doge doge;
    private boolean directionKeyPressed;
    private OrthographicCamera camera;
    private Array<TouchAction> pointers;
    private Vector3 touchpos;

    public enum Move {
        Left, Right, Jump, Shoot, JumpAndShoot
    }

    private class TouchAction {
        public TouchAction(int pointer, Move move) {
            this.pointer = pointer;
            this.move = move;
        }
        public int pointer;
        public Move move;

    }

    public InputHandler(Doge doge, OrthographicCamera camera) {
        this.doge = doge;
        directionKeyPressed = false;
        this.camera = camera;
        touchpos = new Vector3();
        pointers = new Array<TouchAction>();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                /*if (!directionKeyPressed) {
                    directionKeyPressed = true;
                    doge.moveLeft();
                    //Gdx.app.debug(TAG, "LEFT");
                }*/
                doge.setLeftMove(true);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                /*if (!directionKeyPressed) {
                    directionKeyPressed = true;
                    doge.moveRight();
                    //Gdx.app.debug(TAG, "RIGHT");
                }*/
                doge.setRightMove(true);
                break;
            case Input.Keys.SPACE:
                doge.shoot();
                if(!doge.isInAir()) {
                    doge.jump();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                //directionKeyPressed = false;
                //doge.onStopMoving();
                // Gdx.app.debug(TAG, "stop LEFT");
                doge.setLeftMove(false);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                //directionKeyPressed = false;
                //doge.onStopMoving();
                //Gdx.app.debug(TAG, "stop RIGHT");
                doge.setRightMove(false);
                break;
            case Input.Keys.SPACE:
                doge.stopShooting();
                break;
            case Input.Keys.ESCAPE:
                System.gc();
                Gdx.app.exit();
                break;
            case Input.Keys.R :
                //gameWorld.reset();
                break;
            case Input.Keys.O:
                //gameWorld.showDebug();
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

        if (touchpos.x <= 0 && touchpos.y < 0) {
            //doge.moveLeft();
            doge.setLeftMove(true);
            pointers.add(new TouchAction(pointer, Move.Left));
        } else if (touchpos.x > 0 && touchpos.y < 0) {
            //doge.moveRight();
            doge.setRightMove(true);
            pointers.add(new TouchAction(pointer, Move.Right));
        }

        if (touchpos.y > 0) {
            doge.shoot();
            pointers.add(new TouchAction(pointer, Move.JumpAndShoot));
            if (!doge.isInAir())
                doge.jump();
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for(TouchAction touchAction : pointers) {
            if(touchAction.pointer == pointer) {
                switch (touchAction.move) {
                    case Left:
                        doge.setLeftMove(false);
                        break;
                    case Right:
                        doge.setRightMove(false);
                        break;
                    case JumpAndShoot:
                        doge.stopShooting();
                        break;
                }
            }
        }

        //doge.onStopMoving();
        //doge.stopShooting();
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
