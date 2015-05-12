package com.moscona.monastery.bonehead.impl;

import com.moscona.monastery.api.core.Capability;
import com.moscona.monastery.api.core.Node;
import com.moscona.monastery.api.core.NodeState;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A basic Node implementation that totally fakes ou a cluster
 */
public class BoneHeadedNode implements Node<Integer> {
    private BoneHeadedInMemoryCluster cluster;
    private Integer id;
    private final GreetingCapabilityImpl greetingCapability;
    private NodeState state = NodeState.DISCONNECTED;

    public BoneHeadedNode() {
        cluster = BoneHeadedInMemoryCluster.instance();
        greetingCapability = new GreetingCapabilityImpl();
    }

    @Override
    public Optional<Integer> getId() {
        return Optional.of(id);
    }

    @Override
    public synchronized NodeState getState() {
        return state;
    }

    @Override
    public synchronized CompletableFuture<Node> announce() {
        CompletableFuture<Node> future = new CompletableFuture<>();
        switch (state) {
            case JOINED:
                System.out.printf("Node %d already joined. Nothing to do", id);
                future.complete(this);
                break;
            case ANNOUNCED:
                System.out.println("Node in ANNOUNCED state. Wait for result...");
                future.complete(this);
                break;
            case DISCONNECTED:
                state = NodeState.ANNOUNCED; // should be thread safe!
                new Thread(() -> {
                    try {
                        System.out.println("Joining...");
                        Thread.sleep(1000);
                        id = cluster.getNextId();
                        state = NodeState.JOINED;
                        System.out.printf("Node %d joined.", id);
                        future.complete(this);
                    } catch (InterruptedException e) {
                        future.completeExceptionally(e);
                    }
                }).start();
        }
        return future;
    }

    @Override
    public <T extends Capability> CompletableFuture<Optional<? extends T>> getCapability(Class<T> capabilityClass) {
        CompletableFuture<Optional<? extends T>> future = new CompletableFuture<>();

        if (capabilityClass.isAssignableFrom(greetingCapability.getClass())) {
            future.complete(Optional.of(capabilityClass.cast(greetingCapability)));
        }
        else {
            future.complete(Optional.empty());
        }

        return future;
    }
}
