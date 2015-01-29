package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Atte Huhtakangas on 27.1.2015 23:10.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public abstract class GameObject {
    protected Vector2 speed;
    protected Vector2 scale;
    protected Vector2 friction;
    protected boolean moving;
    protected float speedPlus;
    protected TextureRegion defaultTextureReg;
    protected Rectangle bounds;


    public GameObject() {
        speed = new Vector2();
        scale = new Vector2(1,1);
        friction = new Vector2();
        moving = false;
        speedPlus = 100.0f;
        bounds = new Rectangle();
        defaultTextureReg = null;
    }

    public abstract void draw(SpriteBatch batch);
    public abstract void update(float deltaTime);
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
    protected void updateMotionY(float deltaTime){
        if (speed.y != 0) {
            // Apply friction
            if (speed.y > 0) {
                speed.y = Math.max(speed.y - friction.y * deltaTime, 0);
            } else {
                speed.y = Math.min(speed.y + friction.y * deltaTime, 0);
            }
        }
    }


    public void moveRight() {
        speed.x = speedPlus;
        moving = true;
    }

    public void moveLeft() {
        speed.x = -speedPlus;
        moving = true;
    }

    public void onStopMoving() {
        moving = false;
    }

    public Vector2 getSpeed() {
        return speed;
    }

    abstract public String getTag();
}
