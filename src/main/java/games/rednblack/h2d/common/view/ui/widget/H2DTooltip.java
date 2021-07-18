package games.rednblack.h2d.common.view.ui.widget;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Null;

/** A listener that shows a tooltip actor when the mouse is over another actor.
 * @author Nathan Sweet */
public class H2DTooltip<T extends Actor> extends InputListener {
    static Vector2 tmp = new Vector2();

    private final H2DTooltipManager manager;
    final Container<T> container;
    boolean instant, always, followMouse;
    Actor targetActor;

    /** @param contents May be null. */
    public H2DTooltip (@Null T contents) {
        this(contents, H2DTooltipManager.getInstance());
    }

    /** @param contents May be null. */
    public H2DTooltip (@Null T contents, H2DTooltipManager manager) {
        this.manager = manager;

        container = new Container(contents) {
            public void act (float delta) {
                super.act(delta);
                if (targetActor != null && targetActor.getStage() == null) remove();
            }
        };
        container.setTouchable(Touchable.disabled);
    }

    public H2DTooltipManager getManager () {
        return manager;
    }

    public Container<T> getContainer () {
        return container;
    }

    public void setActor (@Null T contents) {
        container.setActor(contents);
    }

    public @Null T getActor () {
        return container.getActor();
    }

    /** If true, this tooltip is shown without delay when hovered. */
    public void setInstant (boolean instant) {
        this.instant = instant;
    }

    /** If true, this tooltip will follow mouse position when actor is hovered. */
    public void setFollowMouse(boolean followMouse) {
        this.followMouse = followMouse;
    }

    /** If true, this tooltip is shown even when tooltips are not {@link H2DTooltipManager#enabled}. */
    public void setAlways (boolean always) {
        this.always = always;
    }

    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        if (instant) {
            container.toFront();
            return false;
        }
        manager.touchDown(this);
        return false;
    }

    public boolean mouseMoved (InputEvent event, float x, float y) {
        if (!followMouse && container.hasParent()) return false;
        setContainerPosition(event.getListenerActor(), x, y);
        return true;
    }

    private void setContainerPosition (Actor actor, float x, float y) {
        this.targetActor = actor;
        Stage stage = actor.getStage();
        if (stage == null) return;

        float offsetX = manager.offsetX, offsetY = manager.offsetY, dist = manager.edgeDistance;
        Vector2 point = actor.localToStageCoordinates(tmp.set(x + offsetX, y - offsetY - container.getHeight()));
        if (point.y < dist) point = actor.localToStageCoordinates(tmp.set(x + offsetX, y + offsetY));
        if (point.x < dist) point.x = dist;
        if (point.x + container.getWidth() > stage.getWidth() - dist) point.x = stage.getWidth() - dist - container.getWidth();
        if (point.y + container.getHeight() > stage.getHeight() - dist) point.y = stage.getHeight() - dist - container.getHeight();
        container.setPosition(point.x, point.y);

        point = actor.localToStageCoordinates(tmp.set(actor.getWidth() / 2, actor.getHeight() / 2));
        point.sub(container.getX(), container.getY());
        container.setOrigin(point.x, point.y);
    }

    public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
        if (pointer != -1) return;
        if (Gdx.input.isTouched()) return;
        Actor actor = event.getListenerActor();
        if (fromActor != null && fromActor.isDescendantOf(actor)) return;
        container.pack();
        setContainerPosition(actor, x, y);
        manager.enter(this);
    }

    public void exit (InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
        if (toActor != null && toActor.isDescendantOf(event.getListenerActor())) return;
        hide();
    }

    public void hide () {
        manager.hide(this);
    }
}

