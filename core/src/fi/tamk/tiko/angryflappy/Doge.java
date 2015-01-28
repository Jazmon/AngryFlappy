package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Atte Huhtakangas on 27.1.2015 23:10.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class Doge {
    public static final String TAG = Doge.class.getName();
    private Animation running;
    private TextureRegion stopped;
    private boolean facingLeft;
    private Rectangle bounds;
    private Vector2 speed;
    private Vector2 scale;
    private Vector2 friction;
    private final int FRAME_COLS = 10;
    private final int FRAME_ROWS = 2;
    private Array<TextureRegion> allFrames;
    private float stateTime;
    private TextureRegion currentFrame;
    private final float SPEED_PLUS = 100.0f;
    private boolean moving;


    public Doge() {
        Texture dogesheet = new Texture("dogesheet2.png");
        dogesheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int tileWidth = dogesheet.getWidth() / FRAME_COLS;
        int tileHeight = dogesheet.getHeight() / FRAME_ROWS;
        TextureRegion[][] tmp = TextureRegion.split(dogesheet, tileWidth, tileHeight);
        allFrames = Utilities.to1DArray(tmp);
        running = new Animation(1 / 8f, Utilities.getFramesRange(allFrames, 1, 8));
        stateTime = 0.0f;
        bounds = new Rectangle(0, 0, dogesheet.getWidth() / FRAME_COLS, dogesheet.getHeight() / FRAME_ROWS);
        speed = new Vector2(0, 0);
        stopped = allFrames.first();
        currentFrame = stopped;
        scale = new Vector2(2, 2);
        friction = new Vector2(120.0f, 0);
        moving = false;
    }

    public void update(float deltaTime) {
        // move to new position
        bounds.x += speed.x * deltaTime;
        bounds.y += speed.y * deltaTime;

        if (!moving) {
            updateMotionX(deltaTime);
        }
    }

    private void updateMotionX(float deltaTime) {
        if (speed.x != 0) {
            // Apply friction
            if (speed.x > 0) {
                speed.x = Math.max(speed.x - friction.x * deltaTime, 0);
            } else {
                speed.x = Math.min(speed.x + friction.x * deltaTime, 0);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        boolean flip = false;

        if (isMoving()) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = running.getKeyFrame(stateTime, true);
        } else {
            currentFrame = stopped;
        }

        if (isFacingLeft())
            flip = true;

        batch.draw(currentFrame,
                flip ? bounds.x + bounds.width : bounds.x, bounds.y,
                bounds.width / 2, bounds.height / 2,
                flip ? -bounds.width : bounds.width, bounds.height,
                scale.x, scale.y,
                0
        );
    }

    public boolean isMoving() {
        return speed.x != 0;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void moveLeft() {
        speed.x = -SPEED_PLUS;
        facingLeft = true;
        moving = true;
        Gdx.app.debug(TAG, "moveLeft()");
    }

    public void moveRight() {
        speed.x = SPEED_PLUS;
        facingLeft = false;
        moving = true;
        Gdx.app.debug(TAG, "moveRight()");
    }

    public void onStopMoving() {
        moving = false;
    }

    public Vector2 getSpeed() {
        return speed;
    }
}
