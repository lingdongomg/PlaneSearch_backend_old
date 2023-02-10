package cn.whpu.webserver.Search.util;

import cn.whpu.webserver.Search.graph.Edge;

import java.util.LinkedList;

/**
 * 存最终路径
 */

public class Path implements Cloneable, Comparable<Path> {
    // 路径
    private final LinkedList<Edge> edges;
    // 路径的总花费
    private Integer totalCost;

    public Path() {
        edges = new LinkedList<>();
        totalCost = 0;
    }

    public Path(LinkedList<Edge> edges) {
        this.edges = edges;
        totalCost = 0;
        for (Edge edge : edges) {
            totalCost += edge.getWeight();
        }
    }

    public LinkedList<Edge> getEdges() {
        return edges;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
        totalCost += edge.getWeight();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(totalCost);
        sb.append(": [");
        if (edges.size() > 0) {
            for (Edge edge : edges) {
                sb.append(edge.getStartnode());
                sb.append("-");
            }

            sb.append(edges.getLast().getEndnode());
        }
        sb.append("]");
        return sb.toString();
    }

    // 获取路径
    public String getPath(){
        StringBuilder s = new StringBuilder();
        if (this.edges.size() > 0) {
            for (Edge edge : edges) {
                s.append(edge.getStartnode()).append(",");
            }
            s.append(edges.getLast().getEndnode());
        }
        return s.toString();
    }

    public int compareTo(Path path2) {
        return Integer.compare(totalCost, path2.getTotalCost());
    }

    public Path clone() {
        LinkedList<Edge> edges = new LinkedList<>();

        for (Edge edge : this.edges) {
            edges.add(edge.clone());
        }

        return new Path(edges);
    }

}
