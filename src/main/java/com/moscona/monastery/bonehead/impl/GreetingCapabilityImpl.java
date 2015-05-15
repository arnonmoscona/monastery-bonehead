package com.moscona.monastery.bonehead.impl;

import com.moscona.monastery.api.core.Node;
import com.moscona.monastery.cando.NodeAnnouncement;

/**
 * Implementation of the greeting capability
 */
public class GreetingCapabilityImpl implements GreetingCapability {
    private Node node;

    public void onJoin() {
        System.out.println("greetingCapability: onJoin()");
    }

    @Override
    public void bind(Node<?> context) {
        node = context;
    }

    @Override
    public void onAllCapabilitiesBound() throws IllegalStateException {
//        node.getCapability(NodeAnnouncement.class).thenAccept(announcer -> announcer.ifPresent())
    }
}
