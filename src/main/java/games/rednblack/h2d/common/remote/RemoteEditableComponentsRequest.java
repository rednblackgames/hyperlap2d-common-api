package games.rednblack.h2d.common.remote;

/** Request the editable + addable component keys for an entity (auto-discovery). */
public class RemoteEditableComponentsRequest {
    public String entityId;
    public RemoteHandle<RemoteEditableComponentsResult> handle;
}