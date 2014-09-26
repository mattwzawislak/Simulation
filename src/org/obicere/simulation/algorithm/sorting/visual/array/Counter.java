package org.obicere.simulation.algorithm.sorting.visual.array;

/**
 * @author Obicere
 */
public class Counter {

    private int comparisons = 0;
    private int accesses    = 0;

    public void compared() {
        comparisons++;
    }

    public void accessed() {
        accesses++;
    }

    public void multiAccessed(final int times){
        accesses += times;
    }

    public int getCompares(){
        return comparisons;
    }

    public int getAccesses(){
        return accesses;
    }

    public void reset() {
        comparisons = 0;
        accesses = 0;
    }

}
