package net.projectmonastery.monastery.bonehead.impl;

import net.projectmonastery.monastery.api.core.Capability;
import net.projectmonastery.monastery.api.core.Node;
import net.projectmonastery.monastery.api.core.NodeProvider;
import net.projectmonastery.monastery.api.core.NodeProviderBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Arnon Moscona on 5/13/2015.
 */
public class BoneHeadedNodeBuilder implements NodeProviderBuilder {
    ArrayList<Capability> capabilities;

    public BoneHeadedNodeBuilder() {
        capabilities = new ArrayList<>();
        capabilities.add(new BoneHeadedAnnouncementCapability()); // critical for our implementation and comes by default
        capabilities.add(new BoneHeadedNodeDiscovery());
    }

    @Override
    public NodeProviderBuilder add(Capability... capabilities) {
        this.capabilities.addAll(Arrays.asList(capabilities));
        return this;
    }

    @Override
    public NodeProvider build() throws Exception {
        BoneHeadedNode node = new BoneHeadedNode(capabilities);
        capabilities.forEach(capability -> capability.bind(node));
        capabilities.forEach(Capability::onAllCapabilitiesBound);

        return new BoneHeadNodeProvider(node);
    }

    private static class BoneHeadNodeProvider implements NodeProvider {
        private final Node node;

        BoneHeadNodeProvider(Node node) {
            this.node = node;
        }
        /**
         * Connects to the cluster, and when connected provides a Node.
         *
         * @return a CompletionStage that whn complete provides a connected Node
         */
        @Override
        public CompletableFuture<Node> connect() {
            CompletableFuture<Node> promise = new CompletableFuture<>();
            promise.complete(node);
            return promise;
        }
    }
}
