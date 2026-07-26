package games.rednblack.h2d.common.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * Lays out a property panel on a shared two column grid: a fixed width label column on the left and
 * a field column on the right. Every metric lives in the constants below, so panels built with this
 * helper line up with each other instead of each one inventing its own widths and paddings.
 * <p>
 * Rows are chained and each one opens itself, so a panel body reads as a list of properties:
 * <pre>
 * PropertyGrid grid = PropertyGrid.on(mainTable);
 * grid.section("Physics", physicsEnabledSwitch);
 * grid.pair("Gravity", "X", gravityX, "Y", gravityY);
 * grid.row("Sleep velocity", sleepVelocity);
 * grid.toggle("Allow sleep", allowSleepSwitch);
 * </pre>
 * The property box is only {@value #BOX_WIDTH}px wide and must never scroll horizontally, so the
 * grid is width bound by construction: fields are sized by the grid, not by their content. A widget
 * whose content can be arbitrarily long (a select box holding long animation names, a label showing
 * an entity id) must never dictate the column width — put it in a cell wrapped by
 * {@link #elastic(Cell)}, which lets it fill whatever width it is given without asking for more.
 */
public class PropertyGrid {

    /** Width of the property box the panels live in, see UICollapsibleBox#BOX_DEFAULT_WIDTH. */
    public static final int BOX_WIDTH = 270;
    /** What is left of {@link #BOX_WIDTH} once the vertical scrollbar of the box is accounted for. */
    public static final int BOX_CONTENT_WIDTH = BOX_WIDTH - 5;
    /** Width a panel spans, and the width its body asks for so the field column can fill it. */
    public static final int PANEL_WIDTH = BOX_CONTENT_WIDTH;
    /** Padding around a whole panel. */
    public static final int PANEL_PAD = 6;
    /** Widest the label column may get. Shorter labels give the leftover to the field column. */
    public static final int LABEL_WIDTH = 104;
    public static final int LABEL_GAP = 6;
    /**
     * Narrowest the field column gets, when the labels are long enough to claim {@link #LABEL_WIDTH}.
     * The column expands into whatever the label column leaves behind, so fields are usually wider.
     */
    public static final int FIELD_WIDTH = 127;
    /** Two paired fields plus their inline labels and gaps add up to {@link #FIELD_WIDTH}. */
    public static final int HALF_FIELD_WIDTH = 47;
    /** Three fields plus their gaps add up to {@link #FIELD_WIDTH}. */
    public static final int THIRD_FIELD_WIDTH = 40;
    public static final int FIELD_HEIGHT = 21;
    /** Width the grid is laid out in: the panel minus its padding. */
    public static final int GRID_WIDTH = PANEL_WIDTH - 2 * PANEL_PAD;
    /** Inline "X"/"Y" labels of a paired row: single letters, so the fields get the rest. */
    public static final int SUB_LABEL_WIDTH = 10;
    public static final int SUB_LABEL_GAP = 3;
    public static final int PAIR_GAP = 6;
    /**
     * Left and right inset of the rows, so the content sits inside the section rules and titles
     * rather than running flush to the panel edges.
     */
    public static final int CONTENT_PAD = 8;
    /** Live value shown next to a slider, e.g. "1.2x" or "Unlimited". */
    public static final int VALUE_WIDTH = 60;
    /** Vertical rhythm. */
    public static final int ROW_PAD = 5;
    public static final int SECTION_PAD_TOP = 10;
    public static final int SECTION_PAD_BOTTOM = 7;
    public static final int BUTTON_GAP = 6;
    /** Dense four column row of a layout constraint, see {@link #constraintRow}. */
    public static final int CONSTRAINT_TOGGLE_WIDTH = 30;
    public static final int CONSTRAINT_TARGET_WIDTH = 88;
    public static final int CONSTRAINT_SIDE_WIDTH = 68;
    public static final int CONSTRAINT_MARGIN_WIDTH = 42;
    public static final int CONSTRAINT_GAP = 2;
    public static final int CONSTRAINT_ROW_PAD = 2;

    /** Label styles of the design: grey names, white values, compact font and dialog font. */
    public static final String LABEL_STYLE = "property-label";
    public static final String SECTION_STYLE = "property-section";
    /** Dialog scale, see {@link #dialogScale()}: same colours, the editor's default font. */
    public static final String LABEL_STYLE_LARGE = "property-label-large";
    public static final String SECTION_STYLE_LARGE = "property-section-large";
    /** Widest the label column may get at the dialog scale, where the labels are set in a bigger font. */
    public static final int DIALOG_LABEL_WIDTH = 170;
    public static final int DIALOG_ROW_PAD = 9;
    public static final int DIALOG_SECTION_PAD_TOP = 14;
    public static final int DIALOG_SECTION_PAD_BOTTOM = 9;

    private final VisTable table;
    private boolean firstRow;
    private int labelColumnWidth;
    private int minLabelWidth;
    private String labelStyle = LABEL_STYLE;
    private String sectionStyle = SECTION_STYLE;
    private int labelWidthCap = LABEL_WIDTH;
    private int rowPad = ROW_PAD;
    private int sectionPadTop = SECTION_PAD_TOP;
    private int sectionPadBottom = SECTION_PAD_BOTTOM;

    public PropertyGrid(VisTable table) {
        this.table = table;
        // Rows start at the left edge of the panel, under the section titles.
        table.left();
        boolean empty = table.getCells().size == 0;
        if (empty) {
            // Zero sized cell keeping the field column expandable, so a grid whose rows happen to be
            // all full width or all compact still fills its container instead of shrinking to the
            // width of its widest row. Without it such a grid collapses (labels ellipsize, full width
            // rows stop at their content) because no column of it would ever claim the spare width.
            table.add();
            table.add().expandX();
            table.row();
        }
        this.firstRow = empty;
    }

    /**
     * Switches the grid to the dialog scale: the editor's default font instead of the compact one,
     * a wider label column and more air between rows. For the settings dialog and friends, which
     * have far more room than the property box.
     */
    public PropertyGrid dialogScale() {
        labelStyle = LABEL_STYLE_LARGE;
        sectionStyle = SECTION_STYLE_LARGE;
        labelWidthCap = DIALOG_LABEL_WIDTH;
        rowPad = DIALOG_ROW_PAD;
        sectionPadTop = DIALOG_SECTION_PAD_TOP;
        sectionPadBottom = DIALOG_SECTION_PAD_BOTTOM;
        return this;
    }

    public static PropertyGrid on(VisTable table) {
        return new PropertyGrid(table);
    }

    /**
     * A grid nested in {@code parent}, see {@link #subGrid(Actor)}. It takes over the parent's label
     * column width, so its fields start at the same x as the parent's even though its own labels are
     * shorter.
     */
    public static PropertyGrid on(VisTable table, PropertyGrid parent) {
        return on(table).minLabelWidth(parent.labelColumnWidth());
    }

    /** Width the labels added so far need, which is what the label column ends up being. */
    public int labelColumnWidth() {
        return labelColumnWidth;
    }

    /** Keeps the label column at least this wide. Rows added before this call are not affected. */
    public PropertyGrid minLabelWidth(int width) {
        this.minLabelWidth = width;
        return this;
    }

    /** The table being filled, for the rare panel that needs a raw cell. */
    public VisTable getTable() {
        return table;
    }

    /**
     * Body table for a collapsible panel. CollapsibleWidget lays its child out at the child's own
     * preferred width, so unlike a top level panel the body is never stretched for us: it has to ask
     * for the full panel width itself, otherwise the field column would have nothing to expand into.
     */
    public static VisTable createPanelBody() {
        VisTable body = new VisTable() {
            @Override
            public float getPrefWidth() {
                return Math.max(super.getPrefWidth(), PANEL_WIDTH);
            }
        };
        body.pad(0, PANEL_PAD, PANEL_PAD, PANEL_PAD);
        return body;
    }

    /** Pads the panel itself. Only for top level panels, collapsible ones are padded by their box. */
    public PropertyGrid padPanel() {
        table.pad(PANEL_PAD);
        return this;
    }

    /**
     * Lets a cell fill the width it is given without ever asking for more, so long content cannot
     * widen the panel. Use it for every select box, text field or label whose content is unbounded.
     */
    public static <T extends Actor> Cell<T> elastic(Cell<T> cell) {
        return cell.growX().minWidth(0).prefWidth(0);
    }

    /**
     * A cell of the field column: it asks for {@link #FIELD_WIDTH} and takes every pixel the label
     * column does not need. This is the only column of the grid that expands, so the fields grow
     * leftwards up to the longest label instead of leaving a gap in the middle of the panel.
     */
    private <T extends Actor> Cell<T> fieldCell(Cell<T> cell) {
        return cell.growX().minWidth(0).prefWidth(FIELD_WIDTH).left().padRight(CONTENT_PAD);
    }

    /** One of the narrow fields of a paired or tripled row, growing with the column around it. */
    private static <T extends Actor> Cell<T> partCell(Cell<T> cell, int prefWidth) {
        return cell.growX().minWidth(0).prefWidth(prefWidth).height(FIELD_HEIGHT);
    }

    /**
     * A cell spanning the whole grid. It fills the width but does not expand: expanding would make
     * the label column grow too, which is exactly what the field column is supposed to claim.
     */
    private <T extends Actor> Cell<T> wideCell(Cell<T> cell) {
        return fullBleedCell(cell).padLeft(CONTENT_PAD).padRight(CONTENT_PAD);
    }

    /** Like {@link #wideCell}, but running to the panel edges: section titles and their rules. */
    private <T extends Actor> Cell<T> fullBleedCell(Cell<T> cell) {
        return cell.colspan(2).fillX().minWidth(0).prefWidth(0);
    }

    // ---- labels ----

    /** Label of the label column, left aligned under the section titles, ":" appended when missing. */
    public static VisLabel label(String text) {
        return ellipsized(StandardWidgetsFactory.createLabel(withColon(text), style(LABEL_STYLE), Align.left));
    }

    /** Inline label of a paired field, e.g. the "X" of "Position X Y". Kept to a letter or two. */
    public static VisLabel subLabel(String text) {
        return StandardWidgetsFactory.createLabel(text, style(LABEL_STYLE), Align.left);
    }

    /** Plain left aligned text, no trailing colon: a sentence rather than a field name. */
    public static VisLabel text(String text) {
        return StandardWidgetsFactory.createLabel(text, style(LABEL_STYLE), Align.left);
    }

    /** Left aligned read only value, for panels showing computed numbers. */
    public static VisLabel value(String text) {
        return StandardWidgetsFactory.createLabel(text, style(SECTION_STYLE), Align.left);
    }

    /** Read only value of unbounded length: stays on one line and ellipsizes instead of growing. */
    public static VisLabel valueEllipsized(String text) {
        return ellipsized(value(text));
    }

    /** Truncates with an ellipsis rather than spilling out of its cell. Never wraps to a second line. */
    private static VisLabel ellipsized(VisLabel label) {
        label.setEllipsis(true);
        return label;
    }

    /**
     * Label column content made of the label plus a small leading widget, e.g. the toggle that links
     * the two scale fields. Right aligned, so the label still ends flush with the rows around it.
     */
    public static VisTable labelWith(Actor leading, String text) {
        VisTable group = new VisTable();
        group.add(label(text)).left();
        group.add(leading).padLeft(SUB_LABEL_GAP).expandX().left();
        return group;
    }

    /** Section title, styled like the title bar of a collapsible panel. */
    public static VisLabel sectionLabel(String title) {
        return StandardWidgetsFactory.createLabel(title, style(SECTION_STYLE), Align.left);
    }

    /** Read only value at this grid's scale, for values a panel builds itself (slider readouts). */
    public VisLabel valueLabel(String text) {
        return StandardWidgetsFactory.createLabel(text, style(sectionStyle), Align.left);
    }

    /** {@link #valueLabel(String)} that ellipsizes instead of growing, for paths and other user data. */
    public VisLabel valueLabelEllipsized(String text) {
        return ellipsized(valueLabel(text));
    }

    private VisLabel gridLabel(String text) {
        return ellipsized(StandardWidgetsFactory.createLabel(withColon(text), style(labelStyle), Align.left));
    }

    private VisLabel gridText(String text) {
        return StandardWidgetsFactory.createLabel(text, style(labelStyle), Align.left);
    }

    private VisLabel gridSectionLabel(String title) {
        return StandardWidgetsFactory.createLabel(title, style(sectionStyle), Align.left);
    }

    // ---- rows ----

    /**
     * Section title spanning the whole grid, with a rule filling the leftover width and an optional
     * trailing widget (usually the switch enabling the whole section).
     */
    public PropertyGrid section(String title) {
        return section(title, null);
    }

    public PropertyGrid section(String title, Actor trailing) {
        if (!firstRow) {
            table.row().padTop(sectionPadTop);
        }
        VisTable head = new VisTable();
        head.add(gridSectionLabel(title)).left().padRight(LABEL_GAP);
        elastic(head.add(new Separator("menu"))).height(2).padRight(trailing == null ? 0 : LABEL_GAP);
        if (trailing != null) {
            head.add(trailing).right();
        }
        fullBleedCell(table.add(head));
        table.row().padTop(sectionPadBottom);
        firstRow = true; // the section rule already spaces the row that follows
        return this;
    }

    /** Label plus a field filling the field column. The field is sized by the grid, not its content. */
    public PropertyGrid row(String label, Actor field) {
        openRow();
        addLabel(label);
        fieldCell(table.add(field)).height(FIELD_HEIGHT);
        return this;
    }

    /** Label plus a widget keeping its own size: switches, tint buttons, short read only values. */
    public PropertyGrid rowCompact(String label, Actor field) {
        openRow();
        addLabel(label);
        table.add(field).maxWidth(FIELD_WIDTH).left().padRight(CONTENT_PAD);
        return this;
    }

    /**
     * A boolean property: its name in the label column and a pill switch pinned to the right edge of
     * the content, the same place the switch of a section title sits.
     */
    public PropertyGrid toggle(String label, Actor toggle) {
        openRow();
        addLabel(label);
        table.add(toggle).expandX().right().padRight(CONTENT_PAD);
        return this;
    }

    /**
     * A boolean whose name is a sentence rather than a field name, as in the settings pages: the text
     * takes the whole row and the switch is pinned to the right edge of the content.
     */
    public PropertyGrid toggleWide(String text, Actor toggle) {
        return toggleWide(text, toggle, null);
    }

    /** {@link #toggleWide(String, Actor)} with a tooltip covering both the text and the switch. */
    public PropertyGrid toggleWide(String text, Actor toggle, String tooltip) {
        openRow();
        // The sentence keeps its natural width so it cannot collapse under the switch, and shrinks
        // with an ellipsis rather than drawing over it when the container is too narrow.
        VisLabel label = ellipsized(gridText(text));
        VisTable row = new VisTable();
        row.add(label).growX().minWidth(0).left();
        row.add(toggle).right();
        wideCell(table.add(row));
        if (tooltip != null) {
            StandardWidgetsFactory.addTooltip(label, tooltip);
            StandardWidgetsFactory.addTooltip(toggle, tooltip);
        }
        return this;
    }

    /** Label, a slider filling the field column, and the live value it is showing. */
    public PropertyGrid sliderRow(String label, Actor slider, Actor value) {
        openRow();
        addLabel(label);
        VisTable row = new VisTable();
        elastic(row.add(slider));
        row.add(value).width(VALUE_WIDTH).right().padLeft(LABEL_GAP);
        fieldCell(table.add(row));
        return this;
    }

    /**
     * Two related booleans sharing one row, labelled inline like a paired field, so "Flip X Y" reads
     * the same way as "Position X Y" right above it.
     */
    public PropertyGrid togglePair(String label, String labelA, Actor toggleA, String labelB, Actor toggleB) {
        openRow();
        addLabel(label);
        VisTable pair = new VisTable();
        pair.add(subLabel(labelA)).width(SUB_LABEL_WIDTH).right().padRight(SUB_LABEL_GAP);
        togglePart(pair.add(toggleA));
        pair.add(subLabel(labelB)).width(SUB_LABEL_WIDTH).right().padLeft(PAIR_GAP).padRight(SUB_LABEL_GAP);
        togglePart(pair.add(toggleB));
        fieldCell(table.add(pair));
        return this;
    }

    /**
     * A switch inside a paired row: the cell grows with the column so the inline labels line up with
     * the paired fields above, while the switch itself keeps its own size and click area.
     */
    private static <T extends Actor> Cell<T> togglePart(Cell<T> cell) {
        return cell.expandX().minWidth(0).prefWidth(HALF_FIELD_WIDTH).left();
    }

    /** Label plus two narrow fields, each with its own inline label. */
    public PropertyGrid pair(String label, String labelA, Actor fieldA, String labelB, Actor fieldB) {
        openRow();
        addLabel(label);
        addPair(labelA, fieldA, labelB, fieldB);
        return this;
    }

    /**
     * Two narrow fields whose label column holds a widget instead of plain text: the switch of a
     * sensor side, or a {@link #labelWith(Actor, String)} group.
     */
    public PropertyGrid pair(Actor leading, String labelA, Actor fieldA, String labelB, Actor fieldB) {
        openRow();
        table.add(leading).minWidth(trackLabelWidth(leading)).maxWidth(labelWidthCap).fillX().left()
                .padLeft(CONTENT_PAD).padRight(LABEL_GAP);
        addPair(labelA, fieldA, labelB, fieldB);
        return this;
    }

    private void addPair(String labelA, Actor fieldA, String labelB, Actor fieldB) {
        VisTable pair = new VisTable();
        pair.add(subLabel(labelA)).width(SUB_LABEL_WIDTH).right().padRight(SUB_LABEL_GAP);
        partCell(pair.add(fieldA), HALF_FIELD_WIDTH);
        pair.add(subLabel(labelB)).width(SUB_LABEL_WIDTH).right().padLeft(PAIR_GAP).padRight(SUB_LABEL_GAP);
        partCell(pair.add(fieldB), HALF_FIELD_WIDTH);
        fieldCell(table.add(pair));
    }

    /** Label plus three narrow fields sharing the field column, for vector values like falloff. */
    public PropertyGrid triple(String label, Actor fieldA, Actor fieldB, Actor fieldC) {
        openRow();
        addLabel(label);
        VisTable triple = new VisTable();
        partCell(triple.add(fieldA), THIRD_FIELD_WIDTH).padRight(SUB_LABEL_GAP);
        partCell(triple.add(fieldB), THIRD_FIELD_WIDTH).padRight(SUB_LABEL_GAP);
        partCell(triple.add(fieldC), THIRD_FIELD_WIDTH);
        fieldCell(table.add(triple));
        return this;
    }

    /** A widget spanning the whole grid, left aligned. */
    public PropertyGrid wide(Actor actor) {
        openRow();
        table.add(actor).colspan(2).left().padLeft(CONTENT_PAD).padRight(CONTENT_PAD);
        return this;
    }

    /** A widget spanning and filling the whole grid. */
    public PropertyGrid wideFill(Actor actor) {
        openRow();
        wideCell(table.add(actor));
        return this;
    }

    /**
     * A widget spanning and filling the whole grid, keeping its own preferred width. Unlike
     * {@link #wideFill(Actor)} it lets its content decide how wide the panel has to be, which is what
     * a dialog wants: the property box instead has a fixed width that nothing may push.
     */
    public PropertyGrid wideContent(Actor actor) {
        openRow();
        table.add(actor).colspan(2).fillX().minWidth(0).padLeft(CONTENT_PAD).padRight(CONTENT_PAD);
        return this;
    }

    /**
     * A widget spanning the grid and taking every pixel left, across and down. For the list that
     * makes up the body of a dialog, under the form that adds to it.
     */
    public PropertyGrid wideGrow(Actor actor) {
        openRow();
        table.add(actor).colspan(2).grow().minWidth(0).padLeft(CONTENT_PAD).padRight(CONTENT_PAD);
        return this;
    }

    /** A widget spanning and filling the whole grid, at a fixed height. */
    public PropertyGrid wideFill(Actor actor, int height) {
        openRow();
        wideCell(table.add(actor)).height(height);
        return this;
    }

    /**
     * A nested property grid spanning the panel. Unlike {@link #wideFill(Actor)} it gets no inset of
     * its own: the rows inside it already carry {@link #CONTENT_PAD}, so they line up with the rows
     * around it instead of being indented twice.
     */
    public PropertyGrid subGrid(Actor actor) {
        openRow();
        fullBleedCell(table.add(actor));
        return this;
    }

    /** A widget centered across the whole grid, for panels that are a message rather than a grid. */
    public PropertyGrid wideCentered(Actor actor) {
        openRow();
        table.add(actor).colspan(2).expandX().center().padLeft(CONTENT_PAD).padRight(CONTENT_PAD);
        return this;
    }

    /** Buttons on their own row, right aligned under the field column. */
    public PropertyGrid buttons(Actor... buttons) {
        openRow();
        VisTable group = new VisTable();
        for (int i = 0; i < buttons.length; i++) {
            group.add(buttons[i]).height(FIELD_HEIGHT).padLeft(i == 0 ? 0 : BUTTON_GAP);
        }
        table.add(group).colspan(2).right().padLeft(CONTENT_PAD).padRight(CONTENT_PAD);
        return this;
    }

    /**
     * A row in the field column whose content can be taken away without leaving a gap behind, for
     * properties that only apply to some entities.
     */
    public CollapsibleRow collapsibleField(Actor actor) {
        openRow();
        Cell<Actor> spacer = table.add().padLeft(CONTENT_PAD).padRight(LABEL_GAP); // holds the label column, stays empty
        Cell<Actor> cell = fieldCell(table.add(actor));
        return new CollapsibleRow(table, cell, spacer, actor, ROW_PAD);
    }

    /** A {@link #subGrid(Actor)} that can be taken away without leaving a gap behind. */
    public CollapsibleRow collapsibleSubGrid(Actor actor) {
        openRow();
        Cell<Actor> cell = fullBleedCell(table.add(actor));
        return new CollapsibleRow(table, cell, null, actor, SECTION_PAD_TOP);
    }

    /**
     * One side of a layout constraint: enable toggle, target, anchor side and margin. Rows like this
     * are denser than the label/field grid, so build them in a table of their own and drop it in
     * with {@link #wideFill(Actor)}.
     */
    public PropertyGrid constraintRow(Actor toggle, Actor target, Actor side, Actor margin) {
        openRow(CONSTRAINT_ROW_PAD);
        table.add(toggle).width(CONSTRAINT_TOGGLE_WIDTH).left();
        // the target holds entity names, so it takes whatever width the row has to spare
        partCell(table.add(target), CONSTRAINT_TARGET_WIDTH).padLeft(CONSTRAINT_GAP);
        table.add(side).width(CONSTRAINT_SIDE_WIDTH).height(FIELD_HEIGHT).fill().padLeft(CONSTRAINT_GAP);
        table.add(margin).width(CONSTRAINT_MARGIN_WIDTH).height(FIELD_HEIGHT).fill().padLeft(CONSTRAINT_GAP);
        return this;
    }

    /** Rule across the grid, for the rare split that is not a titled section. */
    public PropertyGrid separator() {
        if (!firstRow) {
            table.row();
        }
        // addSeparator() pads itself and opens the next row, so set the spacing on the cell.
        table.addSeparator().colspan(2).padTop(sectionPadTop).padBottom(0);
        table.row().padTop(sectionPadBottom);
        firstRow = true;
        return this;
    }

    private void openRow() {
        openRow(rowPad);
    }

    private void openRow(int padTop) {
        if (!firstRow) {
            table.row().padTop(padTop);
        }
        firstRow = false;
    }

    /**
     * Adds the label column cell. Its minimum width is left at the label's own width on purpose: if a
     * row ever demands more than the panel can give, the field column is the one that has to shrink,
     * never the label, otherwise the text would ellipsize while there is still room next to it.
     */
    private void addLabel(String text) {
        VisLabel label = gridLabel(text);
        table.add(label).minWidth(trackLabelWidth(label)).maxWidth(labelWidthCap).left()
                .padLeft(CONTENT_PAD).padRight(LABEL_GAP);
    }

    /**
     * Records how wide the label column has to be and returns the minimum width of this cell: its own
     * label at least, and never less than an inherited {@link #minLabelWidth(int)}.
     */
    private int trackLabelWidth(Actor label) {
        int width = label instanceof Layout ? (int) Math.ceil(((Layout) label).getPrefWidth()) : 0;
        labelColumnWidth = Math.max(labelColumnWidth, Math.min(width, labelWidthCap));
        return Math.max(Math.min(width, labelWidthCap), minLabelWidth);
    }

    private static String withColon(String text) {
        if (text == null || text.isEmpty()) return "";
        String trimmed = text.trim();
        return trimmed.endsWith(":") ? trimmed : trimmed + ":";
    }

    /** Falls back to the plain small label style when a panel runs against a skin without ours. */
    public static String style(String style) {
        return VisUI.getSkin().has(style, Label.LabelStyle.class) ? style : "small";
    }

    /**
     * Handle on a row that can hide its content completely: the cell keeps its place in the grid but
     * takes no height and no padding while empty, so nothing shows where the row would be.
     */
    public static final class CollapsibleRow {
        private final VisTable table;
        private final Cell<Actor> cell;
        private final Cell<Actor> spacer;
        private final Actor actor;
        private final int padTop;

        private CollapsibleRow(VisTable table, Cell<Actor> cell, Cell<Actor> spacer, Actor actor, int padTop) {
            this.table = table;
            this.cell = cell;
            this.spacer = spacer;
            this.actor = actor;
            this.padTop = padTop;
        }

        public void setContentVisible(boolean visible) {
            if ((cell.getActor() != null) == visible) return;
            cell.setActor(visible ? actor : null).padTop(visible ? padTop : 0);
            // the empty label cell of the row would keep the padding alive on its own
            if (spacer != null) spacer.padTop(visible ? padTop : 0);
            table.invalidateHierarchy();
        }

        public boolean isContentVisible() {
            return cell.getActor() != null;
        }
    }
}
