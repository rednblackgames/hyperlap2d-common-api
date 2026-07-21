package games.rednblack.h2d.common.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Result of listing loaded assets: a category name (spine, spriteAnimation, particle, talos, font, tinyvg, shader, imageRegion) to asset names. */
public class RemoteAssetsResult {
    public boolean ok;
    public String error;
    public Map<String, List<String>> categories = new HashMap<>();

    public void add(String category, String name) {
        categories.computeIfAbsent(category, k -> new ArrayList<>()).add(name);
    }
}