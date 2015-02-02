package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Atte Huhtakangas on 2.2.2015 16:31.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class GameRenderer implements Disposable {
    private OrthographicCamera camera;
    private OrthographicCamera guiCamera;

    private SpriteBatch batch;
    private GameWorld gameWorld;

    public GameRenderer() {

    }

    @Override
    public void dispose() {

    }
}
