package com.moscona.monastery.bonehead.impl;

import com.moscona.monastery.api.core.Node;
import com.moscona.monastery.cando.NodeInformation;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * A bone headed singleton cluster in memory.
 * Yes, I know Singleton is an anti-pattern, but this is BoneHeaded
 */
public class BoneHeadedInMemoryCluster {
    private static AtomicReference<BoneHeadedInMemoryCluster> instance = new AtomicReference<>(null);
    private AtomicInteger nextId = new AtomicInteger(1);
    private ArrayList<Consumer<NodeInformation<Integer>>> newNodeActions;
    private ArrayList<BoneHeadedNode> nodes;

    private BoneHeadedInMemoryCluster() {
        nodes = new ArrayList<>();
        newNodeActions = new ArrayList<>();
    }

    public static BoneHeadedInMemoryCluster instance() {
        instance.compareAndSet(null, new BoneHeadedInMemoryCluster());
        return instance.get();
    }

    public int getNextId() {
        return nextId.getAndIncrement();
    }

    public void add(BoneHeadedNode node) throws Exception {
        nodes.add(node);
        System.out.println("added node "+node.getId());
        BoneHeadedNodeInformation info = new BoneHeadedNodeInformation(node);
        newNodeActions.forEach(action->action.accept(info));
    }

    public void onNewNode(Consumer<NodeInformation<Integer>> action) {
        newNodeActions.add(action);
    }
}
