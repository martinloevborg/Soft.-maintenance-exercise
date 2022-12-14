/*
 * @(#)QuadTreeDrawing.java  2.2.2  2009-04-04
 *
 * Copyright (c) 1996-2009 by the original authors of JHotDraw
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

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.geom.Dimension2DDouble;
import org.jhotdraw.geom.QuadTree;
import java.awt.*;
import java.awt.geom.*;
import org.jhotdraw.util.*;
import java.util.*;
import org.jhotdraw.app.JHotDrawFeatures;
import org.jhotdraw.geom.Geom;

/**
 * QuadTreeDrawing uses a QuadTree2DDouble to improve responsiveness of drawings
 * which contain many children.
 *
 * @author Werner Randelshofer
 * @version 2.2.2 2009-04-04 draw() method did not check isVisible() property.
 * <br>2.2.1 2008-03-26 Fixed NullPointerException when setting the
 * canvas size. Fixed cloning of quadTree and canvasSize. 
 * <br>2.2 2007-04-09 Added methods setCanvasSize, getCanvasSize.
 * <br>2.1 2007-02-09 Moved FigureListener and UndoableEditListener into
 * inner class.
 * <br>2.0 2006-01-14 Changed to support double precision coordinates.
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public class QuadTreeDrawing extends AbstractDrawing {

    private QuadTree<Figure> quadTree = new QuadTree<Figure>();
    private boolean needsSorting = false;
    private Dimension2DDouble canvasSize;

    @Override
    public int indexOf(Figure figure) {
        return children.indexOf(figure);
    }

    @Override
    public void basicAdd(int index, Figure figure) {
        super.basicAdd(index, figure);
        quadTree.add(figure, figure.getDrawingArea());
        needsSorting = true;
    }

    @Override
    public Figure basicRemoveChild(int index) {
        Figure figure = getChild(index);
        quadTree.remove(figure);
        needsSorting = true;
        super.basicRemoveChild(index);
        return figure;
    }

    @Override
    public void draw(Graphics2D g) {
        Rectangle2D clipBounds = g.getClipBounds();
        if (clipBounds != null) {
            Collection<Figure> c = quadTree.findIntersects(clipBounds);
            Collection<Figure> toDraw = sort(c);
            draw(g, toDraw);
        } else {
            draw(g, children);
        }
    }

    /**
     * Implementation note: Sorting can not be done for orphaned children.
     */
    public java.util.List<Figure> sort(Collection<? extends Figure> c) {
        ensureSorted();
        ArrayList<Figure> sorted = new ArrayList<Figure>(c.size());
        for (Figure f : children) {
            if (c.contains(f)) {
                sorted.add(f);
            }
        }
        return sorted;
    }

    public void draw(Graphics2D g, Collection<Figure> c) {
        for (Figure f : c) {
            if (f.isVisible()) {
                f.draw(g);
            }
        }
    }

    public java.util.List<Figure> getChildren(Rectangle2D.Double bounds) {
        return new LinkedList<Figure>(quadTree.findInside(bounds));
    }

    @Override
    public java.util.List<Figure> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public Figure findFigureInside(Point2D.Double p) {
        Collection<Figure> c = quadTree.findContains(p);
        for (Figure f : getFiguresFrontToBack()) {
            if (c.contains(f) && f.contains(p)) {
                return f.findFigureInside(p);
            }
        }
        return null;

    }

    /**
     * Returns an iterator to iterate in
     * Z-order front to back over the children.
     */
    public java.util.List<Figure> getFiguresFrontToBack() {
        ensureSorted();
        return new ReversedList<Figure>(children);
    }

    public Figure findFigure(Point2D.Double p) {
        Collection<Figure> c = quadTree.findContains(p);
        switch (c.size()) {
            case 0:
                return null;
            case 1: {
                Figure f = c.iterator().next();
                return (f.contains(p)) ? f : null;
            }
            default: {
                for (Figure f : getFiguresFrontToBack()) {
                    if (c.contains(f) && f.contains(p)) {
                        return f;
                    }
                }
                return null;
            }
        }
    }

    public Figure findFigureExcept(Point2D.Double p, Figure ignore) {
        Collection<Figure> c = quadTree.findContains(p);
        switch (c.size()) {
            case 0: {
                return null;
            }
            case 1: {
                Figure f = c.iterator().next();
                return (f == ignore || !f.contains(p)) ? null : f;
            }
            default: {
                for (Figure f : getFiguresFrontToBack()) {
                    if (f != ignore && f.contains(p)) {
                        return f;
                    }
                }
                return null;
            }
        }
    }

    public Figure findFigureExcept(Point2D.Double p, Collection<? extends Figure> ignore) {
        Collection<Figure> c = quadTree.findContains(p);
        switch (c.size()) {
            case 0: {
                return null;
            }
            case 1: {
                Figure f = c.iterator().next();
                return (!ignore.contains(f) || !f.contains(p)) ? null : f;
            }
            default: {
                for (Figure f : getFiguresFrontToBack()) {
                    if (!ignore.contains(f) && f.contains(p)) {
                        return f;
                    }
                }
                return null;
            }
        }
    }

    public Figure findFigureBehind(Point2D.Double p, Figure figure) {
        boolean isBehind = false;
        for (Figure f : getFiguresFrontToBack()) {
            if (isBehind) {
                if (f.isVisible() && f.contains(p)) {
                    return f;
                }
            } else {
                isBehind = figure == f;
            }
        }
        return null;
    }

    public Figure findFigureBehind(Point2D.Double p, Collection<? extends Figure> children) {
        int inFrontOf = children.size();
        for (Figure f : getFiguresFrontToBack()) {
            if (inFrontOf == 0) {
                if (f.isVisible() && f.contains(p)) {
                    return f;
                }
            } else {
                if (children.contains(f)) {
                    inFrontOf--;
                }
            }
        }
        return null;
    }

    public java.util.List<Figure> findFigures(Rectangle2D.Double r) {
        LinkedList<Figure> c = new LinkedList<Figure>(quadTree.findIntersects(r));
        switch (c.size()) {
            case 0:
            // fall through
            case 1:
                return c;
            default:
                return sort(c);
        }
    }

    public java.util.List<Figure> findFiguresWithin(Rectangle2D.Double bounds) {
        LinkedList<Figure> contained = new LinkedList<Figure>();
        for (Figure f : children) {
            Rectangle2D.Double r = f.getBounds();
            if (AttributeKeys.TRANSFORM.get(f) != null) {
                Rectangle2D rt = AttributeKeys.TRANSFORM.get(f).createTransformedShape(r).getBounds2D();
                r = (rt instanceof Rectangle2D.Double) ? (Rectangle2D.Double) rt : new Rectangle2D.Double(rt.getX(), rt.getY(), rt.getWidth(), rt.getHeight());
            }
            if (f.isVisible() && Geom.contains(bounds, r)) {
                contained.add(f);
            }
        }
        return contained;
    }

    @Override
    @FeatureEntryPoint(JHotDrawFeatures.ARRANGE)
    public void bringToFront(Figure figure) {
        if (children.remove(figure)) {
            children.add(figure);
            needsSorting = true;
            fireAreaInvalidated(figure.getDrawingArea());
        }
    }

    @Override
    @FeatureEntryPoint(JHotDrawFeatures.ARRANGE)
    public void sendToBack(Figure figure) {
        if (children.remove(figure)) {
            children.add(0, figure);
            needsSorting = true;
            fireAreaInvalidated(figure.getDrawingArea());
        }
    }

    @Override
    public boolean contains(Figure f) {
        return children.contains(f);
    }

    /**
     * Ensures that the children are sorted in z-order sequence.
     */
    private void ensureSorted() {
        if (needsSorting) {
            Collections.sort(children, FigureLayerComparator.INSTANCE);
            needsSorting = false;
        }
    }

    @Override
    protected void setAttributeOnChildren(AttributeKey key, Object newValue) {
        // empty
    }

    public void setCanvasSize(Dimension2DDouble newValue) {
        Dimension2DDouble oldValue = canvasSize;
        canvasSize = (newValue == null) ? null : (Dimension2DDouble) newValue.clone();
        firePropertyChange("canvasSize", oldValue, newValue);
    }

    public Dimension2DDouble getCanvasSize() {
        return (canvasSize == null) ? null : (Dimension2DDouble) canvasSize.clone();
    }

    public QuadTreeDrawing clone() {
        QuadTreeDrawing that = (QuadTreeDrawing) super.clone();
        that.canvasSize = (this.canvasSize == null) ? null : (Dimension2DDouble) this.canvasSize.clone();
        that.quadTree = new QuadTree<Figure>();
        for (Figure f : getChildren()) {
            quadTree.add(f, f.getDrawingArea());
        }
        return that;
    }

    protected EventHandler createEventHandler() {
        return new QuadTreeEventHandler();
    }

    /**
     * Handles all figure events fired by Figures contained in the Drawing.
     */
    protected class QuadTreeEventHandler extends AbstractCompositeFigure.EventHandler {

        @Override
        public void figureChanged(FigureEvent e) {
            quadTree.remove(e.getFigure());
            quadTree.add(e.getFigure(), e.getFigure().getDrawingArea());
            needsSorting = true;
            invalidate();
            fireAreaInvalidated(e.getInvalidatedArea());
        }
    }

    @Override
    protected void drawFill(Graphics2D g) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void drawStroke(Graphics2D g) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }
}
