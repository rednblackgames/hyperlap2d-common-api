package games.rednblack.h2d.common.remote;

import java.util.HashMap;
import java.util.Map;

/** Result: a copy of the editor's entityType (int) -> display-name (String) map. */
public class RemoteTypeNamesResult {
    public boolean ok;
    public String error;
    public Map<Integer, String> names = new HashMap<>();
}