/*
 * @(#)EditorColorChooserAction.java  3.0  2009-04-10
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

import java.util.*;
import java.awt.*;
import javax.swing.*;
import org.jhotdraw.draw.*;

/**
 * EditorColorChooserAction.
 * <p>
 * The behavior for choosing the initial color of the JColorChooser matches with
 * {@link EditorColorIcon }.
 *
 * @author Werner Randelshofer
 * @version 3.0 2009-04-10 Extend from AttributeAction instead of from 
 * AbstractSelectedAction to ensure that this action fires UndoableEdit events.
 * <br>
 * <br>2.0.1 2009-03-29 Ensure that instance variable fixedAttributes is
 * not null,
 * <br>2.0 2006-06-07 Reworked.
 * <br>1.0 2004-03-02  Created.
 */
public class EditorColorChooserAction extends AttributeAction {
    protected AttributeKey<Color> key;

    protected static JColorChooser colorChooser;

    /** Creates a new instance. */
    public EditorColorChooserAction(DrawingEditor editor, AttributeKey<Color> key) {
        this(editor, key, null, null);
    }

    /** Creates a new instance. */
    public EditorColorChooserAction(DrawingEditor editor, AttributeKey<Color> key, Icon icon) {
        this(editor, key, null, icon);
    }

    /** Creates a new instance. */
    public EditorColorChooserAction(DrawingEditor editor, AttributeKey<Color> key, String name) {
        this(editor, key, name, null);
    }

    public EditorColorChooserAction(DrawingEditor editor, final AttributeKey<Color> key, String name, Icon icon) {
        this(editor, key, name, icon, new HashMap<AttributeKey, Object>());
    }

    public EditorColorChooserAction(DrawingEditor editor, final AttributeKey<Color> key, String name, Icon icon,
            Map<AttributeKey, Object> fixedAttributes) {
        super(editor, fixedAttributes, name, icon);
        this.key = key;
        putValue(AbstractAction.NAME, name);
        putValue(AbstractAction.SMALL_ICON, icon);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (colorChooser == null) {
            colorChooser = new JColorChooser();
        }
        Color initialColor = getInitialColor();
        // FIXME - Reuse colorChooser object instead of calling static method here.
        Color chosenColor = colorChooser.showDialog((Component) e.getSource(), labels.getString("attribute.color.text"), initialColor);
        if (chosenColor != null) {
            HashMap<AttributeKey, Object> attr = new HashMap<AttributeKey, Object>(attributes);
            attr.put(key, chosenColor);
            applyAttributesTo(attr, getView().getSelectedFigures());
        }
    }

    public void selectionChanged(FigureSelectionEvent evt) {
        //setEnabled(getView().getSelectionCount() > 0);
    }

    protected Color getInitialColor() {
        Color initialColor = (Color) getEditor().getDefaultAttribute(key);
        if (initialColor == null) {
            initialColor = Color.red;
        }
        return initialColor;
    }
}
