package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Atte Huhtakangas on 29.1.2015 21:09.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */

// TODO: projectile speed and arch dependant on fling gesture's distance
public class Wow extends GameObject implements Pool.Poolable {
    private final Vector2 scaleMax = new Vector2(2.0f, 2.0f);
    private final Vector2 scaleMin = new Vector2(0.9f, 0.9f);
    private boolean goBigger;

    public Wow(float x, float y) {
        super();
        defaultTextureReg = new TextureRegion(new Texture("wow.png"));
        bounds.set(x, y, defaultTextureReg.getRegionWidth(), defaultTextureReg.getRegionHeight());

        init();
    }

    @Override
    public void init() {
        speed.set(0, 80f);
        goBigger = true;
        scale.set(1, 1);
        alive = true;
    }

    @Override
    public void dispose() {
        defaultTextureReg.getTexture().dispose();
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(defaultTextureReg, bounds.x, bounds.y,
                bounds.width / 2, bounds.height / 2,
                bounds.width, bounds.height,
                scale.x, scale.y,
                0
        );
    }

    @Override
    protected void checkCollision() {
        if (bounds.x + bounds.width + speed.x / 4 >= Constants.VIEWPORT_WIDTH / 2) {
            speed.set(-speed.x, speed.y);
        } else if (bounds.x + speed.x / 4 <= -Constants.VIEWPORT_WIDTH / 2) {
            speed.set(-speed.x, speed.y);
        }

        if (bounds.y + bounds.height + speed.x / 4 >= Constants.VIEWPORT_HEIGHT / 2) {
            alive = false;
        } else if (bounds.y + speed.y / 4 <= -Constants.VIEWPORT_HEIGHT / 2) {
            speed.set(speed.x, -speed.y);
        }
    }

    @Override
    public void update(float deltaTime) {
        bounds.x += speed.x * deltaTime;
        bounds.y += speed.y * deltaTime;

        if (!moving) {
            updateMotionX(deltaTime);
            updateMotionY(deltaTime);
        }

        pulse();
        checkCollision();
    }

    private void pulse() {
        float change = 0.05f;
        if (goBigger) {
            scale.add(change, change);
        } else {
            scale.add(-change, -change);
        }

        if (scale.x >= scaleMax.x) {
            goBigger = false;
        } else if (scale.x <= scaleMin.x) {
            goBigger = true;
        }
    }

    public void setSpeedMultiplier(float velocity) {
        speed.x += speed.x * (velocity / 1000);
    }

    @Override
    public String getTag() {
        return Wow.class.getName();
    }

    @Override
    public void reset() {
        init();
    }
}
