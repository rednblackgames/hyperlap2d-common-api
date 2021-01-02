package games.rednblack.h2d.common.view.ui.widget;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.PopupMenu;

public class H2DPopupMenu extends PopupMenu {

    public static final Interpolation.SwingOut swingOut = new Interpolation.SwingOut(1.05f);
    private static final float TRANSITION_TIME = 0.15f;

    private boolean removing = false;

    public H2DPopupMenu() {
        super("noborder");
        setTransform(true);
    }

    @Override
    public void showMenu(Stage stage, float x, float y) {
        super.showMenu(stage, x, y);
        setOrigin(Align.topLeft);
        setScale(1.4f);
        getColor().a = 0;
        clearActions();
        addAction(Actions.parallel(
                Actions.alpha(1, TRANSITION_TIME),
                Actions.scaleTo(1, 1, TRANSITION_TIME, swingOut)
        ));
        removing = false;
    }

    @Override
    public boolean remove () {
        if (!removing) {
            removing = true;
            clearActions();
            addAction(Actions.sequence(
                    Actions.parallel(
                            Actions.alpha(0, TRANSITION_TIME),
                            Actions.scaleTo(1.25f, 1.25f, TRANSITION_TIME, Interpolation.pow2Out)
                    ),
                    Actions.run(super::remove)));
        }
        return removing;
    }
}