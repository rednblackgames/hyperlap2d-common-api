package games.rednblack.h2d.common.remote;

/**
 * Request for a layer-management operation. {@code op} selects the action; the editor-core bridge
 * resolves layer names case-insensitively against the current viewing entity's layers (the editor
 * stores layer names case-sensitively by hash, but the MCP accepts any case to avoid mistakes),
 * runs the appropriate (undoable) layer command, and completes the handle.
 *
 * <ul>
 *   <li><b>LIST</b> — return all layers of the current viewing entity (layerName ignored).</li>
 *   <li><b>CREATE</b> — create a new layer named {@code layerName} (rejected if a same-cased or
 *       case-insensitive match already exists).</li>
 *   <li><b>DELETE</b> — delete the layer {@code layerName} and every entity on it.</li>
 *   <li><b>RENAME</b> — rename layer {@code layerName} to {@code newName}.</li>
 *   <li><b>ORDER</b> — move layer {@code layerName} one step {@code direction} ("up" or "down").</li>
 *   <li><b>SET_ENTITY</b> — move the entity {@code entityId} onto layer {@code layerName}.</li>
 * </ul>
 */
public class RemoteLayerRequest {
    public enum Op { LIST, CREATE, DELETE, RENAME, ORDER, SET_ENTITY }

    public Op op;
    public String layerName;
    public String newName;     // RENAME target
    public String direction;   // ORDER: "up" or "down"
    public String entityId;    // SET_ENTITY

    public RemoteHandle<RemoteLayerResult> handle;
}