package com.moscona.monastery.bonehead.impl;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A bone headed singleton cluster in memory.
 * Yes, I know Singleton is an anti-pattern, but this is BoneHeaded
 */
public class BoneHeadedInMemoryCluster {
    private static AtomicReference<BoneHeadedInMemoryCluster> instance = new AtomicReference<>(null);
    private AtomicInteger nextId = new AtomicInteger(1);

    private BoneHeadedInMemoryCluster() {

    }

    public static BoneHeadedInMemoryCluster instance() {
        instance.compareAndSet(null, new BoneHeadedInMemoryCluster());
        return instance.get();
    }

    public int getNextId() {
        return nextId.getAndIncrement();
    }
}
