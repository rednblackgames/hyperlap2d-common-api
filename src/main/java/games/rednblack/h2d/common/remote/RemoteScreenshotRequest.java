package games.rednblack.h2d.common.remote;

/**
 * Request to capture the current scene to PNG. Sent by the MCP plugin via the
 * {@code MsgAPI.ACTION_REMOTE_SCREENSHOT} notification; fulfilled on the render thread
 * by the editor-core RemoteOpsMediator.
 */
public class RemoteScreenshotRequest {
    public enum Mode { WHOLE, VIEW, REGION }

    public Mode mode = Mode.WHOLE;
    /** World-space rectangle, used only for {@link Mode#REGION}. */
    public float x, y, width, height;

    public RemoteHandle<RemoteScreenshotResult> handle;
}