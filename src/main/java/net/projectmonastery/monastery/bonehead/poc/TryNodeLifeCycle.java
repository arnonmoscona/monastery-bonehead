package net.projectmonastery.monastery.bonehead.poc;

import net.projectmonastery.monastery.bonehead.impl.BoneHeadedNodeBuilder;
import net.projectmonastery.monastery.capabilitiy.NodeAnnouncement;

/**
 * Try the async flow of a node
 */
public class TryNodeLifeCycle {
    private void run() {
        try {
            new BoneHeadedNodeBuilder().build().connect().thenAccept(node -> {
                node.getCapability(NodeAnnouncement.class).thenAccept(nodeAnnouncement -> {
                    nodeAnnouncement.addJoinListener(n -> System.out.println("Node joined (known via callback)"));

                    nodeAnnouncement.announce().thenAccept(nodeAnnouncer -> {
                                System.out.printf("\nfinished with ID %s and state %s\n", node.getId(), nodeAnnouncer.getState());
                            }
                    );
                });
            });
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
