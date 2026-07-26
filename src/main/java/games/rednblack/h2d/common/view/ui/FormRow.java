package games.rednblack.h2d.common.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * A single line form: inline labels, fields sharing the leftover width, and the action button at the
 * right. Used above a {@link ListTable} in the panels that add items to a list, so that "add a new
 * one" always reads as one line, whatever the panel.
 * <pre>
 * new FormRow().label("Key").field(keyField).label("Value").field(valueField).action(addButton)
 * </pre>
 * Set in the editor's default font, like the rest of the dialogs.
 */
public class FormRow extends VisTable {

    /** Space between a label and its field, and between one field and the next label. */
    public static final int LABEL_GAP = 6;
    public static final int PART_GAP = 14;
    public static final int ACTION_GAP = 12;
    /**
     * How much width a field asks for, and the least it accepts. A form row sits in a dialog that
     * sizes itself, so its fields have to state a usable width: asking for nothing, the way the
     * fixed width property box does, leaves them with whatever the labels and the button do not take.
     */
    public static final int FIELD_WIDTH = 150;
    public static final int FIELD_MIN_WIDTH = 110;

    private boolean empty = true;

    /** Name of the field that follows, in the label colour of the design. */
    public FormRow label(String text) {
        VisLabel label = StandardWidgetsFactory.createLabel(withColon(text),
                PropertyGrid.style(PropertyGrid.LABEL_STYLE_LARGE), Align.left);
        add(label).padLeft(empty ? 0 : PART_GAP).padRight(LABEL_GAP);
        empty = false;
        return this;
    }

    /** An input that takes a share of the row's width: text fields, select boxes. */
    public FormRow field(Actor field) {
        return field(field, FIELD_WIDTH, FIELD_MIN_WIDTH);
    }

    /** {@link #field(Actor)} with its own widths, for inputs that need more or less room. */
    public FormRow field(Actor field, int prefWidth, int minWidth) {
        add(field).growX().prefWidth(prefWidth).minWidth(minWidth)
                .height(PropertyGrid.FIELD_HEIGHT).padLeft(empty ? 0 : PART_GAP);
        empty = false;
        return this;
    }

    /**
     * A group of inputs that grows with the row while keeping the width its own content asks for.
     * Unlike {@link #field(Actor)} it states no width of its own, because the group's changes: the
     * shader value group holds one field for a float and four for a vec4.
     */
    public FormRow group(Actor group) {
        add(group).growX().height(PropertyGrid.FIELD_HEIGHT).padLeft(empty ? 0 : PART_GAP);
        empty = false;
        return this;
    }

    /** An input that keeps its own size: spinners, tint buttons, read only values. */
    public FormRow compact(Actor field) {
        add(field).left().padLeft(empty ? 0 : PART_GAP);
        empty = false;
        return this;
    }

    /** The button that commits the row, pinned at its right end. */
    public FormRow action(Actor button) {
        add(button).height(PropertyGrid.FIELD_HEIGHT).padLeft(ACTION_GAP);
        empty = false;
        return this;
    }

    private static String withColon(String text) {
        if (text == null || text.isEmpty()) return "";
        String trimmed = text.trim();
        return trimmed.endsWith(":") ? trimmed : trimmed + ":";
    }
}
