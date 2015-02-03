package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Atte Huhtakangas on 1.2.2015 16:27.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class Ground {
    private Rectangle rect;

    public Ground() {
        rect = new Rectangle(-Constants.VIEWPORT_WIDTH / 2, -Constants.VIEWPORT_HEIGHT / 2, Constants.VIEWPORT_WIDTH, 40);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(rect.x, rect.y,
                rect.width, rect.height);
    }

}
