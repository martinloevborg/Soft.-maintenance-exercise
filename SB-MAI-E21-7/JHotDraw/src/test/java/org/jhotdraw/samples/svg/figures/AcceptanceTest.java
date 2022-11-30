/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

/**
 *
 * @author Bruger
 */
public class AcceptanceTest extends ScenarioTest<Given, When, Then>{

    @Test
    public void BDDScenario(){
        given().givenText();
        when().writeText();
        then().resultText();
    }
}
