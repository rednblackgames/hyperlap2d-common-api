package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.highlight.BaseHighlighter;
import com.kotcrab.vis.ui.util.highlight.Highlight;
import com.kotcrab.vis.ui.util.highlight.Highlighter;

/**
 * Text area implementation supporting highlighting words and scrolling in both X and Y directions.
 * <p>
 * For best scroll pane settings you should create scroll pane using {@link #createCompatibleScrollPane()}.
 * <p>
 * Note about overlapping highlights: this text area can handle overlapping highlights, highlights that starts earlier
 * have higher priority. If two highlights have the exactly the same start point, then it is undefined which highlight
 * will be used and depends on how array containing highlights will be sorted.
 * @author Kotcrab
 * @see Highlighter
 * @since 1.1.2
 */
public class ConsoleTextArea extends HighlightTextArea {
    private Array<Highlight> highlights = new Array<>();
    private Array<Chunk> renderChunks = new Array<>();
    private boolean chunkUpdateScheduled = true;
    private Color defaultColor = Color.WHITE;
    private Color defaultBackgroundColor = Color.CLEAR;
    private ConsoleHighlight.TextFormat defaultTextFormat = ConsoleHighlight.TextFormat.NORMAL;

    private BaseHighlighter highlighter;

    private float maxAreaWidth = 0;
    private float maxAreaHeight = 0;

    private Color tmpColor = new Color();
    private ConsoleStyle style;

    private Drawable selectionDrawable;
    private float selectionX, selectionY;
    private boolean renderSelection = false;

    public ConsoleTextArea (String text) {
        this(text, "default");
        init();
    }

    public ConsoleTextArea (String text, String styleName) {
        this(text, VisUI.getSkin().get(styleName, ConsoleStyle.class));
        init();
    }

    public ConsoleTextArea (String text, ConsoleStyle style) {
        super(text, style);
        init();
    }

    private void init() {
        softwrap = false;
        style = (ConsoleStyle) getStyle();
    }

    @Override
    void updateDisplayText () {
        super.updateDisplayText();
        processHighlighter();
    }

