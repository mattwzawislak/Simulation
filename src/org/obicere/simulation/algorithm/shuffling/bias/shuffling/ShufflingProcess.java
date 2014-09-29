package org.obicere.simulation.algorithm.shuffling.bias.shuffling;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Obicere
 */
public class ShufflingProcess {

    private final ShufflingAlgorithm algorithm;
    private final ResultData         data;

    private final int length;
    private final int testCount;

    private final AtomicBoolean calculating = new AtomicBoolean();

    private final AtomicInteger testsDone = new AtomicInteger();

    public ShufflingProcess(final ShufflingAlgorithm algorithm, final int length, final int testCount) {
        this.algorithm = algorithm;
        this.data = new ResultData(length);
        this.length = length;
        this.testCount = testCount;
    }

    public ResultData getData() {
        return data;
    }

    public int getTestsDone() {
        return testsDone.get();
    }

    private void test() {
        final int[] newSet = new int[length];
        for (int i = 0; i < length; i++) {
            newSet[i] = i;
        }
        final int[] shuffled = algorithm.shuffle(newSet);
        data.register(shuffled);
    }

    public boolean isCalculating(){
        return calculating.get();
    }

    public void startTesting() {
        calculating.set(true);
        for (int i = 0; i < testCount; i++) {
            test();
            testsDone.incrementAndGet();
        }
        calculating.set(false);
    }

}
