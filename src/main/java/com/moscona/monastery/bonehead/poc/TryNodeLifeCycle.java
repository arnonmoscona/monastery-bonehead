package com.moscona.monastery.bonehead.poc;

import com.moscona.monastery.api.core.Node;
import com.moscona.monastery.bonehead.impl.BoneHeadedNode;
import com.moscona.monastery.bonehead.impl.BoneHeadedNodeBuilder;
import com.moscona.monastery.cando.NodeAnnouncement;

import java.util.Optional;

/**
 * Try the async flow of a node
 */
public class TryNodeLifeCycle {
    private Node<Integer> node;

    private void run() {
        try {
            node = new BoneHeadedNodeBuilder().build();

            try {
                NodeAnnouncement<?> nodeAnnouncement = node.getCapability(NodeAnnouncement.class).get();
                nodeAnnouncement.addJoinListener(node -> System.out.println("Node joined (known via callback)"));
                nodeAnnouncement.announce().thenAcceptAsync(nodeAnnouncer -> {
                            System.out.printf("\nfinished with ID %s and state %s\n", node.getId(), nodeAnnouncer.getState());
                        }
                );
            }
            catch(Throwable ex) {
                throw new Exception("No node announcement capability available");
            }
        }
        catch (Throwable t) {
            System.err.println("Error: "+t);
            t.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        new TryNodeLifeCycle().run();
    }
}
