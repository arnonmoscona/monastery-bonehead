package com.moscona.monastery.bonehead.impl;

import com.moscona.monastery.api.core.Capability;
import com.moscona.monastery.api.core.Node;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A basic Node implementation that totally fakes ou a cluster
 */
public class BoneHeadedNode implements Node<Integer> {
    private Integer id;
    private final GreetingCapabilityImpl greetingCapability;

    public BoneHeadedNode() {
        greetingCapability = new GreetingCapabilityImpl();
    }

    @Override
    public Optional<Integer> getId() {
        return Optional.of(id);
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
