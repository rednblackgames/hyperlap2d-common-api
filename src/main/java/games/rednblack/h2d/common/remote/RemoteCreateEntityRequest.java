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

    public RemoteHandle<RemoteCreateEntityResult> handle;
}