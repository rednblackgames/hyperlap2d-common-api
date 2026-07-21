package games.rednblack.h2d.common.remote;

import java.util.ArrayList;
import java.util.List;

/** Result: componentKeys whose panel currently applies (editable) + those that can be added (addable). */
public class RemoteEditableComponentsResult {
    public boolean ok;
    public String error;
    public List<String> editable = new ArrayList<>();
    public List<String> addable = new ArrayList<>();
}