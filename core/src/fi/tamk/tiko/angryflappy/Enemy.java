package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
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
    private Array<TextureRegion> explosionFrames;
    private Animation explosion;
    private float stateTime;
    private TextureRegion currentFrame;


    public Array<Projectile> getProjectiles() {
        return projectiles;
    }

    private Array<Projectile> projectiles;

    public Enemy() {
        super();
        moving = true;
        Texture texture = new Texture("flappybird.png");
        defaultTextureReg = new TextureRegion(texture);
        float x = MathUtils.random(-Constants.VIEWPORT_WIDTH / 2 + 20, -Constants.VIEWPORT_WIDTH / 2 + 300);
        float y = MathUtils.random(Constants.VIEWPORT_HEIGHT / 2 - 30, Constants.VIEWPORT_HEIGHT / 2 - 100);
        speed.set(-120f, -10f);
        rotation = 0f;
        scale.set(4.0f, 4.0f);
        bounds.set(x, y, defaultTextureReg.getRegionWidth(), defaultTextureReg.getRegionHeight());
        prevShotTime = 0;
        projectiles = new Array<Projectile>();

        stateTime = 0f;
        explosionFrames = new Array<TextureRegion>();
        currentFrame = defaultTextureReg;
        exploded = false;
        explosion = setAnimation("explosion.png", 5, 5,1 / 25.0f, 0, 24);
        explosion.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void die() {
        if(!exploded) {
            exploded = true;
            Gdx.app.debug(getTag(), "setting exploded to true");
        } else {
            Gdx.app.debug(getTag(), "finally calling super.die()");
            super.die();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(exploded) {
            stateTime += Gdx.graphics.getDeltaTime();
            Gdx.app.debug(getTag(), "Is exploded,setting to expl. frames");
            currentFrame = explosion.getKeyFrame(stateTime, false);
            if(explosion.isAnimationFinished(stateTime)) {
                Gdx.app.debug(getTag(), "animation finished, removing");
                die();
            }
        }

        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width / 2, bounds.height / 2, bounds.width, bounds.height, scale.x, scale.y, rotation);
        for (Projectile projectile : projectiles)
            projectile.draw(batch);
    }

    @Override
    public void update(float deltaTime) {
        bounds.x += speed.x * deltaTime;
        bounds.y += speed.y * deltaTime;

        if (!moving) {
            updateMotionX(deltaTime);
            updateMotionY(deltaTime);
        }

        for (Projectile projectile : projectiles)
            projectile.update(deltaTime);

        checkCollision();
        rotate();
        shoot();

        removeDeadProjectiles();
    }

    private Animation setAnimation(String textureFileName, int frameCols, int frameRows, float animDelay, int framesStart, int framesEnd) {
        Texture textureSheet = new Texture(textureFileName);
        textureSheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        int tileWidth = textureSheet.getWidth() / frameCols;
        int tileHeight = textureSheet.getHeight() / frameRows;
        TextureRegion[][] tmp = TextureRegion.split(textureSheet, tileWidth, tileHeight);

        Array<TextureRegion> frames = Utilities.to1DArray(tmp);

        explosionFrames.addAll(frames);
        Gdx.app.debug(getTag(), "bird explosion frames count:" + explosionFrames.size);
        return new Animation(animDelay, Utilities.getFramesRange(frames, framesStart, framesEnd));
    }

    private void removeDeadProjectiles() {
        for (int i = 0; i < projectiles.size; i++) {
            if(!projectiles.get(i).isAlive()) {
                projectiles.get(i).dispose();
                projectiles.removeIndex(i);
            }
        }
    }

    private void rotate() {
        //rotation += 1.0f;
    }

    @Override
    public void init() {

    }

    private void shoot() {
        //Gdx.app.debug(getTag(), "dT: " +TimeUtils.timeSinceMillis(TimeUtils.millis() - prevShotTime));
        if (TimeUtils.timeSinceMillis(prevShotTime) >= MathUtils.random(2500f,4000f)) {
            prevShotTime = TimeUtils.millis();
            projectiles.add(new Projectile(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2));
        }
    }

    @Override
    protected void checkCollision() {
        if (bounds.x + bounds.width + speed.x / 4 >= Constants.VIEWPORT_WIDTH / 2) {
            speed.set(-speed.x, speed.y);
            // Gdx.app.debug(getTag(), "COLLIDE");
        } else if (bounds.x + speed.x / 4 <= -Constants.VIEWPORT_WIDTH / 2) {
            speed.set(-speed.x, speed.y);
            // Gdx.app.debug(getTag(), "COLLIDE");
        }

        if (bounds.y + bounds.height + speed.x / 4 >= Constants.VIEWPORT_HEIGHT / 2) {
            speed.set(speed.x, -speed.y);
        } else if (bounds.y + speed.y / 4 <= -Constants.VIEWPORT_HEIGHT / 2) {
            speed.set(speed.x, -speed.y);
        }
    }

    @Override
    public String getTag() {
        return Enemy.class.getName();
    }

    @Override
    public void dispose() {
        for(Projectile projectile : projectiles) {
            projectile.dispose();
        }

        defaultTextureReg.getTexture().dispose();

        projectiles.clear();
        projectiles = null;
        Gdx.app.debug(getTag(), "disposed");
    }
}
