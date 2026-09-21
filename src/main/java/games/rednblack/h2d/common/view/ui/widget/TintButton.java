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

package games.rednblack.h2d.common.view.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.kotcrab.vis.ui.VisUI;

/**
 * A colour swatch. It is a layout aware group so a table can stretch it to the space it has; a
 * table only resizes plain groups it is told the exact size of. Anywhere it is not stretched it
 * keeps the size it was built with, which is what it reports as its preferred size.
 */
public class TintButton extends WidgetGroup {

    private final Skin skin;
    private final Image colorImg;
    private final Image borderImg;
    private final float prefWidth, prefHeight;

    private final Color colorValue = new Color();

    public TintButton(int width, int height) {
        skin = VisUI.getSkin();
        colorImg = new Image(skin.getDrawable("white"));
        borderImg = new Image(skin.getDrawable("tint-border"));

        addActor(colorImg);
        addActor(borderImg);

        prefWidth = width;
        prefHeight = height;
        setSize(width, height);
    }

    @Override
    public float getPrefWidth() {
        return prefWidth;
    }

    @Override
    public float getPrefHeight() {
        return prefHeight;
    }

    /** The fill and the border follow the size the button has been given. */
    @Override
    public void layout() {
        colorImg.setBounds(1, 1, getWidth() - 2, getHeight() - 2);
        borderImg.setBounds(0, 0, getWidth(), getHeight());
    }

    public Color getColorValue() {
        return colorValue;
    }

    public void setColorValue(Color color) {
        colorImg.setColor(color);
        colorValue.set(color);
    }
}
