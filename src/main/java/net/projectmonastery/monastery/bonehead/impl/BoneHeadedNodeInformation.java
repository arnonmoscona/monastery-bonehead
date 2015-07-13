package net.projectmonastery.monastery.bonehead.impl;

import net.projectmonastery.monastery.cando.NodeInformation;
import lombok.Value;

import java.util.Optional;

/**
 * Created by Arnon Moscona on 5/15/2015.
 */
@Value
public class BoneHeadedNodeInformation implements NodeInformation<Integer> {
    private Integer id;
    private String connectionString;

    protected BoneHeadedNodeInformation(BoneHeadedNode node) throws Exception {
        Optional<Integer> nodeId = node.getId();
        if (nodeId.isPresent()) {
            id = nodeId.get();
            connectionString = node.getConnectionString();
            return;
        }
        throw new Exception("Node does not have an ID.");
    }
}
