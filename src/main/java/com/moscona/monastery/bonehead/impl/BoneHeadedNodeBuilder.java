package com.moscona.monastery.bonehead.impl;

import com.moscona.monastery.api.core.Capability;
import com.moscona.monastery.api.core.Node;
import com.moscona.monastery.api.core.NodeBuilder;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Arnon Moscona on 5/13/2015.
 */
public class BoneHeadedNodeBuilder implements NodeBuilder<Integer> {
    ArrayList<Capability> capabilities;

    public BoneHeadedNodeBuilder() {
        capabilities = new ArrayList<>();
        capabilities.add(new BoneHeadedAnnouncementCapability()); // critical for our implementation and comes by default
    }

    @Override
    public NodeBuilder<Integer> add(Capability... capabilities) {
        this.capabilities.addAll(Arrays.asList(capabilities));
        return this;
    }

    @Override
    public Node<Integer> build() throws InvalidStateException {
        BoneHeadedNode node = new BoneHeadedNode(capabilities);
        capabilities.forEach(capability -> capability.bind(node));
        capabilities.forEach(Capability::onAllCapabilitiesBound);

        return node;
    }
}
