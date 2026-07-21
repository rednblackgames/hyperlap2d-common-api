package games.rednblack.h2d.common.remote;

import java.util.HashMap;
import java.util.Map;

/** Result: the current scene name + its settings (physics/lights/shader fields). */
public class RemoteSceneSettingsResult {
    public boolean ok;
    public String error;
    public String sceneName;
    public Map<String, Object> settings = new HashMap<>();
}