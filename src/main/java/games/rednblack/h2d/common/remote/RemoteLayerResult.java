package games.rednblack.h2d.common.remote;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of a layer-management request. For {@code LIST}, {@link #layers} holds the current viewing
 * entity's layers (in editor order: index 0 is the back-most layer). For mutations, {@link #ok}
 * and {@link #error} reflect success/failure (mutations run through the editor's undoable layer
 * commands).
 */
public class RemoteLayerResult {
    public boolean ok;
    public String error;
    public final List<LayerInfo> layers = new ArrayList<>();

    public static class LayerInfo {
        public final String name;
        public final int index;
        public final boolean isVisible;
        public final boolean isLocked;

        public LayerInfo(String name, int index, boolean isVisible, boolean isLocked) {
            this.name = name;
            this.index = index;
            this.isVisible = isVisible;
            this.isLocked = isLocked;
        }
    }
}