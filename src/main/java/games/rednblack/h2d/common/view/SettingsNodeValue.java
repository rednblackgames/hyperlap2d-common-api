package games.rednblack.h2d.common.view;

import com.kotcrab.vis.ui.widget.VisTable;
import games.rednblack.puremvc.Facade;

public abstract class SettingsNodeValue<T> {

    /**
     * Air around a section title in a settings panel. The panel is a surface of its own, so its
     * sections are blocks with room to breathe rather than headings packed into a dialog.
     */
    public static final int SECTION_PAD_TOP = 22;
    public static final int SECTION_PAD_BOTTOM = 11;

    private final VisTable contentTable = new VisTable();
    private T settings;
    private final String name;
    protected Facade facade;

    public SettingsNodeValue(String name, Facade facade) {
        this.name = name;
        contentTable.top().left();
        this.facade = facade;
    }

    public abstract void translateSettingsToView();
    public abstract void translateViewToSettings();
    public abstract boolean validateSettings();
    public abstract boolean requireRestart();

    public VisTable getContentTable() {
        return contentTable;
    }
    public String getName() {
        return name;
    }

    public void setSettings(T settings) {
        this.settings = settings;
    }

    public T getSettings() {
        return settings;
    }
}