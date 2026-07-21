package games.rednblack.h2d.common.remote;

/** Request to list loaded project assets by category. Fulfilled by RemoteOpsMediator. */
public class RemoteAssetsRequest {
    public RemoteHandle<RemoteAssetsResult> handle;
}