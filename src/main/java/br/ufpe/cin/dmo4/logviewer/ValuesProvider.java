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
