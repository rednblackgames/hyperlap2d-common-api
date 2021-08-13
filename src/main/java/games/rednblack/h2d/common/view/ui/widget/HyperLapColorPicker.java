package games.rednblack.h2d.common.view.ui.widget;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerListener;
import games.rednblack.h2d.common.H2DDialog;

public class HyperLapColorPicker extends ColorPicker {
    private boolean fadeOutActionRunning;

    public HyperLapColorPicker () {
        this((String) null);
    }

    public HyperLapColorPicker (String title) {
        this("default", title, null);
    }

    public HyperLapColorPicker (String title, ColorPickerListener listener) {
        this("default", title, listener);
    }

    public HyperLapColorPicker (ColorPickerListener listener) {
        this("default", null, listener);
    }

    public HyperLapColorPicker (String styleName, String title, ColorPickerListener listener) {
        super(styleName, title, listener);
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
    protected void close() {
        super.close();
    }

    @Override
    public VisWindow fadeIn() {
        addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.scaleTo(0.8f, 0.8f),
                Actions.moveBy(0, -15),
                Actions.parallel(
                        Actions.scaleTo(1, 1, FADE_TIME, H2DDialog.swingOut),
                        Actions.alpha(1, FADE_TIME),
                        Actions.moveBy(0, 15, FADE_TIME)
                )
        ));
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        return this;
    }

    @Override
    public void fadeOut() {
        if (fadeOutActionRunning) return;
        fadeOutActionRunning = true;
        final Touchable previousTouchable = getTouchable();
        setTouchable(Touchable.disabled);
        Stage stage = getStage();
        if (stage != null && stage.getKeyboardFocus() != null && stage.getKeyboardFocus().isDescendantOf(this)) {
            FocusManager.resetFocus(stage);
        }
        addAction(Actions.sequence(Actions.sequence(
                Actions.parallel(
                        Actions.scaleTo(0.8f, 0.8f, FADE_TIME, Interpolation.pow5Out),
                        Actions.alpha(0, FADE_TIME, Interpolation.pow3In),
                        Actions.moveBy(0, -15, FADE_TIME, Interpolation.pow5In)
                ),
                Actions.moveBy(0, 15)), new Action() {
            @Override
            public boolean act (float delta) {
                setTouchable(previousTouchable);
                remove();
                getColor().a = 1f;
                fadeOutActionRunning = false;
                return true;
            }
        }));
    }
}
