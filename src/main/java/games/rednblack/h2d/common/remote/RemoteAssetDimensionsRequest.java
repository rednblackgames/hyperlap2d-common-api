package games.rednblack.h2d.common.remote;

/** Request the pixel + world dimensions of all image/nine-patch regions (for scene layout planning). */
public class RemoteAssetDimensionsRequest {
    public RemoteHandle<RemoteAssetDimensionsResult> handle;
}