package games.rednblack.h2d.common.remote;

/**
 * Request to set an entity's z-index (local to its layer; an integer the runtime auto-adjusts
 * into a linear progression). The editor-core bridge resolves the uniqueId to an entity, runs an
 * undoable z-index command, and completes the handle.
 */
public class RemoteZIndexRequest {
    public String entityId;
    public int zIndex;
    public RemoteHandle<RemoteEditResult> handle;
}