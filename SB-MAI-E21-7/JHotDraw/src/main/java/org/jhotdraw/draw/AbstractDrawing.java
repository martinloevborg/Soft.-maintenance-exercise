/*
 * @(#)AbstractDrawing.java  4.1  2007-12-16
 *
 * Copyright (c) 1996-2007 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.draw;

import org.jhotdraw.xml.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import java.util.*;
import java.io.*;

/**
 * AbstractDrawing.
 *
 * @author Werner Randelshofer
 * @version 4.1 2007-12-16 Drawing hold attributes on its own. 
 * <br>4.0 2007-07-17 Adapted to changes in Drawing interface. 
 * <br>3.0 2007-05-18 Don't fire UndoableEdit events when Figures
 * are added/removed from a Drawing. The
 * <br>2.2 2006-12-26 Support for InputFormat's and OutputFormat's added.
 * <br>2.1 2006-07-08 Extend AbstractBean.
 * <br>2.0.1 2006-02-06 Did ugly dirty fix for IndexOutOfBoundsException when
 * undoing removal of Figures.
 * <br>2.0 2006-01-14 Changed to support double precision coordinates.
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public abstract class AbstractDrawing extends AbstractAttributedCompositeFigure implements Drawing {
    private final static Object lock = new JPanel().getTreeLock();
    private transient FontRenderContext fontRenderContext;
    private LinkedList<InputFormat> inputFormats = new LinkedList<InputFormat>();
    private LinkedList<OutputFormat> outputFormats = new LinkedList<OutputFormat>();
    private final static boolean DEBUG = false;

    /** Creates a new instance. */
    public AbstractDrawing() {
    }

    public void addUndoableEditListener(UndoableEditListener l) {
        listenerList.add(UndoableEditListener.class, l);
    }

    public void removeUndoableEditListener(UndoableEditListener l) {
        listenerList.remove(UndoableEditListener.class, l);
    }

    /**
     *  Notify all listenerList that have registered interest for
     * notification on this event type.
     */
    @Override
    public void fireUndoableEditHappened(UndoableEdit edit) {
        UndoableEditEvent event = null;
        if (listenerList.getListenerCount() > 0) {
            // Notify all listeners that have registered interest for
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (event == null) {
                    event = new UndoableEditEvent(this, edit);
                }
                if (listeners[i] == UndoableEditListener.class) {
                    ((UndoableEditListener) listeners[i + 1]).undoableEditHappened(event);
                }
            }
        }
    }

    public FontRenderContext getFontRenderContext() {
        return fontRenderContext;
    }

    public void setFontRenderContext(FontRenderContext frc) {
        fontRenderContext = frc;
    }

    @Override
    public void read(DOMInput in) throws IOException {
        in.openElement("figures");
        for (int i = 0; i < in.getElementCount(); i++) {
            Figure f;
            add(f = (Figure) in.readObject(i));
        }
        in.closeElement();
    }

    @Override
    public void write(DOMOutput out) throws IOException {
        out.openElement("figures");
        for (Figure f : getChildren()) {
            out.writeObject(f);
        }
        out.closeElement();
    }

    /**
     * The drawing view synchronizes on the lock when drawing a drawing.
     */
    @Override
    public Object getLock() {
        return lock;
    }

    public void addInputFormat(InputFormat format) {
        inputFormats.add(format);
    }

    public void addOutputFormat(OutputFormat format) {
        outputFormats.add(format);
        if (DEBUG) {
            System.out.println(this + ".addOutputFormat(" + format + ")");
        }
    }

    public void setOutputFormats(java.util.List<OutputFormat> formats) {
        this.outputFormats = new LinkedList<OutputFormat>(formats);
    }

    public void setInputFormats(java.util.List<InputFormat> formats) {
        this.inputFormats = new LinkedList<InputFormat>(formats);
    }

    public java.util.List<InputFormat> getInputFormats() {
        return inputFormats;
    }

    public java.util.List<OutputFormat> getOutputFormats() {
        if (DEBUG) {
            System.out.println(this + ".getOutputFormats size:" + outputFormats.size());
        }
        return outputFormats;
    }

    @Override
    public Drawing getDrawing() {
        return this;
    }

    /*@Override
    public Rectangle2D.Double getDrawingArea() {
        Rectangle2D.Double drawingArea;

        Dimension2DDouble canvasSize = getCanvasSize();
        if (canvasSize != null) {
            drawingArea = new Rectangle2D.Double(
                    0d, 0d,
                    canvasSize.width, canvasSize.height);
        } else {
            drawingArea = super.getDrawingArea();
            drawingArea.add(0d, 0d);
            /*drawingArea = new Rectangle2D.Double(
                    0d, 0d,
                    canvasSize.width, canvasSize.height);* /
        }
        return drawingArea;
    }*/
}
