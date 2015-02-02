package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Atte Huhtakangas on 31.1.2015 14:52.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class Projectile extends GameObject implements Disposable{
    private float rotation;

    public Projectile(float x, float y) {
        super();
        Texture texture = new Texture("birdshit.png");
        defaultTextureReg = new TextureRegion(texture);
        scale.set(1,1);
        moving = true;
        speed.set(0.0f, MathUtils.random(-320.0f, -380.0f));
        alive = true;
        rotation = 0.0f;
        //Texture explosionSheet = new Texture("explosion.png");
        //explosionFrames = Utilities.
        bounds.set(x, y, defaultTextureReg.getRegionWidth(), defaultTextureReg.getRegionHeight());
    }



    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(defaultTextureReg,
                bounds.x, bounds.y,
                bounds.width / 2, bounds.height / 2,
                bounds.width, bounds.height,
                scale.x, scale.y,
                rotation
        );
    }

    @Override
    public void update(float deltaTime) {
        bounds.x += speed.x * deltaTime;
        bounds.y += speed.y * deltaTime;

        if (!moving) {
            updateMotionX(deltaTime);
            updateMotionY(deltaTime);
        }

        checkCollision();
        rotate();
    }

    private void rotate() {
        rotation += MathUtils.random(3.0f, 7.0f);
    }

    @Override
    protected void checkCollision() {
        if (bounds.x + bounds.width + speed.x / 4 >=
                Constants.VIEWPORT_WIDTH / 2) {
            alive = false;
        } else if (bounds.x + speed.x / 4 <= -Constants.VIEWPORT_WIDTH / 2) {
            alive = false;
        }

        if (bounds.y + bounds.height + speed.x / 4 >=
                Constants.VIEWPORT_HEIGHT / 2) {
            alive = false;
        } else if (bounds.y <= -Constants.VIEWPORT_HEIGHT / 2) {
            alive = false;
        }
    }

    @Override
    public void init() {

    }

    @Override
    public String getTag() {
        return Projectile.class.getName();
    }

    @Override
    public void dispose() {
        defaultTextureReg.getTexture().dispose();
        Gdx.app.debug(getTag(), "disposed");
    }
}
