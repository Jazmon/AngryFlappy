package fi.tamk.tiko.angryflappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Atte Huhtakangas on 27.1.2015 21:34.
 * -
 * Part of AngryFlappy in package fi.tamk.tiko.angryflappy.
 */
public class Utilities {
    private static final String TAG = Utilities.class.getName();

    public static Array<TextureRegion> to1DArray(TextureRegion[][] tmp) {
        Array<TextureRegion> array = new Array<TextureRegion>();
        int counter = 0;

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                array.add(tmp[i][j]);
                counter++;
            }
        }

        Gdx.app.debug(TAG, counter + " frames added to 1D TextureRegion array");

        return array;
    }

    public static Array<TextureRegion> getFramesRange(Array<TextureRegion> frames, int first, int last) {
        Array<TextureRegion> tmp = new Array<TextureRegion>();
        int counter = 0;

        for(int i = first; i <= last; i++) {
            if(frames.get(i) != null) {
                tmp.add(frames.get(i));
                counter++;
            }
        }

        Gdx.app.debug(TAG, "got " + counter + " frames out of " + (last - first + 1));

        return tmp;
    }
}