    @Override
    protected InputListener createInputListener() {
        return new TextAreaListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.V)
                    return true;
                return super.keyDown(event, keycode);
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                return false;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.V)
                    return true;
                return super.keyUp(event, keycode);
            }

            @Override
            protected void setCursorPosition (float x, float y) {
                moveOffset = -1;

                Drawable background = style.background;
                BitmapFont font = style.font;

                float height = getHeight();

                if (background != null) {
                    height -= background.getTopHeight();
                    x -= background.getLeftWidth();
                }
                x = Math.max(0, x);
                if (background != null) {
                    y -= background.getTopHeight();
                }

                float lineHeight = font.getLineHeight() - font.getDescent();
                cursorLine = (int) Math.floor((height - y) / lineHeight) + firstLineShowing;
                cursorLine = Math.max(0, Math.min(cursorLine, getLines() - 1));

                lastBlink = 0;
                cursorOn = false;
                cursor = Math.min(letterUnderCursor(x), text.length());
                updateCurrentLine();
            }
        };
    }

    @Override
    protected void calculateOffsets () {
        super.calculateOffsets();
        if (!chunkUpdateScheduled) return;
        chunkUpdateScheduled = false;
        highlights.sort();
        renderChunks.clear();

        String text = getText();

        Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
        GlyphLayout layout = layoutPool.obtain();
        boolean carryHighlight = false;
        for (int lineIdx = 0, highlightIdx = 0; lineIdx < linesBreak.size; lineIdx += 2) {
            int lineStart = linesBreak.items[lineIdx];
            int lineEnd = linesBreak.items[lineIdx + 1];
            int lineProgress = lineStart;
            float chunkOffset = 0;

            for (; highlightIdx < highlights.size; ) {
                Highlight highlight = highlights.get(highlightIdx);
                ConsoleHighlight.TextFormat textFormat = ConsoleHighlight.TextFormat.NORMAL;
                Color backgroundColor = defaultColor;
                if (highlight.getStart() > lineEnd) {
                    break;
                }

                if (highlight instanceof ConsoleHighlight) {
                    ConsoleHighlight consoleHighlight = (ConsoleHighlight) highlight;

                    textFormat = consoleHighlight.getTextFormat();
                    backgroundColor = consoleHighlight.getBackgroundColor();
                }

                if (highlight.getStart() == lineProgress || carryHighlight) {
                    renderChunks.add(new Chunk(text.substring(lineProgress, Math.min(highlight.getEnd(), lineEnd)), highlight.getColor(), backgroundColor, chunkOffset, lineIdx, textFormat));
                    lineProgress = Math.min(highlight.getEnd(), lineEnd);

                    if (highlight.getEnd() > lineEnd) {
                        carryHighlight = true;
                    } else {
                        carryHighlight = false;
                        highlightIdx++;
                    }
                } else {
                    //protect against overlapping highlights
                    boolean noMatch = false;
                    while (highlight.getStart() <= lineProgress) {
                        highlightIdx++;
                        if (highlightIdx >= highlights.size) {
                            noMatch = true;
                            break;
                        }
                        highlight = highlights.get(highlightIdx);
                        if (highlight.getStart() > lineEnd) {
                            noMatch = true;
                            break;
                        }
                    }
                    if (noMatch) break;
                    renderChunks.add(new Chunk(text.substring(lineProgress, highlight.getStart()), defaultColor, defaultBackgroundColor, chunkOffset, lineIdx, defaultTextFormat));
                    lineProgress = highlight.getStart();
                }

                Chunk chunk = renderChunks.peek();
                layout.setText(style.font, chunk.text);
                chunkOffset += layout.width;
                //current highlight needs to be applied to next line meaning that there is no other highlights that can be applied to currently parsed line
                if (carryHighlight) break;
            }

            if (lineProgress < lineEnd) {
                renderChunks.add(new Chunk(text.substring(lineProgress, lineEnd), defaultColor, defaultBackgroundColor, chunkOffset, lineIdx, defaultTextFormat));
            }
        }

        maxAreaWidth = 0;
        for (String line : text.split("\\n")) {
            layout.setText(style.font, line);
            maxAreaWidth = Math.max(maxAreaWidth, layout.width + 30);
        }

        layoutPool.free(layout);
        updateScrollLayout();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        try {
            super.draw(batch, parentAlpha);
        } catch (Exception ignore) {
            //Ignore any exception that may occurs while drawing this
        }
    }

    @Override
    protected void drawSelection(Drawable selection, Batch batch, BitmapFont font, float x, float y) {
        selectionDrawable = selection;
        selectionX = x;
        selectionY = y;
        renderSelection = true;
    }

    protected void drawSelectionLater(Drawable selection, Batch batch, BitmapFont font, float x, float y) {
        int i = firstLineShowing * 2;
        float offsetY = -font.getDescent();
        int minIndex = Math.min(cursor, selectionStart);
        int maxIndex = Math.max(cursor, selectionStart);
        while (i + 1 < linesBreak.size && i < (firstLineShowing + linesShowing) * 2) {

            int lineStart = linesBreak.get(i);
            int lineEnd = linesBreak.get(i + 1);

            if (!((minIndex < lineStart && minIndex < lineEnd && maxIndex < lineStart && maxIndex < lineEnd)
                    || (minIndex > lineStart && minIndex > lineEnd && maxIndex > lineStart && maxIndex > lineEnd))) {

                int start = Math.max(linesBreak.get(i), minIndex);
                int end = Math.min(linesBreak.get(i + 1), maxIndex);

                float selectionX = glyphPositions.get(start) - glyphPositions.get(linesBreak.get(i));
                float selectionWidth = glyphPositions.get(end) - glyphPositions.get(start);

                selection.draw(batch, x + selectionX + fontOffset, y - textHeight - font.getDescent() - offsetY, selectionWidth,
                        font.getLineHeight() - font.getDescent());
            }

            offsetY += font.getLineHeight() - font.getDescent();
            i += 2;
        }
    }

    @Override
    protected void drawCursor (Drawable cursorPatch, Batch batch, BitmapFont font, float x, float y) {
        float textOffset = cursor >= glyphPositions.size || cursorLine * 2 >= linesBreak.size ? 0
                : glyphPositions.get(cursor) - glyphPositions.get(linesBreak.items[cursorLine * 2]);
        cursorX = textOffset + fontOffset + font.getData().cursorX;
        cursorPatch.draw(batch, x + cursorX,
                y - font.getDescent() / 2 - (cursorLine - firstLineShowing + 1) * (font.getLineHeight() - font.getDescent()), cursorPatch.getMinWidth(),
                font.getLineHeight() - font.getDescent());
    }

    @Override
    protected void drawText (Batch batch, BitmapFont font, float x, float y) {
        maxAreaHeight = 0;
        float offsetY = font.getDescent();
        float parentAlpha = font.getColor().a;
        Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
        GlyphLayout layout = layoutPool.obtain();

        for (int i = firstLineShowing * 2; i < (firstLineShowing + linesShowing) * 2 && i < linesBreak.size; i += 2) {
            for (Chunk chunk : renderChunks) {
                if (chunk.lineIndex == i) {
                    layout.setText(font, chunk.text);
                    tmpColor.set(batch.getColor());

                    batch.setColor(chunk.backgroundColor.r, chunk.backgroundColor.g, chunk.backgroundColor.b, chunk.backgroundColor.a * parentAlpha);
                    style.underline.draw(batch, x + chunk.offsetX, y + offsetY - layout.height + font.getDescent(),
                            layout.width, layout.height - font.getDescent() * 2.5f);
                    batch.setColor(tmpColor);
                }
            }

            offsetY -= font.getLineHeight() - font.getDescent();
            maxAreaHeight += font.getLineHeight() - font.getDescent();
        }

        if (renderSelection) {
            renderSelection = false;
            batch.setColor(tmpColor);
            drawSelectionLater(selectionDrawable, batch, font, selectionX, selectionY);
        }

        maxAreaHeight = 0;
        offsetY = font.getDescent();
        for (int i = firstLineShowing * 2; i < (firstLineShowing + linesShowing) * 2 && i < linesBreak.size; i += 2) {
            for (Chunk chunk : renderChunks) {
                if (chunk.lineIndex == i) {
                    font.setColor(chunk.color);
                    font.getColor().a *= parentAlpha;
                    font.draw(batch, chunk.text, x + chunk.offsetX, y + offsetY);

                    if (style.underline != null && (chunk.textFormat == ConsoleHighlight.TextFormat.UNDERLINE
                            || chunk.textFormat == ConsoleHighlight.TextFormat.STRIKE)) {
                        layout.setText(font, chunk.text);
                        tmpColor.set(batch.getColor());
                        float underlineY = chunk.textFormat == ConsoleHighlight.TextFormat.STRIKE ? (layout.height - font.getDescent()) / 2f : layout.height - font.getDescent();
                        batch.setColor(chunk.color.r, chunk.color.g, chunk.color.b, chunk.color.a * parentAlpha);
                        style.underline.draw(batch, x + chunk.offsetX, y + offsetY - underlineY, layout.width, 1.4f);
                        batch.setColor(tmpColor);
                    }
                }
            }

            offsetY -= font.getLineHeight() - font.getDescent();
            maxAreaHeight += font.getLineHeight() - font.getDescent();
        }
        layoutPool.free(layout);
        maxAreaHeight += 30;
    }

    /**
     * Processes highlighter rules, collects created highlights and schedules text area displayed text update. This should be called
     * after highlighter rules has changed to update highlights.
     */
    public void processHighlighter () {
        if (highlights == null) return;
        highlights.clear();
        if (highlighter != null) highlighter.process(this, highlights);
        chunkUpdateScheduled = true;
    }

    /**
     * Changes highlighter of text area. Note that you don't have to call {@link #processHighlighter()} after changing
     * highlighter - you only have to call it when highlighter rules has changed.
     */
    public void setHighlighter (BaseHighlighter highlighter) {
        this.highlighter = highlighter;
        processHighlighter();
    }

    public BaseHighlighter getHighlighter () {
        return highlighter;
    }

    @Override
    public float getPrefWidth () {
        return maxAreaWidth + 5;
    }

    @Override
    public float getPrefHeight () {
        return maxAreaHeight + 5;
    }

    @Override
    public ScrollPane createCompatibleScrollPane () {
        ScrollPane scrollPane = super.createCompatibleScrollPane();
        scrollPane.setScrollingDisabled(false, false);
        return scrollPane;
    }

    private static class Chunk {
        String text;
        Color color;
        Color backgroundColor;
        float offsetX;
        int lineIndex;
        ConsoleHighlight.TextFormat textFormat;

        public Chunk (String text, Color color, Color backgroundColor, float offsetX, int lineIndex, ConsoleHighlight.TextFormat textFormat) {
            this.text = text;
            this.color = color;
            this.offsetX = offsetX;
            this.lineIndex = lineIndex;
            this.textFormat = textFormat;
            this.backgroundColor = backgroundColor;
        }
    }

    public static class ConsoleStyle extends VisTextFieldStyle {
        public Drawable underline;

        public ConsoleStyle() {
        }

        public ConsoleStyle(BitmapFont font, Color fontColor, Drawable cursor, Drawable selection, Drawable background) {
            super(font, fontColor, cursor, selection, background);
        }

        public ConsoleStyle(ConsoleStyle style) {
            super(style);
            this.underline = style.underline;
        }
    }
}