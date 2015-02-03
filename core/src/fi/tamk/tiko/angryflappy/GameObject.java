package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Atte Huhtakangas on 27.1.2015 23:10.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public abstract class GameObject implements Disposable {
    protected Vector2 speed;
    protected Vector2 scale;
    protected Vector2 friction;
    protected boolean moving;
    protected float speedPlus;
    protected TextureRegion defaultTextureReg;
    protected boolean alive;
    protected Rectangle bounds;
    protected float rotation;

    public GameObject() {
        speed = new Vector2();
        scale = new Vector2(1, 1);
        friction = new Vector2();
        rotation = 0;
        moving = false;
        speedPlus = 100.0f;
        bounds = new Rectangle();
        defaultTextureReg = null;
        alive = true;
    }

    public abstract void draw(SpriteBatch batch);

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(bounds.x, bounds.y,
                bounds.width / 2, bounds.height / 2,
                bounds.width, bounds.height,
                scale.x, scale.y,
                0
        );
    }

    public abstract void update(float deltaTime);

    public abstract void init();

    protected void updateMotionX(float deltaTime) {
        if (speed.x != 0) {
            // Apply friction
            if (speed.x > 0) {
                speed.x = Math.max(speed.x - friction.x * deltaTime, 0);
            } else {
                speed.x = Math.min(speed.x + friction.x * deltaTime, 0);
            }
        }
    }

    protected void updateMotionY(float deltaTime) {
        if (speed.y != 0) {
            // Apply friction
            if (speed.y > 0) {
                speed.y = Math.max(speed.y - friction.y * deltaTime, 0);
            } else {
                speed.y = Math.min(speed.y + friction.y * deltaTime, 0);
            }
        }
    }

    protected abstract void checkCollision();

    public void die() {
        alive = false;
    }

    abstract public String getTag();

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public boolean isAlive() {
        return alive;
    }
}
