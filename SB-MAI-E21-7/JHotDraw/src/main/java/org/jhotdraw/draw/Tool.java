/*
 * @(#)Tool.java  3.0  2008-05-26
 *
 * Copyright (c) 1996-2008 by the original authors of JHotDraw
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

import java.awt.*;
import java.awt.event.*;
/**
 * A tool defines a mode of the drawing view. All input events targeted to the
 * drawing view are forwarded to its current tool.
 * <p>
 * Tools inform listeners when they are done with an interaction by calling
 * the ToolListener's toolDone() method. The Tools are created once and reused.
 * They are initialized/deinitialized with activate()/deactivate().
 * <p>
 * Tools are used for user interaction. Unlike figures, a tool works with
 * the user interface coordinates of the DrawingView. The user interface 
 * coordinates are expressed in integer pixels.
 * <p>
 * A Tool forwards UndoableEdit events to the Drawing object onto which it
 * is performing changes.
 * <p>
 * Design pattern:<br>
 * Name: Mediator.<br>
 * Role: Colleague.<br>
 * Partners: {@link DrawingEditor} as Mediator, {@link Tool} as
 * Colleague.
 * <p>
 * Design pattern:<br>
 * Name: Model-View-Controller.<br>
 * Role: Controller.<br>
 * Partners: {@link DrawingView} as View, {@link Figure} as Model.
 * <p>
 * Design pattern:<br>
 * Name: Observer.<br>
 * Role: Subject.<br>
 * Partners: {@link ToolListener} as Observer.
 *
 * @author Werner Randelshofer
 * @version 3.0 2008-05-26 Added method supportsHandleInteraction.  
 * <br>2.0 2008-05-17 Added method getToolTipText. 
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public interface Tool extends MouseListener, MouseMotionListener, KeyListener {
    
    /**
     * Activates the tool for the given editor. This method is called
     * whenever the user switches to this tool.
     */
    public void activate(DrawingEditor editor);
    
    /**
     * Deactivates the tool. This method is called whenever the user
     * switches to another tool.
     */
    public void deactivate(DrawingEditor editor);

    /**
     * Adds a listener for this tool.
     */
    void addToolListener(ToolListener l);
    
    /**
     * Removes a listener for this tool.
     */
    void removeToolListener(ToolListener l);
    
    /**
     * Draws the tool.
     */
    void draw(Graphics2D g);
    
    /**
     * Deletes the selection.
     * Depending on the tool, this could be selected figures, selected points
     * or selected text.
     */
    public void editDelete();
    /**
     * Cuts the selection into the clipboard.
     * Depending on the tool, this could be selected figures, selected points
     * or selected text.
     */
    public void editCut();
    /**
     * Copies the selection into the clipboard.
     * Depending on the tool, this could be selected figures, selected points
     * or selected text.
     */
    public void editCopy();
    /**
     * Duplicates the selection.
     * Depending on the tool, this could be selected figures, selected points
     * or selected text.
     */
    public void editDuplicate();
    /**
     * Pastes the contents of the clipboard.
     * Depending on the tool, this could be selected figures, selected points
     * or selected text.
     */
    public void editPaste();
    
    /**
     * Returns the tooltip text for a mouse event on a drawing view.
     * 
     * @param view A drawing view.
     * @param evt A mouse event.
     * @return A tooltip text or null.
     */
    public String getToolTipText(DrawingView view, MouseEvent evt);
    
    /**
     * Returns true, if this tool lets the user interact with handles.
     * <p>
     * Handles may draw differently, if interaction is not possible.
     * 
     * @return True, if this tool supports interaction with the handles.
     */
    public boolean supportsHandleInteraction();
}
