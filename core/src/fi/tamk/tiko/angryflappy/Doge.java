package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Atte Huhtakangas on 27.1.2015 23:10.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class Doge extends GameObject {
    private Animation running;
    private Animation shoot;
    private boolean facingLeft;
    private float stateTime;
    private TextureRegion currentFrame;
    private boolean isShooting;
    private int lives;
    private Ground ground;
    private boolean isPlacedLeft;
    private boolean flip;

    private boolean leftMove;
    private boolean rightMove;

    private boolean isInAir;
    private float gravity;

    public Doge(Ground ground) {
        super();
        running = Utilities.setAnimation("dogesheet.png", 10, 2, 1 / 10f, 1, 8);
        shoot = Utilities.setAnimation("dogesheet2.png", 12, 2, 1 / 12f, 12, 23);
        defaultTextureReg = Utilities.getFrame("dogesheet.png", 10, 2, 0);
        bounds.set(-Constants.VIEWPORT_WIDTH / 2 + 100, -Constants.VIEWPORT_HEIGHT / 2 + 100, defaultTextureReg.getRegionWidth(), defaultTextureReg.getRegionHeight());
        init();
        this.ground = ground;
        flip = false;
    }

    public boolean isInAir() {
        return isInAir;
    }

    @Override
    public void init() {
        lives = 5;
        stateTime = 0.0f;
        isShooting = false;
        speed.set(0, 0);
        isPlacedLeft = false;
        currentFrame = defaultTextureReg;
        scale.set(2.0f, 2.0f);
        friction.set(120.0f, 200.0f);
        alive = true;
        isInAir = false;
        gravity = 4f;
        bounds.setX(200f);
        bounds.setY(-Constants.VIEWPORT_HEIGHT / 2 + 30);
        leftMove = false;
        rightMove = false;
        speedPlus = 300f;
    }

    @Override
    public void dispose() {
        defaultTextureReg.getTexture().dispose();
    }

    @Override
    public void update(float deltaTime) {
        updateMotion();

        // move to new position
        bounds.x += speed.x * deltaTime;
        bounds.y += speed.y * deltaTime;

        // Check if facing left and not turned left
        // if yes, then move the bounds by width
        if (facingLeft && !isPlacedLeft) {
            bounds.x -= bounds.width / 2;
            isPlacedLeft = true;
        }
        // Check if facing right and is placed left
        // if yes then move the bounds by width
        else if (!facingLeft && isPlacedLeft) {
            isPlacedLeft = false;
            bounds.x += bounds.width / 2;
        }

        // TODO: Handling jumping better!
        if (isInAir) {
            if (bounds.y > ground.getRect().y + ground.getRect().height) {
                bounds.y += speed.y * deltaTime;
                speed.y -= gravity;
            } else {
                isInAir = false;
                bounds.y = ground.getRect().y + ground.getRect().height;
                speed.y = 0;
            }
        }

        // Reduce speed if not moving
        if (!leftMove && !rightMove) {
            updateMotionX(deltaTime);
        }

        checkCollision();
    }

    private void updateMotion() {
        speed.x = 0;
        if (leftMove) {
            speed.x -= speedPlus;
        }
        if (rightMove) {
            speed.x += speedPlus;
        }
    }

    @Override
    protected void checkCollision() {
        // if hit right wall
        if (bounds.x + bounds.width / 4 >= Constants.VIEWPORT_WIDTH / 2) {
            bounds.x = Constants.VIEWPORT_WIDTH / 2 - bounds.width;
        }
        // if hit left wall
        else if (bounds.x + bounds.width / 4 <= -Constants.VIEWPORT_WIDTH / 2) {
            bounds.x = -Constants.VIEWPORT_WIDTH / 2 + bounds.width;
        }
    }

    /**
     * Draws the doge and the wow projectiles.
     *
     * @param batch the SpriteBatch which is used to draw.
     */
    @Override
    public void draw(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();


        // If moving get animation for running
        if (leftMove || rightMove) {
            currentFrame = running.getKeyFrame(stateTime, true);
        } else {
            currentFrame = defaultTextureReg;
        }

        // if facing left set to flip the img
        if (leftMove && !flip) {
            flip = true;
        } else if (rightMove && flip) {
            flip = false;
        }

        if (isShooting) {
            currentFrame = shoot.getKeyFrame(stateTime, true);
        }

        batch.draw(currentFrame,
                flip ? bounds.x + 2 * bounds.width : bounds.x, bounds.y,
                bounds.width / 2, bounds.height / 2,
                flip ? -bounds.width : bounds.width, bounds.height,
                scale.x, scale.y,
                0
        );
    }

    /**
     * Shoot a new wow projectile.
     */
    // TODO: add fling and the velocity variable from it
    public Wow shoot() {
        isShooting = true;
        float posX = facingLeft ? bounds.x - bounds.width / 2 : bounds.x + bounds.width / 2;
        float posY = bounds.y + bounds.height;

        return new Wow(posX, posY);
    }

    /**
     * Sets the doge to take damage and die when 0 lives left.
     * <p/>
     * Overrides the GameObject normal behavior by adding lives.
     */
    @Override
    public void die() {
        if (lives > 0) {
            lives--;
            Gdx.input.vibrate(200);
        } else {
            Gdx.input.vibrate(800);
            super.die();
        }
    }

    /**
     * Jumps the doge.
     * <p/>
     * Increases the speed.y and marks that the doge is in air.
     */
    public void jump() {
        speed.y += 150.0f;
        isInAir = true;
    }

    /**
     * Sets the doge to move left.
     * <p/>
     * If moving right, override it by setting rightMove to false.
     *
     * @param t whether to move left or not.
     */
    public void setLeftMove(boolean t) {
        if (rightMove && t) rightMove = false;
        leftMove = t;
        facingLeft = t;
    }

    /**
     * Sets the doge to move right.
     * <p/>
     * If moving left, override it by setting leftMove to false.
     *
     * @param t whether to move right or not.
     */
    public void setRightMove(boolean t) {
        if (leftMove && t) leftMove = false;
        rightMove = t;
        facingLeft = t;
    }

    public int getLives() {
        return lives;
    }

    @Override
    public String getTag() {
        return Doge.class.getName();
    }

    public void stopShooting() {
        isShooting = false;
    }
}
