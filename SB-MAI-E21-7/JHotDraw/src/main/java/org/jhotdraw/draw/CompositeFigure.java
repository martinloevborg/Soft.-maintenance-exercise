/*
 * @(#)CompositeFigure.java  2.0.1  2007-12-20
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

import org.jhotdraw.util.*;
import java.beans.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import java.io.*;
import org.jhotdraw.geom.*;

/**
 * A CompositeFigure is composed of several child Figures.
 * A CompositeFigure can be laid out using a Layouter.
 * <p>
 * Design pattern:<br>
 * Name: Composite.<br>
 * Role: Composite.<br>
 * Partners: {@link Figure} as Component. 
 * <p>
 * Design pattern:<br>
 * Name: Strategy.<br>
 * Role: Context.<br>
 * Partners: {@link Layouter} as Strategy.
 *
 * @author Werner Randelshofer
 * @version 2.1 2007-12-20 Clarified purpose of basicAdd/basicRemove methods. 
 * Added method indexOf.
 * <br>2.0 2007-07-17 Added support for CompositeFigureListener.
 * CompositeFigure is now streamlined with the java.util.List<Figure>
 * interface. 
 * <br>1.0 27. Januar 2006 Created.
 */
public interface CompositeFigure extends Figure {
    /**
     * The value of this attribute is a Insets2D.Double object.
     */
    public final static AttributeKey<Insets2D.Double> LAYOUT_INSETS = new AttributeKey<Insets2D.Double>("layoutInsets", Insets2D.Double.class, new Insets2D.Double());
    
    /**
     * Adds a child to the figure.
     * <p>
     * This is a convenience method for {@code add(getChildCount(), child);}
     * <p>
     * This method calls {@code figureAdded} on all registered
     * {@code CompositeFigureListener}s.
     * 
     * @return {@code true} if this CompositeFigure changed as a result of the
     *         call
     */
    public boolean add(Figure child);
    /**
     * Adds a child to the figure at the specified index.
     * <p>
     * This method calls {@code figureAdded} on all registered
     * {@code CompositeFigureListener}s.
     */
    public void add(int index, Figure child);
    /**
     * Adds a child to the figure without firing events.
     * <p>
     * This method can be used to reinsert a child figure which has been
     * temporarily removed from this CompositeFigure (for example to reorder
     * the sequence of the children) and to efficiently build a drawing from 
     * an {@link InputFormat}.
     * 
     * This is a convenience method for calling
     * {@code basicAdd(getChildCount(), child);}.
     */
    public void basicAdd(Figure child);
    /**
     * Adds a child to the figure at the specified index without
     * firing events.
     * <p>
     * This method can be used to reinsert a child figure which has been
     * temporarily removed from this CompositeFigure (for example to reorder
     * the sequence of the children) and to efficiently build a drawing from 
     * an {@link InputFormat}.
     */
    public void basicAdd(int index, Figure child);
    /**
     * Removes the specified child.
     * Returns true, if the Figure contained the removed child.
     * <p>
     * This is a convenience method for calling 
     * {@code removeChild(getChildren().indexOf(child));}
     * <p>
     * This method calls {@code figureRemoved} on all registered
     * {@code CompositeFigureListener}'s.
     */
    public boolean remove(Figure child);
    /**
     * Removes the child at the specified index.
     * Returns the removed child figure.
     * <p>
     * Calls {@code figureRemoved} on all registered
     * {@code CompositeFigureListener}'s.
     */
    public Figure removeChild(int index);
    /**
     * Removes all children from the composite figure.
     * <p>
     * This is a convenience method for 
     * {@code while(getChildCount() > 0) removeChild(0); }
     */
    public void removeAllChildren();
    /**
     * Removes the specified child without firing events.
     * <p>
     * This method can be used to temporarily remove a child from this 
     * CompositeFigure (for example to reorder the sequence of the children).
     * <p>
     * This is a convenience method for calling 
     * {@code basicRemove(indexOf(child));}.
     * <p>
     * Returns the index of the removed figure. Returns -1 if the
     * figure was not a child of this CompositeFigure.
     */
    public int basicRemove(Figure child);
    /**
     * Removes the child at the specified index without firing events.
     * <p>
     * This method can be used to temporarily remove a child from this 
     * CompositeFigure (for example to reorder the sequence of the children).
     * <p>
     * Returns the removed child figure.
     */
    public Figure basicRemoveChild(int index);
    /**
     * Removes all children from the composite figure without firing events.
     * <p>
     * This method can be used to temporarily remove a child from this 
     * CompositeFigure (for example to reorder the sequence of the children).
     * <p>
     * This is a convenience method for 
     * {@code while(getChildCount() > 0) basicRemoveChild(0); }
     */
    public void basicRemoveAllChildren();
    
    /**
     * Returns an unchangeable list view on the children.
     */
    public java.util.List<Figure> getChildren();
    
    /**
     * Returns the number of children.
     * <p>
     * This is a convenience method for calling 
     * {@code getChildren().size();}.
     */
    public int getChildCount();
    
    /**
     * Returns the child figure at the specified index.
     * <p>
     * This is a convenience method for calling 
     * {@code getChildren().get(index);}.
     */
    public Figure getChild(int index);
    /**
     * Returns the index of the specified child.
     * <p>
     * This is a convenience method for calling 
     * {@code getChildren().indexOf(index);}.
     * 
     * @return The index of the child, or -1 if the specified figure is not
     * a child of this CompositeFigure.
     */
    public int indexOf(Figure child);
    /**
     * Returns true if this composite figure contains the specified figure.
     * <p>
     * This is a convenience method for calling 
     * {@code getChildren().contains(f);}.
     */
    public boolean contains(Figure f);
    /**
     * Get a Layouter object which encapsulated a layout
     * algorithm for this figure. Typically, a Layouter
     * accesses the child components of this figure and arranges
     * their graphical presentation.
        *
     * @return layout strategy used by this figure
     */
    public Layouter getLayouter();
    /**
     * A layout algorithm is used to define how the child components
     * should be laid out in relation to each other. The task for
     * layouting the child components for presentation is delegated
     * to a Layouter which can be plugged in at runtime.
     */
    public void layout();
    /**
     * Set a Layouter object which encapsulated a layout
     * algorithm for this figure. Typically, a Layouter
     * accesses the child components of this figure and arranges
     * their graphical presentation. It is a good idea to set
     * the Layouter in the protected initialize() method
     * so it can be recreated if a GraphicalCompositeFigure is
     * read and restored from a StorableInput stream.
     *
     *
     * @param newValue	encapsulation of a layout algorithm.
     */
    public void setLayouter(Layouter newValue);
    
    /**
     * Adds a listener for this composite figure.
     */
    public void addCompositeFigureListener(CompositeFigureListener listener);
    
    /**
     * Removes a listener from this composite figure.
     */
    public void removeCompositeFigureListener(CompositeFigureListener listener);
}
