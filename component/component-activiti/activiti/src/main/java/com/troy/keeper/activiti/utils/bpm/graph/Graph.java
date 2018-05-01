package com.troy.keeper.activiti.utils.bpm.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private Node initial;

    public Node getInitial() {
        return initial;
    }

    public void setInitial(Node initial) {
        this.initial = initial;
    }

    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();
        visitNode(initial, nodes);

        return nodes;
    }

    public void visitNode(Node node, List<Node> nodes) {
        nodes.add(node);

        for (Edge edge : node.getOutgoingEdges()) {
            Node nextNode = edge.getDest();
            visitNode(nextNode, nodes);
        }
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        visitEdge(initial, edges);

        return edges;
    }

    public void visitEdge(Node node, List<Edge> edges) {
        for (Edge edge : node.getOutgoingEdges()) {
            edges.add(edge);

            Node nextNode = edge.getDest();
            visitEdge(nextNode, edges);
        }
    }

    public Node findById(String id) {
        for (Node node : this.getNodes()) {
            if (id.equals(node.getId())) {
                return node;
            }
        }

        return null;
    }
}
