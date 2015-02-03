package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Atte Huhtakangas on 30.1.2015 22:13.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class Enemy extends GameObject implements Disposable {
    private float rotation;
    private long prevShotTime;
    private boolean exploded;
    private Animation explosion;
    private float stateTime;
    private TextureRegion currentFrame;
    private boolean flipped;

    public Enemy() {
        super();

        Texture texture = new Texture("flappybird.png");
        defaultTextureReg = new TextureRegion(texture);
        currentFrame = defaultTextureReg;
        explosion = Utilities.setAnimation("explosion.png", 5, 5, 1 / 25.0f);
        explosion.setPlayMode(Animation.PlayMode.LOOP);

        init();
    }

    @Override
    public void init() {
        float x = MathUtils.random(-Constants.VIEWPORT_WIDTH / 2 + 20, -Constants.VIEWPORT_WIDTH / 2 + 300);
        float y = MathUtils.random(Constants.VIEWPORT_HEIGHT / 2 - 50, Constants.VIEWPORT_HEIGHT / 2 - 120);
        bounds.set(x, y, defaultTextureReg.getRegionWidth(), defaultTextureReg.getRegionHeight());
        exploded = false;
        stateTime = 0f;
        prevShotTime = 0;
        moving = true;
        scale.set(4.0f, 4.0f);
        speed.set(-120f, -10f);
        rotation = 0f;
        flipped = false;
    }

    @Override
    public void die() {
        if (!exploded) {
            exploded = true;
        } else {
            super.die();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (exploded) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = explosion.getKeyFrame(stateTime, false);
            if (explosion.isAnimationFinished(stateTime)) {
                die();
            }
        }

        boolean flip = speed.x < 0;

        batch.draw(currentFrame,
                flip ? bounds.x + bounds.width : bounds.x, bounds.y,
                bounds.width / 2, bounds.height / 2,
                flip ? -bounds.width : bounds.width, bounds.height,
                scale.x, scale.y,
                rotation
        );
    }


    @Override
    public void update(float deltaTime) {
        bounds.x += speed.x * deltaTime;
        bounds.y += speed.y * deltaTime;

        boolean flip = speed.x < 0;

        if (flip && !flipped) {
            bounds.x -= bounds.width / 2;
            flipped = true;
        } else if (!flip && flipped) {
            bounds.x += bounds.width / 2;
            flipped = false;
        }

        if (!moving) {
            updateMotionX(deltaTime);
            updateMotionY(deltaTime);
        }

        checkCollision();
    }


    public Projectile shoot() {
        Projectile projectile = null;
        if (TimeUtils.timeSinceMillis(prevShotTime) >= MathUtils.random(2500f, 5000f)) {
            prevShotTime = TimeUtils.millis();
            projectile = new Projectile(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
        }

        return projectile;
    }

    @Override
    protected void checkCollision() {
        if (bounds.x + bounds.width + speed.x / 4 >= Constants.VIEWPORT_WIDTH / 2) {
            bounds.x = Constants.VIEWPORT_WIDTH / 2 - bounds.width;
            speed.set(-speed.x, speed.y);
        } else if (bounds.x + speed.x / 4 <= -Constants.VIEWPORT_WIDTH / 2) {
            bounds.x = -Constants.VIEWPORT_WIDTH / 2 + bounds.width;
            speed.set(-speed.x, speed.y);
        }

        if (bounds.y + bounds.height + speed.x / 4 >= Constants.VIEWPORT_HEIGHT / 2) {
            bounds.y = Constants.VIEWPORT_HEIGHT / 2 - bounds.height;
            speed.set(speed.x, -speed.y);
        } else if (bounds.y + speed.y / 4 <= -Constants.VIEWPORT_HEIGHT / 2) {
            //speed.set(speed.x, -speed.y);
            die();
        }
    }

    @Override
    public String getTag() {
        return Enemy.class.getName();
    }

    @Override
    public void dispose() {
        defaultTextureReg.getTexture().dispose();
    }
}
