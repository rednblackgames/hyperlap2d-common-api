package games.rednblack.h2d.common.remote;

/** Result of a screenshot capture. {@code pngBytes} is a complete PNG file payload. */
public class RemoteScreenshotResult {
    public boolean ok;
    public String error;
    public byte[] pngBytes;
    public int width, height;
}