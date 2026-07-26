package games.rednblack.h2d.common.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * A data table: a sunken container holding a header band of column names and one row per item, with
 * an action widget in the last column. Rows are striped and light up under the pointer. The list half
 * of a panel, the way {@link PropertyGrid} is the form half.
 * <pre>
 * ListTable list = new ListTable("Animation", "From", "To");
 * list.item("walk", "0", "7").action(trashButton).onClick(() -> loadIntoForm("walk"));
 * </pre>
 * Every row is made of cells of this one table, so the columns line up by construction and are as
 * wide as their content needs: a table belongs in a dialog that sizes itself, not in the fixed width
 * property box.
 */
public class ListTable extends VisTable {

    public static final int ROW_HEIGHT = 26;
    public static final int HEADER_HEIGHT = 24;
    /** Padding inside a cell. The column names carry the same, so they sit over their values. */
    public static final int CELL_PAD = 10;
    /** Least width of a column, so a short value still leaves its column readable. */
    public static final int COLUMN_MIN_WIDTH = 54;
    /** Inset of the rows from the container's border, which keeps its rounded corners visible. */
    public static final int BORDER_PAD = 3;

    private static final String TABLE_BG = "table-bg";
    private static final String HEADER_BG = "table-header";
    private static final String ROW_BG = "table-row";
    private static final String ROW_BG_ALT = "table-row-alt";
    private static final String ROW_BG_OVER = "table-row-over";
    /** Same fill plus the accent bar, which belongs to the row and so only to its first cell. */
    private static final String ROW_BG_OVER_FIRST = "table-row-over-first";

    private final int columns;
    private int items;

    public ListTable(String... headers) {
        columns = headers.length;
        top().left();
        setBackground(drawable(TABLE_BG));
        pad(BORDER_PAD);

        Drawable headerBackground = drawable(HEADER_BG);
        for (String header : headers) {
            add(headerCell(headerBackground, headerLabel(header)))
                    .growX().fill().height(HEADER_HEIGHT).minWidth(COLUMN_MIN_WIDTH);
        }
        // the header band runs across the action column too, so it reads as one band. Every cell
        // fills its column: left unfilled, a column wider than its content leaves a hole in the row
        // that no cell covers, which breaks both the band and the hover.
        add(headerCell(headerBackground, null)).fill().height(HEADER_HEIGHT);
        row();
    }

    /** One item, a value per column. Missing values are left empty. */
    public ItemRow item(String... values) {
        Drawable background = drawable(items++ % 2 == 0 ? ROW_BG : ROW_BG_ALT);
        Array<VisTable> cells = new Array<>(columns + 1);
        for (int i = 0; i < columns; i++) {
            VisTable cell = cell(background);
            cell.add(valueLabel(i < values.length ? values[i] : "")).growX().left();
            cells.add(cell);
            add(cell).growX().fill().height(ROW_HEIGHT).minWidth(COLUMN_MIN_WIDTH);
        }
        VisTable actionCell = cell(background);
        cells.add(actionCell);
        add(actionCell).fill().height(ROW_HEIGHT);
        row();
        return new ItemRow(cells, background);
    }

    /** Replaces the whole table with a centered message, for the "nothing here" states. */
    public ListTable message(String text) {
        clearChildren();
        items = 0;
        add(StandardWidgetsFactory.createLabel(text,
                PropertyGrid.style(PropertyGrid.LABEL_STYLE_LARGE), Align.center))
                .growX().center().pad(CELL_PAD);
        row();
        return this;
    }

    private static VisTable headerCell(Drawable background, VisLabel label) {
        VisTable cell = new VisTable();
        cell.setBackground(background);
        if (label != null) {
            cell.add(label).growX().left().padLeft(CELL_PAD).padRight(CELL_PAD);
        }
        return cell;
    }

    private static VisTable cell(Drawable background) {
        VisTable cell = new VisTable();
        cell.setBackground(background);
        cell.pad(0, CELL_PAD, 0, CELL_PAD);
        return cell;
    }

    private static Drawable drawable(String name) {
        return VisUI.getSkin().getDrawable(name);
    }

    private static VisLabel headerLabel(String text) {
        return StandardWidgetsFactory.createLabel(text,
                PropertyGrid.style(PropertyGrid.LABEL_STYLE_LARGE), Align.left);
    }

    private static VisLabel valueLabel(String text) {
        VisLabel label = StandardWidgetsFactory.createLabel(text,
                PropertyGrid.style(PropertyGrid.SECTION_STYLE_LARGE), Align.left);
        label.setEllipsis(true); // values are user data: truncate rather than widen the column
        return label;
    }

    /** Handle on the row just added, to hang an action widget or a click on it. */
    public static final class ItemRow {
        private final Array<VisTable> cells;
        private final Drawable background;

        private final HoverListener hoverListener = new HoverListener();

        private ItemRow(Array<VisTable> cells, Drawable background) {
            this.cells = cells;
            this.background = background;
            highlightOnHover();
        }

        /** Widget of the last column, typically the delete button. */
        public ItemRow action(Actor actor) {
            cells.peek().add(actor).right();
            hoverListener.attach(actor); // hovering the button keeps its row lit
            return this;
        }

        /** Makes the row clickable, for tables that load the clicked item back into the form above. */
        public ItemRow onClick(Runnable action) {
            ClickListener listener = new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    action.run();
                }
            };
            for (VisTable cell : cells) {
                cell.addListener(listener);
                StandardWidgetsFactory.addFingerCursor(cell);
            }
            return this;
        }

        private void highlightOnHover() {
            for (VisTable cell : cells) {
                hoverListener.attach(cell);
            }
        }

        /** Lights the whole row while the pointer is anywhere over it, cell borders included. */
        private final class HoverListener extends InputListener {
            private final Drawable over = drawable(ROW_BG_OVER);
            private final Drawable overFirst = drawable(ROW_BG_OVER_FIRST);

            void attach(Actor actor) {
                actor.addListener(this);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) setHovered(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // moving from one cell of this row to another is not leaving the row
                if (pointer == -1 && !isInRow(toActor)) setHovered(false);
            }

            private void setHovered(boolean hovered) {
                for (int i = 0; i < cells.size; i++) {
                    // the accent bar marks the start of the row, so it goes on the first cell only
                    cells.get(i).setBackground(!hovered ? background : i == 0 ? overFirst : over);
                }
            }

            private boolean isInRow(Actor actor) {
                if (actor == null) return false;
                for (VisTable cell : cells) {
                    if (actor == cell || actor.isDescendantOf(cell)) return true;
                }
                return false;
            }
        }
    }
}
