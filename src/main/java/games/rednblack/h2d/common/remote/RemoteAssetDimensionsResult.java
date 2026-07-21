package games.rednblack.h2d.common.remote;

import java.util.ArrayList;
import java.util.List;

/** Result: pixelsPerWU + per-region pixel and world dimensions (world = pixel / pixelsPerWU). */
public class RemoteAssetDimensionsResult {
    public boolean ok;
    public String error;
    public int pixelsPerWU;
    public List<RegionDim> regions = new ArrayList<>();

    public static class RegionDim {
        public String name;
        public int pixelWidth;
        public int pixelHeight;
        public float worldWidth;
        public float worldHeight;
        public boolean ninePatch;
        public RegionDim(String name, int pixelWidth, int pixelHeight, float worldWidth, float worldHeight, boolean ninePatch) {
            this.name = name; this.pixelWidth = pixelWidth; this.pixelHeight = pixelHeight;
            this.worldWidth = worldWidth; this.worldHeight = worldHeight; this.ninePatch = ninePatch;
        }
    }
}