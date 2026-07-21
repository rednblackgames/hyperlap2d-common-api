package games.rednblack.h2d.common.remote;

/** Request to open (load) a scene by name in the editor. */
public class RemoteOpenSceneRequest {
    public String sceneName;
    public RemoteHandle<RemoteEditResult> handle;
}