/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpe.cin.dmo4.logviewer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danilo
 */
public class ValuesProvider {

    private final List<ProviderListener> listeners;

    public ValuesProvider() {
        listeners = new ArrayList();
    }

    public void registerListener(ProviderListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ProviderListener listener) {
        listeners.remove(listener);
    }

    public void newValue(double d) {
        listeners.forEach(
            (l) -> {
                l.valueProvided(d);
            }
        );
    }

}
