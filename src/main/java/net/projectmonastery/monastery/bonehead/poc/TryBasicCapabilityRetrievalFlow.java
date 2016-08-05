package net.projectmonastery.monastery.bonehead.poc;

import net.projectmonastery.monastery.api.core.Node;
import net.projectmonastery.monastery.bonehead.impl.BoneHeadedNodeBuilder;
import net.projectmonastery.monastery.bonehead.impl.GreetingCapability;
import net.projectmonastery.monastery.bonehead.impl.GreetingCapabilityImpl;

/**
 * Tty the most basic capability retrieval flow.
 */
public class TryBasicCapabilityRetrievalFlow {

    private void run() throws Exception {
        System.out.println("creating node");
        new BoneHeadedNodeBuilder()
                .add(new GreetingCapabilityImpl())
                .build()
                .connect()
        .thenAccept(node -> {
            // get a capability
            System.out.println("getting greeting capability");
            node.getCapability(GreetingCapability.class).thenAccept(greetingCapability ->
                    System.out.println(greetingCapability.greet("world")));

            System.out.println("\ndoing it the blocking way");
            System.out.println(node.getCapability(GreetingCapability.class).thenAccept(g -> {
                g.greet("simple");
            }));
        });


    }

    public static void main(String[] args) {
        try {
            new TryBasicCapabilityRetrievalFlow().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
