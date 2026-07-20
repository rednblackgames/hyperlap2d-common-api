package games.rednblack.h2d.common.command;

import com.badlogic.gdx.math.Vector2;
import games.rednblack.editor.renderer.components.DimensionsComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.ecs.Engine;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Facade;

/**
 * Created by Osman on 01.08.2015.
 */
public class TransformCommandBuilder {

    private int entity;
    private TransformData prev;
    private TransformData current = new TransformData();

    public void begin(int forEntity, Engine engine) {
        TransformComponent transformComponent = ComponentRetriever.get(forEntity, TransformComponent.class, engine);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(forEntity, DimensionsComponent.class, engine);
        entity = forEntity;
        prev = new TransformData(
                new Vector2(transformComponent.x, transformComponent.y),
                new Vector2(dimensionsComponent.width, dimensionsComponent.height),
                new Vector2(transformComponent.scaleX, transformComponent.scaleY),
                transformComponent.rotation,
                new Vector2(transformComponent.originX, transformComponent.originY));
        current = new TransformData();
    }

    public void setPos(float x, float y) {
        current.pos = new Vector2(x, y);
    }

    public void setSize(float width, float height) {
        current.size = new Vector2(width, height);
    }

    public void setScale(float x, float y) {
        current.scale = new Vector2(x, y);
    }

    public void setRotation(float rotation) {
        current.rotation = rotation;
    }

    public void setOrigin(float x, float y) {
        current.origin = new Vector2(x, y);
    }

    public void execute(Facade facade) {
        // check if payload is worth sending
        if (current.hasAny()) {
            facade.sendNotification(MsgAPI.ACTION_ITEM_TRANSFORM_TO, new TransformPayload(entity, prev, current));
        }
    }
}