package games.rednblack.h2d.common.vo;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TexturePackerVO {
    public static Map<String, Texture.TextureFilter> filterMap = new HashMap<>();
    static {
        filterMap.put("Linear", Texture.TextureFilter.Linear);
        filterMap.put("Nearest", Texture.TextureFilter.Nearest);
        filterMap.put("MipMap", Texture.TextureFilter.MipMap);
        filterMap.put("MipMapNearestNearest", Texture.TextureFilter.MipMapNearestNearest);
        filterMap.put("MipMapLinearNearest", Texture.TextureFilter.MipMapLinearNearest);
        filterMap.put("MipMapNearestLinear", Texture.TextureFilter.MipMapNearestLinear);
        filterMap.put("MipMapLinearLinear", Texture.TextureFilter.MipMapLinearLinear);
    }

    public String maxWidth = "2048";
    public String maxHeight = "2048";
    public boolean duplicate = true;
    public boolean legacy = false;
    public boolean square;
    public String filterMag = "Linear";
    public String filterMin = "Linear";
    public boolean fast = true;

    public  TexturePackerVO() {

    }

    public TexturePackerVO(TexturePackerVO vo) {
        maxHeight = vo.maxHeight;
        maxWidth = vo.maxWidth;
        duplicate = vo.duplicate;
        square = vo.square;
        filterMag = vo.filterMag;
        filterMin = vo.filterMin;
        legacy = vo.legacy;
        fast = vo.fast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TexturePackerVO that = (TexturePackerVO) o;
        return duplicate == that.duplicate &&
                square == that.square &&
                legacy == that.legacy &&
                fast == that.fast &&
                Objects.equals(maxWidth, that.maxWidth) &&
                Objects.equals(maxHeight, that.maxHeight) &&
                Objects.equals(filterMag, that.filterMag) &&
                Objects.equals(filterMin, that.filterMin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxWidth, maxHeight, duplicate, square, legacy, filterMag, filterMin, fast);
    }
}
