package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Atte Huhtakangas on 1.2.2015 16:27.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class Ground {
    public Rectangle getRect() {
        return rect;
    }

    private Rectangle rect;

    public Ground() {
        rect = new Rectangle(-Constants.VIEWPORT_WIDTH / 2, -Constants.VIEWPORT_HEIGHT / 2, Constants.VIEWPORT_WIDTH, 40);



    }
}
