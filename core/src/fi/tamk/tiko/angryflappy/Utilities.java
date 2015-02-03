package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Atte Huhtakangas on 27.1.2015 21:34.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class Utilities {
    /**
     * Class tag - Used for debugging purposes.
     */
    private static final String TAG = Utilities.class.getName();

    /**
     * Converts a 2D array to a {@link com.badlogic.gdx.utils.Array}.
     *
     * @param array source 2d array
     * @return the converted {@link com.badlogic.gdx.utils.Array}.
     */
    public static Array<TextureRegion> to1DArray(TextureRegion[][] array) {
        Array<TextureRegion> newArray = new Array<>();

        for (TextureRegion[] trArray : array) {
            for (TextureRegion tr : trArray) {
                newArray.add(tr);
            }
        }

        return newArray;
    }

    /**
     * Returns part of an {@link com.badlogic.gdx.utils.Array}.
     *
     * @param frames source array.
     * @param first  first member to include.
     * @param last   last member to include.
     * @return finished array.
     */
    public static Array<TextureRegion> getFramesRange(Array<TextureRegion> frames, int first, int last) {
        Array<TextureRegion> tmp = new Array<>();
        int counter = 0;

        for (int i = first; i <= last; i++) {
            if (frames.get(i) != null) {
                tmp.add(frames.get(i));
                counter++;
            }
        }

        Gdx.app.debug(TAG, "Got " + counter + "/" + (last - first + 1) + " frames");
        return tmp;
    }

    /**
     * Creates an animation from a sprite sheet using a range.
     *
     * @param textureFileName name of the texture file.
     * @param frameCols       number of columns in the sheet
     * @param frameRows       number of rows in the sheet
     * @param animDelay       delay for the animation. (Use "1 / [Frame Count]f " if not sure what to use.
     * @param framesStart     the first frame to include in the animation
     * @param framesEnd       the last frame to include in the animation
     * @return the animation
     */
    public static Animation setAnimation(String textureFileName, int frameCols, int frameRows, float animDelay, int framesStart, int framesEnd) {
        Texture textureSheet = new Texture(textureFileName);
        textureSheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        int tileWidth = textureSheet.getWidth() / frameCols;
        int tileHeight = textureSheet.getHeight() / frameRows;
        TextureRegion[][] tmp = TextureRegion.split(textureSheet, tileWidth, tileHeight);

        Array<TextureRegion> frames = Utilities.to1DArray(tmp);
        return new Animation(animDelay, (framesStart == -1 && framesEnd == -1) ? frames : Utilities.getFramesRange(frames, framesStart, framesEnd));
    }

    public static TextureRegion getFrame(String textureFileName, int frameCols, int frameRows, int frameNum) {
        Texture textureSheet = new Texture(textureFileName);
        textureSheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        int tileWidth = textureSheet.getWidth() / frameCols;
        int tileHeight = textureSheet.getHeight() / frameRows;
        TextureRegion[][] tmp = TextureRegion.split(textureSheet, tileWidth, tileHeight);

        Array<TextureRegion> frames = Utilities.to1DArray(tmp);
        return frames.first();
    }

    /**
     * Creates an animation from a sprite sheet.
     *
     * @param textureFileName name of the texture file.
     * @param frameCols       number of columns in the sheet
     * @param frameRows       number of rows in the sheet
     * @param animDelay       delay for the animation. (Use "1 / [Frame Count]f " if not sure what to use.
     * @return the animation
     */
    public static Animation setAnimation(String textureFileName, int frameCols, int frameRows, float animDelay) {
        return setAnimation(textureFileName, frameCols, frameRows, animDelay, -1, -1);
    }
}
