/*
 * @(#)AttributeAction.java  3.1  2009-04-10
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
package org.jhotdraw.draw.action;

import javax.swing.undo.*;
import org.jhotdraw.app.action.Actions;
import javax.swing.*;
import java.util.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * AttributeAction.
 *
 * @author Werner Randelshofer
 * @version 3.1 2009-04-10 Factored method applyAttributes out of method
 * actionPerformed to make extensions of this class easier.
 * <br>3.0 207-05-12 Method setAttribute in interface Figure does not
 * handle undo/redo anymore, we must do this by ourselves.
 * <br>2.0 2006-06-07 Reworked.
 * <br>1.1 2006-02-27 Support for compatible text action added.
 * <br>1.0 25. November 2003  Created.
 */
public class AttributeAction extends AbstractSelectedAction {
    protected Map<AttributeKey, Object> attributes;

    /** Creates a new instance. */
    /** Creates a new instance. */
    public AttributeAction(DrawingEditor editor, AttributeKey key, Object value) {
        this(editor, key, value, null, null);
    }

    /** Creates a new instance. */
    public AttributeAction(DrawingEditor editor, AttributeKey key, Object value, Icon icon) {
        this(editor, key, value, null, icon);
    }

    /** Creates a new instance. */
    public AttributeAction(DrawingEditor editor, AttributeKey key, Object value, String name) {
        this(editor, key, value, name, null);
    }

    public AttributeAction(DrawingEditor editor, AttributeKey key, Object value, String name, Icon icon) {
        this(editor, key, value, name, icon, null);
    }

    public AttributeAction(DrawingEditor editor, AttributeKey key, Object value, String name, Icon icon, Action compatibleTextAction) {
        super(editor);
        this.attributes = new HashMap<AttributeKey, Object>();
        attributes.put(key, value);

        putValue(AbstractAction.NAME, name);
        putValue(AbstractAction.SMALL_ICON, icon);
        putValue(Actions.UNDO_PRESENTATION_NAME_KEY, key.getPresentationName());
        setEnabled(true);
    }

    public AttributeAction(DrawingEditor editor, Map<AttributeKey, Object> attributes, String name, Icon icon) {
        super(editor);
        this.attributes = (attributes == null) ? new HashMap<AttributeKey, Object>() : attributes;

        putValue(AbstractAction.NAME, name);
        putValue(AbstractAction.SMALL_ICON, icon);
        updateEnabledState();
    }

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        applyAttributesTo(attributes, getView().getSelectedFigures());
    }

    /**
     * Applies the specified attributes to the currently selected figures
     * of the drawing.
     *
     * @param a The attributes.
     * @param figures The figures to which the attributes are applied.
     */
    @SuppressWarnings("unchecked")
    public void applyAttributesTo(final Map<AttributeKey, Object> a, Set<Figure> figures) {
        for (Map.Entry<AttributeKey, Object> entry : a.entrySet()) {
            getEditor().setDefaultAttribute(entry.getKey(), entry.getValue());
        }

        final ArrayList<Figure> selectedFigures = new ArrayList<Figure>(figures);
        final ArrayList<Object> restoreData = new ArrayList<Object>(selectedFigures.size());
        for (Figure figure : selectedFigures) {
            restoreData.add(figure.getAttributesRestoreData());
            figure.willChange();
            for (Map.Entry<AttributeKey, Object> entry : a.entrySet()) {
                entry.getKey().basicSet(figure, entry.getValue());
            }
            figure.changed();
        }
        UndoableEdit edit = new AbstractUndoableEdit() {

            @Override
            public String getPresentationName() {
                String name = (String) getValue(Actions.UNDO_PRESENTATION_NAME_KEY);
                if (name == null) {
                    name = (String) getValue(AbstractAction.NAME);
                }
                if (name == null) {
                    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                    name = labels.getString("attribute.text");
                }
                return name;
            }

            @Override
            public void undo() {
                super.undo();
                Iterator<Object> iRestore = restoreData.iterator();
                for (Figure figure : selectedFigures) {
                    figure.willChange();
                    figure.restoreAttributesTo(iRestore.next());
                    figure.changed();
                }
            }

            @Override
            public void redo() {
                super.redo();
                for (Figure figure : selectedFigures) {
                    restoreData.add(figure.getAttributesRestoreData());
                    figure.willChange();
                    for (Map.Entry<AttributeKey, Object> entry : a.entrySet()) {
                        entry.getKey().basicSet(figure, entry.getValue());
                    }
                    figure.changed();
                }
            }
        };
        getDrawing().fireUndoableEditHappened(edit);
    }

    protected void updateEnabledState() {
        setEnabled(getEditor().isEnabled());
    }
}
