package games.rednblack.h2d.common.view.ui.listener;

/**
 * Used to get events from {@link games.rednblack.h2d.common.H2DDialogs} input dialog.
 */
public interface MultipleInputDialogListener {
    /**
     * Called when input dialog has finished.
     * @param input text entered by user.
     */
    void finished (String[] input);

    /**
     * Called when user canceled dialog or pressed 'close' button. This won't be ever called if dialog is not
     * cancelable.
     */
    void canceled ();
}
