package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by Atte Huhtakangas on 27.1.2015 23:10.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class Doge extends GameObject {
    private Animation running;
    private Animation shoot;
    private boolean facingLeft;
    private Array<TextureRegion> allFrames;
    private float stateTime;
    private TextureRegion currentFrame;
    private boolean isShooting;
    private int lives;
    private Ground ground;
    private boolean isPlacedLeft;

    private boolean leftMove;
    private boolean rightMove;

    private Array<Wow> wows;
    private boolean isInAir;
    private float gravity;
    public Doge(Ground ground) {
        super();
        allFrames = new Array<TextureRegion>();
        running = setAnimation("dogesheet.png", 10, 2, 1 / 10f, 1, 8);
        shoot = setAnimation("dogesheet2.png", 12, 2, 1 / 12f, 12, 23);
        defaultTextureReg = allFrames.first();
        bounds.set(-Constants.VIEWPORT_WIDTH / 2 + 100, -Constants.VIEWPORT_HEIGHT / 2 + 100, defaultTextureReg.getRegionWidth(), defaultTextureReg.getRegionHeight());
        init();
        this.ground = ground;
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
        wows = new Array<Wow>();
        alive = true;
        isInAir = false;
        gravity = 4f;
        //bounds.set(200f, ground.getRect().y + ground.getRect().height);
        bounds.setX(200f);
        bounds.setY(-Constants.VIEWPORT_HEIGHT / 2 + 30);
        leftMove = false;
        rightMove = false;
        speedPlus = 150f;
    }

    @Override
    public void dispose() {
        defaultTextureReg.getTexture().dispose();
        allFrames.clear();
        allFrames = null;

        for (Wow wow : wows) {
            wow.dispose();
        }

        wows.clear();
        wows = null;
        Gdx.app.debug(getTag(), "disposed");
    }

    private Animation setAnimation(String textureFileName, int frameCols, int frameRows, float animDelay, int framesStart, int framesEnd) {
        Texture textureSheet = new Texture(textureFileName);
        textureSheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        int tileWidth = textureSheet.getWidth() / frameCols;
        int tileHeight = textureSheet.getHeight() / frameRows;
        TextureRegion[][] tmp = TextureRegion.split(textureSheet, tileWidth, tileHeight);

        Array<TextureRegion> frames = Utilities.to1DArray(tmp);

        allFrames.addAll(frames);
        Gdx.app.debug(getTag(), "doge frames count:" + allFrames.size);
        return new Animation(animDelay, Utilities.getFramesRange(frames, framesStart, framesEnd));
    }

    @Override
    public void update(float deltaTime) {

        updateMotion();

        // move to new position
        bounds.x += speed.x * deltaTime;
        bounds.y += speed.y * deltaTime;


        if (isFacingLeft() && !isPlacedLeft) {
            bounds.x -= bounds.width;
            Gdx.app.debug(getTag(), "Moving rect to left");
            isPlacedLeft = true;
        } else if (!isFacingLeft() && isPlacedLeft) {
            isPlacedLeft = false;
            bounds.x += bounds.width;
        }

        // TODO: Handling jumping better!
        if (isInAir) {
            if (bounds.y > ground.getRect().y + ground.getRect().height) {
                bounds.y += speed.y * deltaTime;
                speed.y -= gravity;
            } else {
                Gdx.app.debug(getTag(), "on ground");

                isInAir = false;
                bounds.y = ground.getRect().y + ground.getRect().height;
                speed.y = 0;
            }
        }

        // Reduce speed if not moving
        if (!moving) {
            updateMotionX(deltaTime);
        }

        updateWows(deltaTime);
        checkCollision();
        removeDeadWows();
    }

    private void updateMotion() {
        speed.x = 0;
        if(leftMove) {
            speed.x -= speedPlus;
        }
        if(rightMove) {
            speed.x += speedPlus;
        }
    }

    @Override
    protected void checkCollision() {
        // if hit right wall
        if (bounds.x + bounds.width + speed.x / 4 >= Constants.VIEWPORT_WIDTH / 2) {
            speed.set(-speed.x / 10, speed.y);
        }
        // if hit left wall
        else if (bounds.x + speed.x / 4 <= -Constants.VIEWPORT_WIDTH / 2) {
            speed.set(-speed.x / 10, speed.y);
        }
    }



    /**
     * Draws the doge and the wow projectiles.
     *
     * @param batch
     */
    @Override
    public void draw(SpriteBatch batch) {
        boolean flip = false;

        stateTime += Gdx.graphics.getDeltaTime();

        if (isMoving()) {
            currentFrame = running.getKeyFrame(stateTime, true);
        } else {
            currentFrame = defaultTextureReg;
        }

        if (isFacingLeft())
            flip = true;

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

        drawWows(batch);
    }

    /**
     * Shoot a new wow projectile.
     */
    // TODO: add fling and the velocity variable from it
    public void shoot() {
        isShooting = true;
        float posX = facingLeft ? bounds.x - bounds.width / 2 : bounds.x + bounds.width / 2;
        float posY = bounds.y + bounds.height;

        Wow wow = new Wow(posX, posY);
        wows.add(wow);
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        facingLeft = true;
    }

    @Override
    public void moveRight() {
        super.moveRight();
        facingLeft = false;
    }

    /**
     * Sets the doge to take damage and die when 0 lives left.
     *
     * Overrides the GameObject normal behavior by adding lives.
     */
    @Override
    public void die() {
        if (lives > 0) {
            lives--;
            Gdx.input.vibrate(300);
        } else {
            Gdx.input.vibrate(600);
            super.die();
        }
    }

    /**
     * Jumps the doge.
     *
     * Increases the speed.y and marks that the doge is in air.
     */
    public void jump() {
        speed.y += 150.0f;
        isInAir = true;
    }

    /**
     * Sets the doge to move left.
     *
     * If moving right, override it by setting rightMove to false.
     *
     * @param t whether to move left or not.
     */
    public void setLeftMove(boolean t) {
        if(rightMove && t) rightMove = false;
        leftMove = t;
        facingLeft = t;
    }

    /**
     * Sets the doge to move right.
     *
     * If moving left, override it by setting leftMove to false.
     *
     * @param t whether to move right or not.
     */
    public void setRightMove(boolean t) {
        if(leftMove && t) leftMove = false;
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



    public Array<Wow> getWows() {
        return wows;
    }

    public void stopShooting() {
        isShooting = false;
    }

    public boolean isMoving() {
        return speed.x != 0;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    /**
     * Removes dead wows.
     */
    private void removeDeadWows() {
        Iterator<Wow> it = wows.iterator();
        while(it.hasNext()) {
            Wow wow = it.next();
            if(!wow.isAlive()) {
                wow.dispose();
                it.remove();
            }
        }
    }

    private void updateWows(float deltaTime) {
        for (Wow wow : wows) {
            if (wow != null)
                wow.update(deltaTime);
        }
    }

    /**
     * Draws the wow projectiles.
     *
     * @param batch
     */
    private void drawWows(SpriteBatch batch) {
        for (Wow wow : wows) {
            if (wow != null)
                wow.draw(batch);
        }
    }
}
