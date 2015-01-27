package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Atte Huhtakangas on 27.1.2015 23:45.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class InputHandler implements InputProcessor {
    private Doge doge;
    public InputHandler(Doge doge) {
        this.doge = doge;
    }
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A :
            case Input.Keys.LEFT:

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
