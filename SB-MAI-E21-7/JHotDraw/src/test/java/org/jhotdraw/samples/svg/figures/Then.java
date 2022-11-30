/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import java.awt.geom.Point2D.Double;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 *
 * @author Bruger
 */
public class Then extends Stage<Then>{
    @ExpectedScenarioState
    SVGTextFigure sVGTextFigure;
    
    Then resultText(){
        sVGTextFigure.isEmpty();
        return this;
    }
}
