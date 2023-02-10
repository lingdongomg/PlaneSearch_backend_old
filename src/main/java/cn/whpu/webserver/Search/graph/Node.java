package cn.whpu.webserver.Search.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Node {
    // 节点名称
    protected String nodename;
    // 这个点能到哪些点、边长
    protected HashMap<String,Integer> adjacency; // 邻接表

    public Node(String nodename) {
        this.nodename = nodename;
        adjacency = new HashMap<>();
    }

    public String getNodename() {
        return nodename;
    }

    public HashMap<String, Integer> getAdjacency() {
        return adjacency;
    }

    public void addEdge(String tonodename,Integer weight) {
        adjacency.put(tonodename, weight);
    }

    public Set<String> getToNodeSet() {
        return adjacency.keySet();
    }

    public LinkedList<Edge> getEdges() {
        LinkedList<Edge> edges = new LinkedList<>();
        for (String toNodeLabel : adjacency.keySet()) {
            edges.add(new Edge(nodename,toNodeLabel, adjacency.get(toNodeLabel)));
        }

        return edges;
    }
}
