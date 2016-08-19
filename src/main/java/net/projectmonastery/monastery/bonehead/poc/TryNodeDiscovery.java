package net.projectmonastery.monastery.bonehead.poc;

import net.projectmonastery.monastery.bonehead.impl.BoneHeadedNodeBuilder;
import net.projectmonastery.monastery.bonehead.impl.BoneHeadedNodeDiscovery;
import net.projectmonastery.monastery.capability.NodeAnnouncement;
import net.projectmonastery.monastery.capability.NodeDiscovery;

/**
 * Created by Arnon Moscona on 5/15/2015.
 */
public class TryNodeDiscovery {

    private void run() throws Exception {
        new BoneHeadedNodeBuilder()
                .build().connect().thenAccept(node -> {
            node.getCapability(NodeDiscovery.class).thenAccept(cap -> {
                BoneHeadedNodeDiscovery nodeDiscovery = (BoneHeadedNodeDiscovery)cap;
                nodeDiscovery.id = "the one";
                nodeDiscovery.addNodeDiscoveryListener(info -> {
                    System.out.println("TryNodeDiscovery discovered: " + info);
                    System.out.println("  know nodes: (by the time I get here there may be more)");
                    nodeDiscovery.listKnowNodes().thenAccept(list->list.forEach(known-> System.out.println("  * "+known)));
                });
                // nothing should happen until we announce... then we should discover ourselves
                node.getCapability(NodeAnnouncement.class).thenAccept(it -> it.announce());

                System.out.println("creating a second node and the first should discover it once announced");
                new BoneHeadedNodeBuilder()
                        .buildAnd()
                        .exceptionally(ex-> {System.out.println("Exception: "+ex); return null;})
                        .thenAccept(nodeProvider -> nodeProvider.connect()
                                .thenAccept(node1 -> node1.getCapability(NodeAnnouncement.class)
                                        .thenAccept(NodeAnnouncement::announce)));
            });

        });

    }

    public static void main(String[] args) {
        try {
            new TryNodeDiscovery().run();
        } catch (Exception e) {
            System.err.println("Exception: "+e);
            e.printStackTrace(System.err);
        }
    }
}
