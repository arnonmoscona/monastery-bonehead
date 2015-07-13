package net.projectmonastery.monastery.bonehead.impl;

import net.projectmonastery.monastery.api.core.Node;

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
