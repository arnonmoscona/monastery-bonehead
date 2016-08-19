package net.projectmonastery.monastery.bonehead.impl;

import net.projectmonastery.monastery.api.core.Node;
import net.projectmonastery.monastery.capability.NodeAnnouncement;
import net.projectmonastery.monastery.capability.NodeState;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Created by Arnon Moscona on 5/13/2015.
 */
public class BoneHeadedAnnouncementCapability implements NodeAnnouncement {
    private BoneHeadedNode node;
    private NodeState state = NodeState.DISCONNECTED;
    private Integer id;
    private ArrayList<Consumer<Node>> joinListeners;

    public BoneHeadedAnnouncementCapability() {
        joinListeners = new ArrayList<>();
    }

    @Override
    public synchronized NodeState getState() {
        return state;
    }

    @Override
    public synchronized CompletableFuture<NodeAnnouncement> announce() {
        CompletableFuture<NodeAnnouncement> future = new CompletableFuture<>();
        switch (state) {
            case JOINED:
                System.out.printf("Node %d already joined. Nothing to do", node.getId().get());
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
                        id = node.getCluster().getNextId();
                        node.getCluster().add(node);
                        state = NodeState.JOINED;
                        System.out.printf("Node %d joined.\n", id);
                        joinListeners.forEach(listener -> listener.accept(node));
                        future.complete(this);
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                }).start();
        }
        return future;
    }

    @Override
    public Optional<Integer> getId() {
        return Optional.ofNullable(id);
    }

    @Override
    public Optional<Node> getBoundNode() {
        return Optional.of(node);
    }

    @Override
    public NodeAnnouncement addJoinListener(Consumer<Node> action) {
        System.out.println("adding join listener");
        joinListeners.add(action);
        return this;
    }

    @Override
    public void bind(Node context) {
        System.out.println("announcementCapability: binding");
        if (context.getClass().isAssignableFrom(BoneHeadedNode.class)) {
            node = (BoneHeadedNode)context;
        }
        else {
            throw new IllegalArgumentException("This implementation of the NodeAnnouncement capability required a BoneHeadedNode");
        }
    }

    @Override
    public void onAllCapabilitiesBound() throws IllegalStateException {
        System.out.println("announcementCapability: all nodes bound");
    }
}
