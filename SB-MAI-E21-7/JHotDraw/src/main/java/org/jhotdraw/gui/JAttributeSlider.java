/*
 * @(#)JAttributeSlider.java  1.1  2008-05-18
 *
 * Copyright (c) 2007 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.gui;

import javax.swing.*;

/**
 * A JSlider that can be used to edit a double attribute of a Figure.
 *
 * @author Werner Randelshofer
 * @version 1.1 2008-05-18 Added property "enabledWithoutSelection".
 * <br>1.0 April 30, 2007 Created.
 */
public class JAttributeSlider extends JSlider implements AttributeEditor<Double> {
    private boolean isMultipleValues;
    private Double attributeValue;
    private double scaleFactor = 1d;

    /** Creates new instance. */
    public JAttributeSlider() {
        this(JSlider.VERTICAL, 0, 100, 50);
    }

    public JAttributeSlider(int orientation, int min, int max, int value) {
        super(orientation, min, max, value);
    }

    public JComponent getComponent() {
        return this;
    }

    public void setAttributeValue(Double newValue) {
        attributeValue = newValue;
        setValue((int) (newValue * scaleFactor));
    }

    public Double getAttributeValue() {
        return attributeValue;
    }

    public void setScaleFactor(double newValue) {
        scaleFactor = newValue;
    }
    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setMultipleValues(boolean newValue) {
        boolean oldValue = isMultipleValues;
        isMultipleValues = newValue;
        firePropertyChange(MULTIPLE_VALUES_PROPERTY, oldValue, newValue);
    }

    public boolean isMultipleValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void fireStateChanged() {
        super.fireStateChanged();
        Double oldValue = attributeValue;
        attributeValue =  getValue() / scaleFactor;
        firePropertyChange(ATTRIBUTE_VALUE_PROPERTY, oldValue, attributeValue);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
