package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Atte Huhtakangas on 2.2.2015 16:32.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class GestureHandler implements GestureListener {
    private GameWorld world;
    private GameRenderer renderer;

    public GestureHandler(GameWorld world, GameRenderer renderer) {
        this.world = world;
        this.renderer = renderer;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (velocityY > 0) {
            return false;
        }
        if (!world.getDoge().isInAir()) {
            world.getDoge().jump(-velocityY);
            //world.addWow(world.getDoge().shoot(-velocityY));
        }

        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }


}
