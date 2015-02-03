package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * Created by Atte Huhtakangas on 3.2.2015 18:03.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public abstract class AbstractGameScreen implements Screen {
    protected Game game;

    public AbstractGameScreen(Game game) {
        this.game = game;
    }

    @Override
    public abstract void show();

    @Override
    public abstract void render(float deltaTime);

    @Override
    public abstract void resize(int width, int height);

    @Override
    public abstract void pause();

    @Override
    public abstract void resume();

    @Override
    public abstract void hide();

    @Override
    public abstract void dispose();
}
