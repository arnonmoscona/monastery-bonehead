package net.projectmonastery.monastery.bonehead.poc;

import net.projectmonastery.monastery.api.core.Capability;
import net.projectmonastery.monastery.api.core.Node;
import net.projectmonastery.monastery.cando.NodeAnnouncement;
import net.projectmonastery.monastery.bonehead.impl.BoneHeadedNodeBuilder;
import net.projectmonastery.monastery.bonehead.impl.GreetingCapability;
import net.projectmonastery.monastery.bonehead.impl.GreetingCapabilityImpl;

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
        try {
            new BoneHeadedNodeBuilder()
                    .add(new GreetingCapabilityImpl())
                    .add(new MyCapability())
                    .build()
                    .connect()
            .thenAccept(node -> {
                node.getCapability(NodeAnnouncement.class).thenAccept(NodeAnnouncement::announce);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
