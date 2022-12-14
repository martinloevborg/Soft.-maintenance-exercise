/*
 * @(#)AbstractView.java  1.3  2009-02-08
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
package org.jhotdraw.app;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.concurrent.*;
import java.util.prefs.*;

/**
 * AbstractView.
 * 
 * 
 * @author Werner Randelshofer
 * @version 1.3 2009-02-08 Made preferences variable protected instead
 * of private.
 * <br>1.2.1 2008-09-09 Explicitly dispose of the executor service.
 * <br>1.2 2007-12-25 Updated to changes in View interface. 
 * <br>1.1.1 2006-04-11 Fixed view file preferences.
 * <br>1.1 2006-02-16 Support for preferences added.
 * <br>1.0 January 3, 2006 Created.
 */
public abstract class AbstractView extends JPanel implements View {

    private Application application;
    /**
     * The file chooser used for saving the view.
     * Has a null value, if the file chooser has not been used yet.
     */
    protected JFileChooser saveChooser;
    /**
     * The file chooser used for opening the view.
     * Has a null value, if the file chooser has not been used yet.
     */
    protected JFileChooser openChooser;
    /**
     * The view file. 
     * Has a null value, if the view has not been loaded from a file
     * or has not been saved yet.
     */
    protected File file;
    /**
     * The executor used to perform background tasks for the View in a
     * controlled manner. This executor ensures that all background tasks
     * are executed sequentually.
     */
    protected ExecutorService executor;
    /**
     * Hash map for storing view actions by their ID.
     */
    private HashMap<String,Action> actions;
    /**
     * This is set to true, if the view has unsaved changes.
     */
    private boolean hasUnsavedChanges;
    /**
     * The preferences of the view.
     */
    protected Preferences preferences;
    /**
     * This id is used to make multiple open projects from the same view file
     * identifiable.
     */
    private int multipleOpenId = 1;
    /**
     * This is set to true, if the view is showing.
     */
    private boolean isShowing;
    /**
     * The title of the view.
     */
    private String title;

    /**
     * Creates a new instance.
     */
    public AbstractView() {
        preferences = Preferences.userNodeForPackage(getClass());
    }

    /** Initializes the view.
     * This method does nothing, subclasses don't neet to call super. */
    public void init() {
    }

    /** Starts the view.
     * This method does nothing, subclasses don't neet to call super. */
    public void start() {
    }

    /** Activates the view.
     * This method does nothing, subclasses don't neet to call super. */
    public void activate() {
    }

    /** Deactivates the view.
     * This method does nothing, subclasses don't neet to call super. */
    public void deactivate() {
    }

    /** Stops the view.
     * This method does nothing, subclasses don't neet to call super. */
    public void stop() {
    }

    /**
     * Gets rid of all the resources of the view.
     * No other methods should be invoked on the view afterwards.
     */
    public void dispose() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public void setApplication(Application newValue) {
        Application oldValue = application;
        application = newValue;
        firePropertyChange("application", oldValue, newValue);
    }

    public Application getApplication() {
        return application;
    }

    public JComponent getComponent() {
        return this;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File newValue) {
        File oldValue = file;
        file = newValue;
        if (preferences != null && newValue != null) {
            preferences.put("projectFile", newValue.getPath());
        }
        firePropertyChange(FILE_PROPERTY, oldValue, newValue);
    }

    /**
     * Gets the open file chooser for the view.
     */
    public JFileChooser getOpenChooser() {
        if (openChooser == null) {
            openChooser = createOpenChooser();
        }
        return openChooser;
    }

    protected JFileChooser createOpenChooser() {
        JFileChooser c = new JFileChooser();
        if (preferences != null) {
            c.setSelectedFile(new File(preferences.get("projectFile", System.getProperty("user.home"))));
        }
        return c;
    }

    /**
     * Gets the save file chooser for the view.
     */
    public JFileChooser getSaveChooser() {
        if (saveChooser == null) {
            saveChooser = createSaveChooser();
        }
        return saveChooser;
    }

    public boolean canSaveTo(File file) {
        return true;
    }

    protected JFileChooser createSaveChooser() {
        JFileChooser c = new JFileChooser();
        if (preferences != null) {
            c.setCurrentDirectory(new File(preferences.get("projectFile", System.getProperty("user.home"))));
        }
        return c;
    }

    /**
     * Returns true, if the view has unsaved changes.
     * This is a bound property.
     */
    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    protected void setHasUnsavedChanges(boolean newValue) {
        boolean oldValue = hasUnsavedChanges;
        hasUnsavedChanges = newValue;
        firePropertyChange(HAS_UNSAVED_CHANGES_PROPERTY, oldValue, newValue);
    }

    /**
     * Returns the action with the specified id.
     */
    public Action getAction(String id) {
        return (actions == null) ? null : (Action) actions.get(id);
    }

    /**
     * Puts an action with the specified id.
     */
    public void putAction(String id, Action action) {
        if (actions == null) {
            actions = new HashMap<String,Action>();
        }
        if (action == null) {
            actions.remove(id);
        } else {
            actions.put(id, action);
        }
    }

    /**
     * Executes the specified runnable on the worker thread of the view.
     * Execution is perfomred sequentially in the same sequence as the
     * runnables have been passed to this method.
     */
    public void execute(Runnable worker) {
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }
        executor.execute(worker);
    }

    public void setMultipleOpenId(int newValue) {
        int oldValue = multipleOpenId;
        multipleOpenId = newValue;
        firePropertyChange(MULTIPLE_OPEN_ID_PROPERTY, oldValue, newValue);
    }

    public int getMultipleOpenId() {
        return multipleOpenId;
    }

    public void setShowing(boolean newValue) {
        boolean oldValue = isShowing;
        isShowing = newValue;
        firePropertyChange(SHOWING_PROPERTY, oldValue, newValue);
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void markChangesAsSaved() {
        setHasUnsavedChanges(false);
    }

    public void setTitle(String newValue) {
        String oldValue = title;
        title = newValue;
        firePropertyChange(TITLE_PROPERTY, oldValue, newValue);
    }

    public String getTitle() {
        return title;
    }
}
