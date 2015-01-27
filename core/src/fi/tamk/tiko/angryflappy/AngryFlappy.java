package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AngryFlappy extends ApplicationAdapter {
    public static final String TAG = AngryFlappy.class.getName();
    private SpriteBatch batch;
    private Animation animation;
    private Animation running;
    private Array<TextureRegion> allFrames;
    private Texture dogesheet;
    private TextureRegion currentFrame;
    private float stateTime;
    private OrthographicCamera camera;
    // some are 2x width and height
    private final int FRAME_COLS = 10;
    private final int FRAME_ROWS = 2;

    private Array<TextureRegion> getFramesRange(Array<TextureRegion> frames, int first, int last) {
        Array<TextureRegion> tmp = new Array<TextureRegion>();
        int counter = 0;
        for(int i = first; i <= last; i++) {
            if(frames.get(i) != null)
                tmp.add(frames.get(i));
            Gdx.app.debug(TAG, "frame added");
        }
        Gdx.app.debug(TAG, counter + " frames added");
        return tmp;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280f, 720f);
        //camera.position.set(0,0,0);
        //camera.update();
        dogesheet = new Texture("dogesheet2.png");
        int tileWidth = dogesheet.getWidth() / FRAME_COLS;
        int tileHeight = dogesheet.getHeight() / FRAME_ROWS;
        TextureRegion[][] tmp = TextureRegion.split(dogesheet, tileWidth, tileHeight);
        allFrames = to1DArray(tmp);
        running = new Animation(1 / 8f, getFramesRange(allFrames, 1, 8));
        stateTime = 0.0f;
        Gdx.app.log(TAG, "WOOOOHOOO IM FUCKING UP THE CODE");
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float deltaTime = Gdx.graphics.getDeltaTime();
        stateTime += deltaTime;
        currentFrame = running.getKeyFrame(stateTime, true);

        batch.begin();
        //batch.draw(dogesheet,0,0);
        batch.draw(currentFrame, 150, 150);
        /*batch.draw(currentFrame.getTexture(),
                0, 0,
                currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2,
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
                1, 1,
                0,
                0, 0,
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
                false, false);*/
        batch.end();
    }

    private Array<TextureRegion> to1DArray(TextureRegion[][] tmp) {
        Array<TextureRegion> array = new Array<TextureRegion>();
        int counter = 0;
        for (int i = 0; i < tmp.length; i++) {

            for (int j = 0; j < tmp[i].length; j++) {

                array.add(tmp[i][j]);
                counter++;
                //Gdx.app.debug(TAG, "frame added");
            }
        }
        Gdx.app.debug(TAG, counter + " frames added");
        return array;
    }

}
