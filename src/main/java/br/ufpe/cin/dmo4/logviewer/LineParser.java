/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpe.cin.dmo4.logviewer;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.util.Arrays;

/**
 *
 * @author danilo
 */
@FunctionalInterface
public interface LineParser {

    public double parse(String line);

    public static LineParser createParser(String expression) {
        final Binding binding = new Binding();
        final GroovyShell shell = new GroovyShell(binding);
        
        return (String line) -> {
            String[] strArr = line.split(" ");
            
            Double[] col = Arrays
                    .stream(strArr)
                    .map((i) -> Double.parseDouble(i))
                    .toArray(Double[]::new);
            
            binding.setVariable("col", col);
            
            return (double) shell.evaluate(expression);
        };

    }    
}
