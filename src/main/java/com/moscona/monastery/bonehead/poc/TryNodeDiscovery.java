package com.moscona.monastery.bonehead.poc;

import com.moscona.monastery.api.core.Node;
import com.moscona.monastery.bonehead.impl.BoneHeadedNodeBuilder;
import com.moscona.monastery.bonehead.impl.BoneHeadedNodeDiscovery;
import com.moscona.monastery.cando.NodeAnnouncement;
import com.moscona.monastery.cando.NodeDiscovery;

import java.util.concurrent.ExecutionException;

/**
 * Created by Arnon Moscona on 5/15/2015.
 */
public class TryNodeDiscovery {

    private void run() throws Exception {
        Node<Integer> node = new BoneHeadedNodeBuilder()
                .build();
        BoneHeadedNodeDiscovery nodeDiscovery = (BoneHeadedNodeDiscovery) node.getCapability(NodeDiscovery.class).get();
        nodeDiscovery.id = "the one";
        nodeDiscovery.addNodeDiscoveryListener(info -> {
            System.out.println("TryNodeDiscovery discovered: " + info);
            System.out.println("  know nodes: (by the time I get here there may be more)");
            nodeDiscovery.listKnowNodes().thenAccept(list->list.forEach(known-> System.out.println("  * "+known)));
        });
        // nothing should happen until we announce... then we should discover ourselves
        node.getCapability(NodeAnnouncement.class).get().announce();

        System.out.println("creating a second node and the first should discover it once announced");
        Node<Integer> node2 = new BoneHeadedNodeBuilder().build();
        node2.getCapability(NodeAnnouncement.class).get().announce();
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
