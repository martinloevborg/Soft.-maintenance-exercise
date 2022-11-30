/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import java.awt.Graphics2D;

/**
 *
 * @author Bruger
 */
public class When extends Stage<When>{
    @ExpectedScenarioState
    @ProvidedScenarioState
    SVGTextFigure sVGTextFigure;
    
    @BeforeStage
    public void before(){
        sVGTextFigure = new SVGTextFigure();
    }
    When writeText(){
        String newText = "";
        sVGTextFigure.setText(newText);
        return this;
    }
}
