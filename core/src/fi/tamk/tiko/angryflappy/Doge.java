package fi.tamk.tiko.angryflappy;

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
    private Animation running;
    private TextureRegion stopped;
    private boolean facingLeft;
    private Rectangle bounds;
    private Vector2 speed;
    private final int FRAME_COLS = 10;
    private final int FRAME_ROWS = 2;
    private Array<TextureRegion> allFrames;
    private float stateTime;
    private TextureRegion currentFrame;
    private final float SPEED_REDUCTION = 10.0f;


    public Doge() {
        Texture dogesheet = new Texture("dogesheet2.png");
        int tileWidth = dogesheet.getWidth() / FRAME_COLS;
        int tileHeight = dogesheet.getHeight() / FRAME_ROWS;
        TextureRegion[][] tmp = TextureRegion.split(dogesheet, tileWidth, tileHeight);
        allFrames = Utilities.to1DArray(tmp);
        running = new Animation(1 / 8f, Utilities.getFramesRange(allFrames, 1, 8));
        stateTime = 0.0f;

    }

    public void draw(SpriteBatch batch, float deltaTime) {

        stateTime += deltaTime;
        currentFrame = running.getKeyFrame(stateTime, true);
        batch.draw(currentFrame,
                -Constants.VIEWPORT_WIDTH / 2, -Constants.VIEWPORT_HEIGHT / 2,
                currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2,
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
                1, 1,
                0);
    }

    public void move() {
        bounds.x += speed.x;
        bounds.y += speed.y;

        reduceSpeed();
    }

    private void reduceSpeed() {
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
    }

    public boolean isMoving() {
        if (speed.x > 0 || speed.y > 0)
            return true;
        return false;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }
}
