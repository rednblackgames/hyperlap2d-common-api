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

package games.rednblack.h2d.common;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;

public class H2DDialog extends VisDialog {
    protected Skin skin;

	static public final Interpolation.SwingOut swingOut = new Interpolation.SwingOut(1.1f);

    public H2DDialog(String title) {
        super(title);
		init();
    }

    public H2DDialog(String title, String style) {
    	super(title, style);
    	init();
	}

    private void init() {
		skin = VisUI.getSkin();
		padTop(32);
	}

	@Override
	public void hide () {
		super.hide(Actions.sequence(
				Actions.parallel(
					Actions.scaleTo(0.8f, 0.8f, FADE_TIME, Interpolation.pow5Out),
					Actions.alpha(0, FADE_TIME, Interpolation.pow3In),
					Actions.moveBy(0, -15, FADE_TIME, Interpolation.pow5In)
				),
				Actions.removeListener(ignoreTouchDown, true),
				Actions.moveBy(0, 15),
				Actions.removeActor()));
		onDismiss();
	}

	@Override
	public void hide (Action action) {
		super.hide(action);
		onDismiss();
	}

	@Override
	public VisDialog show(Stage stage) {
		show(stage, Actions.sequence(
				Actions.alpha(0),
				Actions.scaleTo(0.8f, 0.8f),
				Actions.moveBy(0, -15),
				Actions.parallel(
						Actions.scaleTo(1, 1, FADE_TIME, swingOut),
						Actions.alpha(1, FADE_TIME),
						Actions.moveBy(0, 15, FADE_TIME)
				)
		));
		setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
		setOrigin(getWidth() / 2f, getHeight() / 2f);
		return this;
	}

	@Override
	public void addCloseButton() {
		VisImageButton closeButton = new VisImageButton("close-window");
		this.getTitleTable().add(closeButton).padRight(0);
		closeButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				close();
			}
		});
		closeButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.cancel();
				return true;
			}
		});
	}

	@Override
	public void close () {
		hide();
	}

	protected void onDismiss() {

	}

}
