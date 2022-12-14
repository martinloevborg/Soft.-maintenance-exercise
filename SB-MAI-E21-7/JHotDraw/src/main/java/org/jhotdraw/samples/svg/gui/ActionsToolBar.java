/*
 * @(#)ActionsToolBar.java  2.0  2008-04-08
 *
 * Copyright (c) 2007-2008 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.app.action.*;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.action.*;
import org.jhotdraw.gui.JPopupButton;
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI;
import org.jhotdraw.samples.svg.figures.SVGGroupFigure;
import org.jhotdraw.undo.UndoRedoManager;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.prefs.Preferences;

/**
 * ActionsToolBar.
 *
 * @author Werner Randelshofer
 * @version 2.0 2008-04-08 Reworked. 
 * <br>1.1 2008-03-26 Don't draw button borders. 
 * <br>1.0 May 1, 2007 Created.
 */
public class ActionsToolBar extends AbstractToolBar {

    private ToggleGridAction toggleGridAction;
    private UndoRedoManager undoManager;

    /** Creates new instance. */
    public ActionsToolBar() {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString(getID() + ".toolbar"));
    }

    @Override
    public void setEditor(DrawingEditor newValue) {
        if (this.editor != null && undoManager != null) {
            this.removePropertyChangeListener(getEventHandler());
        }
        this.editor = newValue;
        if (editor != null && undoManager != null) {
            init();
            setDisclosureState(prefs.getInt(getID() + ".disclosureState", 1));
            this.addPropertyChangeListener(getEventHandler());
        }
    }

    public void setUndoManager(UndoRedoManager newValue) {
        if (this.editor != null && newValue != null) {
            this.removePropertyChangeListener(getEventHandler());
        }
        this.undoManager = newValue;
        if (editor != null && newValue != null) {
            init();
            setDisclosureState(prefs.getInt(getID() + ".disclosureState", 1));
            this.addPropertyChangeListener(getEventHandler());
        }
    }

    @Override
    protected JComponent createDisclosedComponent(int state) {
        JPanel p = null;

        switch (state) {
            case 1: {
                p = new JPanel();
                p.setOpaque(false);
                p.setBorder(new EmptyBorder(5, 5, 5, 8));

                Preferences prefs = Preferences.userNodeForPackage(getClass());

                ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

                GridBagLayout layout = new GridBagLayout();
                p.setLayout(layout);

                GridBagConstraints gbc;
                AbstractButton btn;

                btn = new JButton(undoManager.getUndoAction());
                btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
                btn.setText(null);
                labels.configureToolBarButton(btn, "edit.undo");
                btn.putClientProperty("hideActionText", Boolean.TRUE);
                gbc = new GridBagConstraints();
                gbc.gridy = 0;
                gbc.gridx = 0;
                p.add(btn, gbc);

                btn = new JButton(undoManager.getRedoAction());
                btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
                btn.setText(null);
                labels.configureToolBarButton(btn, "edit.redo");
                btn.putClientProperty("hideActionText", Boolean.TRUE);
                gbc = new GridBagConstraints();
                gbc.gridy = 0;
                gbc.insets = new Insets(0, 3, 0, 0);
                p.add(btn, gbc);


                btn = ButtonFactory.createPickAttributesButton(editor);
                btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
                labels.configureToolBarButton(btn, "attributesPick");
                gbc = new GridBagConstraints();
                gbc.gridy = 1;
                gbc.insets = new Insets(3, 0, 0, 0);
                p.add(btn, gbc);

                btn = ButtonFactory.createApplyAttributesButton(editor);
                btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
                labels.configureToolBarButton(btn, "attributesApply");
                gbc = new GridBagConstraints();
                gbc.gridy = 1;
                gbc.insets = new Insets(3, 3, 0, 0);
                p.add(btn, gbc);

                JPopupButton pb = new JPopupButton();
                pb.setUI((PaletteButtonUI) PaletteButtonUI.createUI(pb));
                pb.setItemFont(UIManager.getFont("MenuItem.font"));
                labels.configureToolBarButton(pb, "actions");
                pb.add(new DuplicateAction());
                pb.addSeparator();
                pb.add(GroupAction.create(editor, new SVGGroupFigure()));
                pb.add(UngroupAction.create(editor, new SVGGroupFigure()));
                pb.addSeparator();
                pb.add(new CutAction());
                pb.add(new CopyAction());
                pb.add(new PasteAction());
                pb.add(new SelectAllAction());
                pb.add(new SelectSameAction(editor));
                pb.add(new ClearSelectionAction());

                gbc = new GridBagConstraints();
                gbc.gridy = 2;
                gbc.insets = new Insets(3, 0, 0, 0);
                p.add(pb, gbc);
                break;
            }
        }
        return p;
    }

    public ToggleGridAction getToggleGridAction() {
        return toggleGridAction;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected String getID() {
        return "actions";
    }

    protected int getDefaultDisclosureState() {
        return 1;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
