package games.rednblack.h2d.common.command;

import com.badlogic.gdx.math.Vector2;

/**
 * Mutable transform data used by {@link TransformCommandBuilder} for the
 * in-progress prev/current state. Fields are {@code null} when not set
 * (the builder only sets the fields that change).
 */
public class TransformData {
    public Vector2 pos;
    public Vector2 size;
    public Vector2 scale;
    public Float rotation;
    public Vector2 origin;

    public TransformData() {
    }

    public TransformData(Vector2 pos, Vector2 size, Vector2 scale, Float rotation, Vector2 origin) {
        this.pos = pos;
        this.size = size;
        this.scale = scale;
        this.rotation = rotation;
        this.origin = origin;
    }

    public boolean hasAny() {
        return pos != null || size != null || scale != null || rotation != null || origin != null;
    }
}