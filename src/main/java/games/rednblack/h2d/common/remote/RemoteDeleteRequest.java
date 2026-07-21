package games.rednblack.h2d.common.remote;

/** Request to delete an entity by uniqueId (selects it, then runs the standard delete command). */
public class RemoteDeleteRequest {
    public String entityId;
    public RemoteHandle<RemoteEditResult> handle;
}