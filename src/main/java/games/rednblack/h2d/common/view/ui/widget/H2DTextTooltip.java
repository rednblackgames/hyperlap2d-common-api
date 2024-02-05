package games.rednblack.h2d.common.view.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.utils.Null;


/** A tooltip that shows a label.
 * @author Nathan Sweet */
public class H2DTextTooltip extends H2DTooltip<Label> {

    private final Label label;

    public H2DTextTooltip (@Null String text, Skin skin) {
        this(text, H2DTooltipManager.getInstance(), skin.get(TextTooltip.TextTooltipStyle.class));
    }

    public H2DTextTooltip (@Null String text, Skin skin, String styleName) {
        this(text, H2DTooltipManager.getInstance(), skin.get(styleName, TextTooltip.TextTooltipStyle.class));
    }

    public H2DTextTooltip (@Null String text, TextTooltip.TextTooltipStyle style) {
        this(text, H2DTooltipManager.getInstance(), style);
    }

    public H2DTextTooltip (@Null String text, H2DTooltipManager manager, Skin skin) {
        this(text, manager, skin.get(TextTooltip.TextTooltipStyle.class));
    }

    public H2DTextTooltip (@Null String text, H2DTooltipManager manager, Skin skin, String styleName) {
        this(text, manager, skin.get(styleName, TextTooltip.TextTooltipStyle.class));
    }

    public H2DTextTooltip (@Null String text, final H2DTooltipManager manager, TextTooltip.TextTooltipStyle style) {
        super(null, manager);

        label = new Label(text, style.label);
        setText(text);

        setStyle(style);
        container.pad(5);
    }

    public void setText(String text) {
        label.setWrap(false);
        label.setText(text);
        label.invalidate();
        float fullWidth = label.getPrefWidth() + 1;
        label.setWrap(true);
        label.setWidth(Math.min(getManager().maxWidth, fullWidth));
        container.setActor(label);
        container.width(label.getWidth());
    }

    public void setStyle (TextTooltip.TextTooltipStyle style) {
        if (style == null) throw new NullPointerException("style cannot be null");
        container.getActor().setStyle(style.label);
        container.setBackground(style.background);
        container.maxWidth(style.wrapWidth);
    }
}
