package games.rednblack.h2d.common.plugins;

/**
 * A window of the editor's application that a plugin opened and can still reach.
 * <p>
 * It is a window of the same application, not a window of its own: it is drawn by the same loop as the
 * editor and its listener is called on the render thread, with this window current.
 */
public interface H2DWindow {

    /** Closes it, and runs the close action once. Closing an already closed window does nothing. */
    void close();

    /** Brings it in front of whatever is covering it. */
    void focus();

    boolean isOpen();

    /**
     * Asks for one frame. A window that renders only when asked costs the loop nothing in between - no
     * draw, and no buffer swap, which on a composited desktop is the part that paces everything else.
     */
    void requestRender();
}
