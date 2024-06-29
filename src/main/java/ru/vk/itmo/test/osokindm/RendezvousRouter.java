package ru.vk.itmo.test.osokindm;

import one.nio.util.Hash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class RendezvousRouter {

    private final List<Node> nodes;

    public RendezvousRouter(List<String> nodeUrls) {
        nodes = new ArrayList<>();
        for (int i = 0; i < nodeUrls.size(); i++) {
            nodes.add(new Node(nodeUrls.get(i), i));
        }
    }

    public Node getNode(String key) {
        if (key == null) {
            return null;
        }
        int max = Integer.MIN_VALUE;
        Node maxHashNode = null;
        for (Node node : nodes) {
            int hash = Hash.murmur3(node.name + key);
            if (hash > max) {
                max = hash;
                maxHashNode = node;
            }
        }
        return maxHashNode;
    }

    @SuppressWarnings("MixedMutabilityReturnType")
    public List<Node> getNodes(String key, int nodeAmount) {
        if (key == null) {
            return Collections.emptyList();
        }
        TreeMap<Integer, Node> sortedNodes = new TreeMap<>();
        for (Node node : nodes) {
            int hash = Hash.murmur3(node.name + key);
            sortedNodes.put(hash, node);
        }
        List<Node> selectedNodes = new ArrayList<>();
        int nodesNeeded = nodeAmount;
        for (Node node : sortedNodes.values()) {
            if (nodesNeeded == 0) break;
            selectedNodes.add(node);
            nodesNeeded--;
        }

        return selectedNodes;
    }

}
