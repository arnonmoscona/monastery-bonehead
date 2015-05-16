package com.moscona.monastery.bonehead.impl;

import com.moscona.monastery.cando.NodeDiscovery;
import com.moscona.monastery.cando.NodeInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Created by Arnon Moscona on 5/15/2015.
 */
public class BoneHeadedNodeDiscovery implements NodeDiscovery<Integer> {
    private final BoneHeadedInMemoryCluster cluster;
    ArrayList<Consumer<NodeInformation<Integer>>> listeners;
    ArrayList<NodeInformation<Integer>> discoveredNodes;
    public String id = "";

    public BoneHeadedNodeDiscovery() {
        cluster = BoneHeadedInMemoryCluster.instance();
        listeners = new ArrayList<>();
        discoveredNodes = new ArrayList<>();
        cluster.onNewNode(info -> {
//            System.out.println("discovered node "+info);
            discoveredNodes.add(info);
            listeners.forEach(action -> action.accept(info));
        });
    }

    @Override
    public NodeDiscovery addNodeDiscoveryListener(Consumer<NodeInformation<Integer>> action) {
//        System.out.println("adding node discovery listener...");
        listeners.add(action);
        return this;
    }

    @Override
    public CompletableFuture<List<NodeInformation<Integer>>> listKnowNodes() {
        ArrayList<NodeInformation<Integer>> retval = new ArrayList<>();
        retval.addAll(discoveredNodes);
        CompletableFuture<List<NodeInformation<Integer>>> future = new CompletableFuture<>();
        future.complete(retval);
        return future;
    }
}
