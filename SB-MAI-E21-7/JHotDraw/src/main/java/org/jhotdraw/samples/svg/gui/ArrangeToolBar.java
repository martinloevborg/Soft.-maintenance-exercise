/*
 * @(#)ArrangeToolBar.java  1.1  2008-05-23
 *
 * Copyright (c) 2008 by the original authors of JHotDraw
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

import java.beans.*;
import java.util.prefs.*;
import javax.swing.border.*;
import org.jhotdraw.gui.*;
import org.jhotdraw.samples.svg.*;
import org.jhotdraw.undo.*;
import org.jhotdraw.util.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.jhotdraw.app.action.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.action.*;
import org.jhotdraw.gui.plaf.palette.*;
import org.jhotdraw.samples.svg.action.*;
import org.jhotdraw.samples.svg.figures.*;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.*;

/**
 * ArrangeToolBar.
 *
 * @author Werner Randelshofer
 * @version 1.1 2008-05-23 Hide the toolbar if nothing is selected, and no
 * creation tool is active. 
 * <br>1.0 2008-04-11  Created.
 */
public class ArrangeToolBar extends AbstractToolBar {

    private SelectionComponentDisplayer displayer;

    /** Creates new instance. */
    public ArrangeToolBar() {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString(getID() + ".toolbar"));
    }

    @Override
    public void setEditor(DrawingEditor newValue) {
        DrawingEditor oldValue = getEditor();
        if (displayer != null) {
            displayer.dispose();
            displayer = null;
        }
        super.setEditor(newValue);
        if (newValue != null) {
            displayer = new SelectionComponentDisplayer(editor, this);
            displayer.setVisibleIfCreationTool(false);
        }
    }

    @Override
    protected JComponent createDisclosedComponent(int state) {
        JPanel p = null;

        switch (state) {
            case 1:
                 {
                    p = new JPanel();
                    p.setOpaque(false);
                    p.setBorder(new EmptyBorder(5, 5, 5, 8));
                    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

                    GridBagLayout layout = new GridBagLayout();
                    p.setLayout(layout);

                    GridBagConstraints gbc;
                    AbstractButton btn;

                    btn = new JButton(new BringToFrontAction(editor));
                    btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
                    btn.setText(null);
                    labels.configureToolBarButton(btn, BringToFrontAction.ID);
                    btn.putClientProperty("hideActionText", Boolean.TRUE);
                    gbc = new GridBagConstraints();
                    gbc.gridy = 0;
                    gbc.anchor = GridBagConstraints.EAST;
                    p.add(btn, gbc);


                    btn = new JButton(new SendToBackAction(editor));
                    btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
                    btn.setText(null);
                    labels.configureToolBarButton(btn, SendToBackAction.ID);
                    btn.putClientProperty("hideActionText", Boolean.TRUE);
                    btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
                    gbc = new GridBagConstraints();
                    gbc.gridy = 1;
                    gbc.insets = new Insets(3, 0, 0, 0);
                    gbc.anchor = GridBagConstraints.NORTH;
                    gbc.weighty=1f;
                    p.add(btn, gbc);
                }
                break;
        }
        return p;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setOpaque(false);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected String getID() {
        return "arrange";
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
