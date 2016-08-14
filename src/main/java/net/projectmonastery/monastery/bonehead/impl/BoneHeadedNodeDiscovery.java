package net.projectmonastery.monastery.bonehead.impl;

import net.projectmonastery.monastery.capabilitiy.NodeDiscovery;
import net.projectmonastery.monastery.capabilitiy.NodeInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Created by Arnon Moscona on 5/15/2015.
 */
public class BoneHeadedNodeDiscovery implements NodeDiscovery {
    private final BoneHeadedInMemoryCluster cluster;
    ArrayList<Consumer<NodeInformation>> listeners;
    ArrayList<NodeInformation> discoveredNodes;
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
    public NodeDiscovery addNodeDiscoveryListener(Consumer<NodeInformation> action) {
//        System.out.println("adding node discovery listener...");
        listeners.add(action);
        return this;
    }

    @Override
    public CompletableFuture<List<NodeInformation>> listKnowNodes() {
        ArrayList<NodeInformation> retval = new ArrayList<>();
        retval.addAll(discoveredNodes);
        CompletableFuture<List<NodeInformation>> future = new CompletableFuture<>();
        future.complete(retval);
        return future;
    }
}
