package com.moscona.monastery.bonehead.poc;

import com.moscona.monastery.api.core.Capability;
import com.moscona.monastery.api.core.Node;
import com.moscona.monastery.bonehead.impl.*;
import com.moscona.monastery.cando.NodeAnnouncement;

import java.util.concurrent.ExecutionException;

/**
 * Created by Arnon Moscona on 5/15/2015.
 */
public class TryCapabilityWithDependency {
    public static class MyCapability implements Capability {
        private Node<?> node;

        @Override
        public void bind(Node<?> context) {
            node = context;
        }

        @Override
        public void onAllCapabilitiesBound() throws IllegalStateException {
            System.out.println("looking for a dependency on greeting, and want to use it after we're joined");
            node.getCapability(NodeAnnouncement.class).thenAccept(nodeAnnouncement -> {
                System.out.println("got announcer");
                ((NodeAnnouncement<?>)nodeAnnouncement).addJoinListener(node -> {
                    System.out.println("join listener invoked.");
                    node.getCapability(GreetingCapability.class).thenAccept(g ->
                        System.out.println("I just joined. " + g.greet("everybody"))
                    );
                });
            });
        }
    }

    public static void main(String[] args) {
        Node<Integer> node = new BoneHeadedNodeBuilder()
                .add(new GreetingCapabilityImpl())
                .add(new MyCapability())
                .build();
        node.getCapability(NodeAnnouncement.class).thenAccept(NodeAnnouncement::announce);
    }
}
