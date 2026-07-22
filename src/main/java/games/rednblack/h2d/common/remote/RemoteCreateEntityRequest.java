package games.rednblack.h2d.common.remote;

/**
 * Request to create a new entity in the current scene. type selects the factory method:
 * image, spriteAnimation, spineAnimation, libraryItem, 9patch, tinyvg, particle, talos
 * (name-based), primitive (width/height rect), composite (empty), label (fontFamily/fontSize),
 * light (lightType POINT/CONE).
 */
public class RemoteCreateEntityRequest {
    public String type;
    public String name;
    public float x, y;
    public float width = 100f, height = 100f;     // primitive rect
    public String fontFamily;                      // label
    public int fontSize = 20;                      // label
    public String lightType;                       // light: "POINT" or "CONE"
    /**
     * Optional: the uniqueId of a composite entity to create INSIDE (as a child). When set,
     * (x, y) are in the composite's local coordinate space. The composite must be a direct child
     * of the current viewing entity (the scene root, or the composite currently being edited);
     * to target a nested composite, enter its container in the editor first. When null/empty the
     * entity is created at the current viewing entity (normally the scene root).
     */
    public String parentUniqueId;
    /**
     * Optional: the layer name to create the entity on (resolved case-insensitively against the
     * effective parent's layers; layer names are otherwise case-sensitive in the editor). When
     * null/empty the entity is created on the currently selected layer.
     */
    public String layer;

    public RemoteHandle<RemoteCreateEntityResult> handle;
}