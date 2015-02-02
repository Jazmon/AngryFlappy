package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

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

    private Array<Wow> wows;

    public boolean isInAir() {
        return isInAir;
    }

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
        // move to new position
        bounds.x += speed.x * deltaTime;
        bounds.y += speed.y * deltaTime;

        if (isFacingLeft() && !isPlacedLeft) {
            bounds.x -= bounds.width;
            Gdx.app.debug(getTag(), "Moving rect to left");
            isPlacedLeft = true;
        } else if(!isFacingLeft() && isPlacedLeft){
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
        //if(bounds.y + speedX > 0)

        if (!moving) {
            updateMotionX(deltaTime);
            //updateMotionY(deltaTime);
        }

        updateWows(deltaTime);
        checkCollision();
        removeDeadWows();
    }

    @Override
    protected void checkCollision() {
        // if hit right wall
        if (bounds.x + bounds.width + speed.x / 4 >= Constants.VIEWPORT_WIDTH / 2) {
            speed.set(0, speed.y);
            // Gdx.app.debug(getTag(), "COLLIDE");
        }
        // if hit left wall
        else if (bounds.x + speed.x / 4 <= -Constants.VIEWPORT_WIDTH / 2) {
            speed.set(0, speed.y);
            // Gdx.app.debug(getTag(), "COLLIDE");
        }

        // if hit ceiling
        /*if (bounds.y + bounds.height + speed.x / 4 >= Constants.VIEWPORT_HEIGHT / 2) {
            speed.set(speed.x, -speed.y);
        } else if (bounds.y + speed.y / 4 <= -Constants.VIEWPORT_HEIGHT / 2) {
            speed.set(speed.x, 0);
        }*/
        // if hit floor
        //if (bounds.y <= (ground.getRect().y + ground.getRect().height)) {
        //   speed.set(speed.x, 0);
        //}
    }

    private void updateWows(float deltaTime) {
        for (Wow wow : wows) {
            if (wow != null)
                wow.update(deltaTime);
        }
    }

    private void drawWows(SpriteBatch batch) {
        for (Wow wow : wows) {
            if (wow != null)
                wow.draw(batch);
        }
    }

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

    public void shoot() {
        isShooting = true;
        float posX = facingLeft ? bounds.x - bounds.width / 2 : bounds.x + bounds.width / 2;
        float posY = bounds.y + bounds.height;

        Wow wow = new Wow(posX, posY);
        wows.add(wow);
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

    private void removeDeadWows() {
        for (int i = 0; i < wows.size; i++) {
            if (!wows.get(i).isAlive()) {
                wows.get(i).dispose();
                wows.removeIndex(i);
            }
        }
    }

    @Override
    public void die() {
        if(lives > 0) {
            lives--;
        } else {
            super.die();
        }
    }

    public int getLives() {
        return lives;
    }

    @Override
    public String getTag() {
        return Doge.class.getName();
    }

    public void jump() {
        speed.y += 150.0f;
        isInAir = true;
    }

    public Array<Wow> getWows() {
        return wows;
    }
}
