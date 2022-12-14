/*
 * @(#)SVGRectRadiusHandle.java  2.0  2008-05-11
 *
 * Copyright (c) 2006-2008 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */

package org.jhotdraw.samples.svg.figures;

import javax.swing.undo.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.geom.*;
import org.jhotdraw.util.*;
import org.jhotdraw.undo.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.*;

/**
 * A Handle to manipulate the radius of a round lead rectangle.
 *
 * @author  Werner Randelshofer
 * @version 2.0 2008-05-11 Added keyboard support. Handle attributes are
 * now retrieved from DrawingEditor.
 * <br>1.0 2006-12-10 Adapted from RoundRectangleHandle.
 */
public class SVGRectRadiusHandle extends AbstractHandle {
    private final static boolean DEBUG = false;
    private static final int OFFSET = 6;
    private Dimension2DDouble originalArc2D;
    CompositeEdit edit;
    
    /** Creates a new instance. */
    public SVGRectRadiusHandle(Figure owner) {
        super(owner);
    }
    
   /**
     * Draws this handle.
     */
    @Override
    public void draw(Graphics2D g) {
        if (getEditor().getTool().supportsHandleInteraction()) {
            drawDiamond(g,
                    (Color) getEditor().getHandleAttribute(HandleAttributeKeys.ATTRIBUTE_HANDLE_FILL_COLOR),
                    (Color) getEditor().getHandleAttribute(HandleAttributeKeys.ATTRIBUTE_HANDLE_STROKE_COLOR));
        } else {
            drawDiamond(g,
                    (Color) getEditor().getHandleAttribute(HandleAttributeKeys.ATTRIBUTE_HANDLE_FILL_COLOR_DISABLED),
                    (Color) getEditor().getHandleAttribute(HandleAttributeKeys.ATTRIBUTE_HANDLE_STROKE_COLOR_DISABLED));
        }
    }
    
    protected Rectangle basicGetBounds() {
        Rectangle r = new Rectangle(locate());
        r.grow(getHandlesize() / 2 + 1, getHandlesize() / 2 + 1);
        return r;
    }
    
    private Point locate() {
        SVGRectFigure owner = (SVGRectFigure) getOwner();
        Rectangle2D.Double r = owner.getBounds();
        Point2D.Double p = new Point2D.Double(
                r.x + owner.getArcWidth(), 
                r.y + owner.getArcHeight()
                );
        if (TRANSFORM.get(owner) != null) {
            TRANSFORM.get(owner).transform(p, p);
        }
        return view.drawingToView(p);
    }
    
    public void trackStart(Point anchor, int modifiersEx) {
        SVGRectFigure svgRect = (SVGRectFigure) getOwner();
        originalArc2D = svgRect.getArc();
    }
    
    public void trackStep(Point anchor, Point lead, int modifiersEx) {
        int dx = lead.x - anchor.x;
        int dy = lead.y - anchor.y;
        SVGRectFigure svgRect = (SVGRectFigure) getOwner();
        svgRect.willChange();
        Point2D.Double p = view.viewToDrawing(lead);
        if (TRANSFORM.get(svgRect) != null) {
            try {
                TRANSFORM.get(svgRect).inverseTransform(p, p);
            } catch (NoninvertibleTransformException ex) {
                if (DEBUG) ex.printStackTrace();
            }
        }
        Rectangle2D.Double r = svgRect.getBounds();
        svgRect.setArc(p.x - r.x, p.y - r.y);
        svgRect.changed();
    }
    public void trackEnd(Point anchor, Point lead, int modifiersEx) {
        final SVGRectFigure svgRect = (SVGRectFigure) getOwner();
        final Dimension2DDouble oldValue = originalArc2D;
        final Dimension2DDouble newValue = svgRect.getArc();
        fireUndoableEditHappened(new SVGRectRadiusUndoableEdit(svgRect, oldValue, newValue));
   }
    @Override
    public void keyPressed(KeyEvent evt) {
        SVGRectFigure owner = (SVGRectFigure) getOwner();
        Dimension2DDouble oldArc = new Dimension2DDouble(owner.getArcWidth(), owner.getArcHeight());
        Dimension2DDouble newArc = new Dimension2DDouble(owner.getArcWidth(), owner.getArcHeight());
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (newArc.height > 0) {
                    newArc.height = Math.max(0, newArc.height - 1);
                }
                evt.consume();
                break;
            case KeyEvent.VK_DOWN:
                newArc.height += 1;
                evt.consume();
                break;
            case KeyEvent.VK_LEFT:
                if (newArc.width > 0) {
                    newArc.width = Math.max(0, newArc.width - 1);
                }
                evt.consume();
                break;
            case KeyEvent.VK_RIGHT:
                newArc.width += 1;
                evt.consume();
                break;
        }
        if (!newArc.equals(oldArc)) {
            owner.willChange();
            owner.setArc(newArc.width, newArc.height);
            owner.changed();
            fireUndoableEditHappened(new SVGRectRadiusUndoableEdit(owner, oldArc, newArc));
        }
    }
    @Override
    public String getToolTipText(Point p) {
        return ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels").//
                getString("handle.roundRectangleRadius.toolTipText");
        }
}
