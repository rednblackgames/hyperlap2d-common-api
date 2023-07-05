/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package games.rednblack.h2d.common.proxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import games.rednblack.h2d.common.view.ui.Cursors;
import games.rednblack.h2d.common.vo.CursorData;
import games.rednblack.puremvc.Proxy;

/**
 * Created by azakhary on 5/15/2015.
 */
public class CursorManager extends Proxy {
    private static final String TAG = CursorManager.class.getCanonicalName();
    public static final String NAME = TAG;

    private CursorData cursor;
    private TextureRegion region;
    private CursorData overrideCursor = null;

    private final ObjectMap<String, Cursor> cursorCache = new ObjectMap<>();

    public CursorManager() {
        super(NAME, null);

        setCursor(Cursors.NORMAL);
    }

    public void setCursor(CursorData cursor, TextureRegion region) {
        this.cursor = cursor;
        this.region = region;
    }

    public void setCursor(CursorData cursor) {
        setCursor(cursor, null);
    }

    public void setOverrideCursor(CursorData cursor) {
        overrideCursor = cursor;
    }

    public void removeOverrideCursor() {
        setOverrideCursor(null);
    }

    public void displayCustomCursor() {
        setCursorPixmap(region);
    }

    public void hideCustomCursor() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }

    private void setCursorPixmap(TextureRegion region) {
        CursorData currentCursor = overrideCursor;
        if(currentCursor == null) {
            currentCursor = cursor;
        }

        if (currentCursor.systemCursor != null) {
            Gdx.graphics.setSystemCursor(currentCursor.systemCursor);
            return;
        }

        Pixmap cursorPm = null;
        Cursor cursorObj = null;
        String cursorName = null;
        if (region == null || overrideCursor != null) {
            if (cursorCache.get(currentCursor.region) != null) {
                cursorObj = cursorCache.get(currentCursor.region);
            } else {
                cursorPm = new Pixmap(Gdx.files.internal("cursors/" + currentCursor.region + ".png"));
                cursorName = currentCursor.region;
            }
        } else {
            Texture texture = region.getTexture();
            if (cursorCache.get(texture.toString()) != null) {
                cursorObj = cursorCache.get(texture.toString());
            } else {
                if (!texture.getTextureData().isPrepared()) {
                    texture.getTextureData().prepare();
                }
                cursorPm = texture.getTextureData().consumePixmap();
                cursorName = texture.toString();
            }
        }

        if (cursorObj == null) {
            cursorObj = Gdx.graphics.newCursor(cursorPm, currentCursor.getHotspotX(), currentCursor.getHotspotY());
            cursorCache.put(cursorName, cursorObj);
        }
        Gdx.graphics.setCursor(cursorObj);

        if (cursorPm != null)
            cursorPm.dispose();
    }
}
