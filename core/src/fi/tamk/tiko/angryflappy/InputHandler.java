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
    //private final int VIBRATION_TIME = 50;
    private final long[] VIBRATION_PATTERN = {0, 33, 33};
    //public static final String TAG = InputHandler.class.getName();
    private OrthographicCamera camera;
    private Array<TouchAction> pointers;
    private Vector3 touchpos;
    private GameWorld world;
    private GameRenderer renderer;


    public InputHandler(GameWorld world, GameRenderer renderer) {
        this.world = world;
        this.renderer = renderer;
        this.camera = new OrthographicCamera();

        touchpos = new Vector3();
        pointers = new Array<>();

        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                world.getDoge().setLeftMove(true);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                world.getDoge().setRightMove(true);
                break;
            case Input.Keys.SPACE:
                if (!world.getDoge().isInAir()) {
                    world.getDoge().jump();
                    world.addWow(world.getDoge().shoot());
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
                world.getDoge().setLeftMove(false);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                world.getDoge().setRightMove(false);
                break;
            case Input.Keys.SPACE:
                world.getDoge().stopShooting();
                break;
            // Exit the game
            case Input.Keys.ESCAPE:
                System.gc();
                Gdx.app.exit();
                break;
            // Reset the world
            case Input.Keys.R:
                world.reset();
                break;
            // Toggle drawing debug
            case Input.Keys.O:
                world.setDrawDebug();
                break;
            // Spawn more birds
            case Input.Keys.H:
                world.spawnMoreBirdsDebug();
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
        //Gdx.input.vibrate(VIBRATION_TIME);
        Gdx.input.vibrate(VIBRATION_PATTERN, -1);
        if (touchpos.x <= 0 && touchpos.y < 0) {
            world.getDoge().setLeftMove(true);
            pointers.add(new TouchAction(pointer, Move.Left));

        } else if (touchpos.x > 0 && touchpos.y < 0) {
            world.getDoge().setRightMove(true);
            pointers.add(new TouchAction(pointer, Move.Right));
        }

        if (touchpos.y > 0) {
            pointers.add(new TouchAction(pointer, Move.JumpAndShoot));
            if (!world.getDoge().isInAir()) {
                //world.getDoge().jump();
                world.addWow(world.getDoge().shoot());
            }
        }

        if (!world.getDoge().isAlive()) {
            world.reset();
            renderer.init();
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (TouchAction touchAction : pointers) {
            if (touchAction.pointer == pointer) {
                switch (touchAction.move) {
                    case Left:
                        world.getDoge().setLeftMove(false);
                        break;
                    case Right:
                        world.getDoge().setRightMove(false);
                        break;
                    case JumpAndShoot:
                        world.getDoge().stopShooting();
                        break;
                }
            }
        }
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

    public enum Move {
        Left, Right, Jump, Shoot, JumpAndShoot
    }

    private class TouchAction {
        public int pointer;
        public Move move;

        public TouchAction(int pointer, Move move) {
            this.pointer = pointer;
            this.move = move;
        }
    }
}
