package com.moscona.monastery.bonehead.poc;

import com.moscona.monastery.api.core.Node;
import com.moscona.monastery.bonehead.impl.BoneHeadedNode;

/**
 * Try the async flow of a node
 */
public class TryNodeLifeCycle {
    private Node<Integer> node;

    private void run() {
        node = new BoneHeadedNode();
        node.announce().thenAccept(node -> {
                System.out.printf("\nfinished with ID %s and state %s\n", node.getId(), node.getState());
            }
        );
        System.out.println("creating node 2");
        new BoneHeadedNode().announce().thenAccept(node -> {
            System.out.printf("\nfinished node 2 with ID %s and state %s\n", node.getId(), node.getState());
        });
    }

    public static void main(String[] args) {
        new TryNodeLifeCycle().run();
    }
}
