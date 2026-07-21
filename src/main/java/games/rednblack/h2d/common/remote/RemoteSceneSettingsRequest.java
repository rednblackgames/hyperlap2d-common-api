package games.rednblack.h2d.common.remote;

/** Request the current scene's settings (physics, lights, shader). */
public class RemoteSceneSettingsRequest {
    public RemoteHandle<RemoteSceneSettingsResult> handle;
}