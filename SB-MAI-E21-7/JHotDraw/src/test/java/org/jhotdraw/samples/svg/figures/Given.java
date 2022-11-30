/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
/**
 *
 * @author Bruger
 */
public class Given extends Stage<Given>{
    @ProvidedScenarioState
    SVGTextFigure sVGTextFigure;
    
    @BeforeStage
    public void before(){
        sVGTextFigure = new SVGTextFigure();
    }
    
    Given givenText(){
        sVGTextFigure.getText();
        return this;
    }
}
