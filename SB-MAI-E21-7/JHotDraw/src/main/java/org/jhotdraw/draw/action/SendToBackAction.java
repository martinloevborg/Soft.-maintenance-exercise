/*
 * @(#)SendToBackAction.java  2.0  2008-05-30
 *
 * Copyright (c) 2003-2008 by the original authors of JHotDraw
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

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.util.*;
import javax.swing.*;
import java.util.*;
import javax.swing.undo.*;
import org.jhotdraw.app.JHotDrawFeatures;
import org.jhotdraw.draw.*;

/**
 * SendToBackAction.
 *
 * @author  Werner Randelshofer
 * @version 2.0 2008-05-30 Renamed from MoveToBackAction to SendToBackAction
 * for consistency with the API of Drawing.
 * <br>1.0 24. November 2003  Created.
 */
public class SendToBackAction extends AbstractSelectedAction {
    
       public static String ID = "edit.sendToBack";
    /** Creates a new instance. */
    public SendToBackAction(DrawingEditor editor) {
        super(editor);
        labels.configureAction(this, ID);
    }

    @FeatureEntryPoint(JHotDrawFeatures.ARRANGE)
    public void actionPerformed(java.awt.event.ActionEvent e) {
        final DrawingView view = getView();
        final LinkedList<Figure> figures = new LinkedList<Figure>(view.getSelectedFigures());
        sendToBack(view, figures);
        fireUndoableEditHappened(new AbstractUndoableEdit() {
            @Override
            public String getPresentationName() {
       return labels.getTextProperty(ID);
            }
            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                SendToBackAction.sendToBack(view, figures);
            }
            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                BringToFrontAction.bringToFront(view, figures);
            }
        }
        );
    }
    public static void sendToBack(DrawingView view, Collection figures) {
        Iterator i = figures.iterator();
        Drawing drawing = view.getDrawing();
        while (i.hasNext()) {
            Figure figure = (Figure) i.next();
            drawing.sendToBack(figure);
        }
    }
}
