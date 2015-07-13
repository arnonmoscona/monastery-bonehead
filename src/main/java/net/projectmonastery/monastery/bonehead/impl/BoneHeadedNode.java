package net.projectmonastery.monastery.bonehead.impl;

import net.projectmonastery.monastery.api.core.Capability;
import net.projectmonastery.monastery.api.core.Node;
import net.projectmonastery.monastery.cando.NodeAnnouncement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A basic Node implementation that totally fakes ou a cluster
 */
public class BoneHeadedNode implements Node<Integer> {
    private BoneHeadedInMemoryCluster cluster;
    private List<Capability> path;
    private Integer cachedId;

    protected BoneHeadedNode() {
        cluster = BoneHeadedInMemoryCluster.instance();
    }

    protected BoneHeadedNode(List<Capability> capabilities) {
        this();
        path = capabilities;
    }

    @Override
    public Optional<Integer> getId() {
        if (cachedId != null) {
            return Optional.of(cachedId);
        }
        try {
            NodeAnnouncement<?> nodeAnnouncement = getCapability(NodeAnnouncement.class).get();
            Optional<?> optional = nodeAnnouncement.getId();
            if (optional.isPresent() && Integer.class.isAssignableFrom(optional.get().getClass())) {
                cachedId = (Integer) optional.get();
            }
        } catch (Exception e) {
            System.err.println("Exception: "+e);
            e.printStackTrace(System.err);
        }
        return Optional.ofNullable(cachedId);
    }

    @Override
    public <T extends Capability> CompletableFuture<T> getCapability(Class<T> capabilityClass) {
        CompletableFuture<T> future = new CompletableFuture<>();
        if (path==null) {
            future.completeExceptionally(new Exception("No capability list exists"));
            return future;
        }

        for (Capability capability: path) {
            if (capabilityClass.isAssignableFrom(capability.getClass())) {
                future.complete(capabilityClass.cast(capability));
                return future;
            }
        }

        future.completeExceptionally(new Exception("cannot find a capability matching "+capabilityClass.getName()));
        return future;
    }

    public BoneHeadedInMemoryCluster getCluster() {
        return cluster;
    }

    @Override
    public List<Capability> getCapabilities() {
        return new ArrayList<>(path);
    }

    @Override
    public String getConnectionString() {
        return "null";
    }
}
