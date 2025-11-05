package org.example.util;

public class Metrics {
    private long dfsVisits = 0;
    private long edgeTraversals = 0;
    private long queuePops = 0;
    private long queuePushes = 0;
    private long relaxations = 0;
    private long startTime;
    private long endTime;

    public void startTiming() {
        startTime = System.nanoTime();
    }

    public void stopTiming() {
        endTime = System.nanoTime();
    }

    public long getElapsedTimeNanos() {
        return endTime - startTime;
    }

    public double getElapsedTimeMs() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public void incrementDfsVisits() {
        dfsVisits++;
    }

    public void incrementEdgeTraversals() {
        edgeTraversals++;
    }

    public void incrementQueuePops() {
        queuePops++;
    }

    public void incrementQueuePushes() {
        queuePushes++;
    }

    public void incrementRelaxations() {
        relaxations++;
    }

    public long getDfsVisits() {
        return dfsVisits;
    }

    public long getEdgeTraversals() {
        return edgeTraversals;
    }

    public long getQueuePops() {
        return queuePops;
    }

    public long getQueuePushes() {
        return queuePushes;
    }

    public long getRelaxations() {
        return relaxations;
    }

    public void reset() {
        dfsVisits = 0;
        edgeTraversals = 0;
        queuePops = 0;
        queuePushes = 0;
        relaxations = 0;
        startTime = 0;
        endTime = 0;
    }

    @Override
    public String toString() {
        return String.format(
            "Metrics{dfsVisits=%d, edgeTraversals=%d, queuePops=%d, queuePushes=%d, relaxations=%d, time=%.3f ms}",
            dfsVisits, edgeTraversals, queuePops, queuePushes, relaxations, getElapsedTimeMs()
        );
    }
}

