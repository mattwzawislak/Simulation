package org.obicere.simulation;

import java.util.logging.Logger;

/**
 * @author Obicere
 */
public abstract class Simulation {

    protected final Logger log = Logger.getLogger(getName());

    public abstract String getName();

    public abstract void main();

}
