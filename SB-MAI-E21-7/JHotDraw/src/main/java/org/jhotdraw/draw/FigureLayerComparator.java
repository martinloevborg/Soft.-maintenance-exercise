/*
 * @(#)FigureLayerComparator.java  1.0  1. April 2004
 *
 * Copyright (c) 1996-2006 by the original authors of JHotDraw
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

import java.util.*;
/**
 * FigureLayerComparator.
 *
 * @author  Werner Randelshofer
 * @version 1.0 1. April 2004  Created.
 */
public class FigureLayerComparator implements Comparator<Figure> {
    public final static FigureLayerComparator INSTANCE = new FigureLayerComparator();
    
    /** Creates a new instance. */
    private FigureLayerComparator() {
    }
    
    public int compare(Figure f1, Figure f2) {
        return f1.getLayer() - f2.getLayer();
    }
    
}
