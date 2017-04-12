package org.prevayler;


import org.prevayler.implementation.PrevaylerImpl;

/**
 * Created by thma on 13.06.2016.
 */
public class PrevaylerFactory<P> {

    private P _prevalentSystem;

    public PrevaylerFactory(P newPrevalentSystem) {
        _prevalentSystem = newPrevalentSystem;
    }

    public static <P> Prevayler<P> createPrevayler(P newPrevalentSystem, String prevalenceBase) throws Exception {
        return new PrevaylerImpl<P>(newPrevalentSystem);
    }

    public static <P> Prevayler<P> createPrevayler(P newPrevalentSystem) throws Exception {
        return createPrevayler(newPrevalentSystem, "PrevalenceBase");
    }

    public Prevayler<P> create() throws Exception {
        return new PrevaylerImpl<P>(_prevalentSystem);
    }
}
