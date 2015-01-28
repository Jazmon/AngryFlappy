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
    private final float SPEED_REDUCTION =4.0f;


    public Doge() {
        Texture dogesheet = new Texture("dogesheet2.png");
        dogesheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int tileWidth = dogesheet.getWidth() / FRAME_COLS;
        int tileHeight = dogesheet.getHeight() / FRAME_ROWS;
        TextureRegion[][] tmp = TextureRegion.split(dogesheet, tileWidth, tileHeight);
        allFrames = Utilities.to1DArray(tmp);
        running = new Animation(1 / 8f, Utilities.getFramesRange(allFrames, 1, 8));
        stateTime = 0.0f;
        bounds = new Rectangle(0,0,dogesheet.getWidth() / FRAME_COLS,dogesheet.getHeight() / FRAME_ROWS );
        speed = new Vector2(0,0);
        stopped = allFrames.first();
        currentFrame = stopped;
        scale = new Vector2(2,2);
        friction = new Vector2(12.0f, 0);
    }

    public void update(float deltaTime) {
        updateMotionX(deltaTime);
        // move to new position
        bounds.x += speed.x * deltaTime;
        bounds.y += speed.y * deltaTime;
    }

    private void updateMotionX(float deltaTime) {
        if(speed.x != 0) {
            // Apply friction
            if(speed.x > 0) {
                speed.x = Math.max(speed.x - friction.x * deltaTime, 0);
            } else {
                speed.x = Math.min(speed.x + friction.x * deltaTime, 0);
            }
        }

        // apply acceleration
        //speed.x += acceleration.
    }

    public void draw(SpriteBatch batch, float deltaTime) {
        stateTime += deltaTime;
        if(isMoving()) {
            currentFrame = running.getKeyFrame(stateTime, true);
            if(isFacingLeft()) {
                currentFrame.flip(true, false);
            } else {
                currentFrame.flip(false, false);
            }
        } else {
            currentFrame = stopped;
        }
       // currentFrame = stopped;
       /* batch.draw(currentFrame.getTexture(),        // texture
                bounds.x, bounds.y,     // x, y
                currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2,  // originX, originY
                bounds.getWidth(), bounds.getHeight(),          // width, height
                scale.x, scale.y,                   // scaleX, scaleY
                0);        */             // rotation

        batch.draw(currentFrame.getTexture(),       // Texture
                bounds.x, bounds.y,                 // x, y
                currentFrame.getTexture().getWidth() / 2, currentFrame.getTexture().getHeight() / 2,    // originX, originY
                bounds.getWidth(), bounds.getHeight(),  // width, height
                scale.x, scale.y,                   // scaleX, scaleY
                0,                                  // rotation
                0, 0,                               // srcX, srcY
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
                isFacingLeft(), false);
    }

   /* public void move(float deltaTime) {
        bounds.x += speed.x * deltaTime;
        bounds.y += speed.y * deltaTime;

        reduceSpeed();

        //currentFrame.setRegion(bounds.x, bounds.y, bounds.width, bounds.height);
        Gdx.app.debug(TAG, "moving");
    }*/

   /* private void reduceSpeed() {
        if (speed.x > 0) {
            speed.x -= SPEED_REDUCTION;
        } else if (speed.x < 0) {
            speed.x += SPEED_REDUCTION;
        }

        if (speed.y > 0) {
            speed.y -= SPEED_REDUCTION;
        } else if (speed.y < 0) {
            speed.y += SPEED_REDUCTION;
        }

        if(speed.x < 2 && speed.x > 0)
            speed.x = 0;

        if(speed.x > - 2 && speed.x < 0)
            speed.x = 0;

        Gdx.app.debug(TAG, "reducing speed");
    }*/

    public boolean isMoving() {
        if (speed.x > 0 || speed.y > 0)
            return true;
        return false;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void moveLeft() {
        speed.x = -20f;
        facingLeft = true;
        Gdx.app.debug(TAG, "moveLeft()");
    }

    public void moveRight() {
        speed.x = 20f;
        facingLeft = false;
        Gdx.app.debug(TAG, "moveRight()");
    }

    public void onStopMoving() {
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public void setSpeed(Vector2 speed) {
        this.speed = speed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
