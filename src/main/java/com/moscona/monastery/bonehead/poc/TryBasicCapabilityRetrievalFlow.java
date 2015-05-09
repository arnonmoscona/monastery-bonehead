package com.moscona.monastery.bonehead.poc;

import com.moscona.monastery.api.core.Node;
import com.moscona.monastery.bonehead.impl.BoneHeadedNode;
import com.moscona.monastery.bonehead.impl.GreetingCapability;

import java.util.concurrent.ExecutionException;

/**
 * Tty the most basic capability retrieval flow.
 */
public class TryBasicCapabilityRetrievalFlow {

    private void run() {
        System.out.println("creating node");
        Node<Integer> node = new BoneHeadedNode();

        // get a capability
        System.out.println("getting greeting capability");
        node.getCapability(GreetingCapability.class).thenAccept(optional -> {
            optional.ifPresent(greetingCapability ->
                    System.out.println(greetingCapability.greet("world")
                    ));
            if (!optional.isPresent()) {
                System.out.println("Oops. Did not get the capability");
            }
        });

        System.out.println("\ndoing it the blocking way");
        try {
            node.getCapability(GreetingCapability.class).get().ifPresent(greetingCapability ->
                    System.out.println(greetingCapability.greet("simple")
                    ));
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        };
    }

    public static void main(String[] args) {
        new TryBasicCapabilityRetrievalFlow().run();
    }
}
