package com.moscona.monastery.bonehead.poc;

import com.moscona.monastery.api.core.Node;
import com.moscona.monastery.bonehead.impl.BoneHeadedNode;
import com.moscona.monastery.bonehead.impl.BoneHeadedNodeBuilder;
import com.moscona.monastery.bonehead.impl.GreetingCapability;
import com.moscona.monastery.bonehead.impl.GreetingCapabilityImpl;

/**
 * Tty the most basic capability retrieval flow.
 */
public class TryBasicCapabilityRetrievalFlow {

    private void run() {
        System.out.println("creating node");
        Node<Integer> node = new BoneHeadedNodeBuilder()
                .add(new GreetingCapabilityImpl())
                .build();

        // get a capability
        System.out.println("getting greeting capability");
        node.getCapability(GreetingCapability.class).thenAccept(greetingCapability -> System.out.println(greetingCapability.greet("world")));


        System.out.println("\ndoing it the blocking way");
        try {
            System.out.println(node.getCapability(GreetingCapability.class).get().greet("simple"));
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
    }

    public static void main(String[] args) {
        new TryBasicCapabilityRetrievalFlow().run();
    }
}
