package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.kotcrab.vis.ui.util.highlight.Highlight;

/**
 * Represents single highlight.
 * @author Kotcrab
 * @since 1.1.2
 */
public class ConsoleHighlight extends Highlight {
    public enum TextFormat {
        NORMAL,
        UNDERLINE,
        STRIKE,
        BOLD,
        ITALIC
    }

    private Color color;
    private int start;
    private int end;
    private TextFormat textFormat;

    public ConsoleHighlight (Color color, int start, int end, TextFormat textFormat) {
        super(color, start, end);
        if (textFormat == null) throw new IllegalArgumentException("textFormat can't be null");
        if (color == null) throw new IllegalArgumentException("color can't be null");
        if (start >= end) throw new IllegalArgumentException("start can't be >= end: " + start + " >= " + end);
        this.color = color;
        this.start = start;
        this.end = end;
        this.textFormat = textFormat;
    }

    public Color getColor () {
        return color;
    }

    public int getStart () {
        return start;
    }

    public int getEnd () {
        return end;
    }

    @Override
    public int compareTo (Highlight o) {
        return getStart() - o.getStart();
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setColor(Color color) {
        if (color == null) throw new IllegalArgumentException("color can't be null");
        this.color = color;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public TextFormat getTextFormat() {
        return textFormat;
    }

    public void setTextFormat(TextFormat textFormat) {
        if (textFormat == null) throw new IllegalArgumentException("textFormat can't be null");
        this.textFormat = textFormat;
    }
}