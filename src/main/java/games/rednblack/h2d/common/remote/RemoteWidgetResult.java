package games.rednblack.h2d.common.remote;

import java.util.ArrayList;
import java.util.List;

/** Result of a widget operation. {@code ok} is false if it was rejected. */
public class RemoteWidgetResult {
    public boolean ok;
    public String error;
    public List<String> validationErrors = new ArrayList<>();
    /** CONVERT: uniqueId of the widget made. */
    public String widgetId;
}
