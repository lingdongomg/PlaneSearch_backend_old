package cn.whpu.webserver.Search.graph;

/*
  邻接表存图
 */

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Graph {
    private final HashMap<String, Node> nodes;

    public Graph() {
        nodes = new HashMap<>();
    }

    public Graph(String filename) {
        this();
        readFromFile(filename);
    }

    public Graph(HashMap<String,Node> nodes) {
        this.nodes = nodes;
    }

    public int getNodeNum() {
        return nodes.size();
    }

    public int getEdgeNum() {
        int edgeCount = 0;
        for (Node node : nodes.values()) {
            edgeCount += node.getEdges().size();
        }
        return edgeCount;
    }

    public void addNode(String label) {
        if (!nodes.containsKey(label))
            nodes.put(label,new Node(label));
    }

    public void addEdge(String label1, String label2, Integer weight) {
        if (!nodes.containsKey(label1))
            addNode(label1);
        if (!nodes.containsKey(label2))
            addNode(label2);
        nodes.get(label1).addEdge(label2,weight);
    }

    public HashMap<String,Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdgeList() {
        List<Edge> edgeList = new LinkedList<>();

        for (Node node : nodes.values()) {
            edgeList.addAll(node.getEdges());
        }

        return edgeList;
    }

    public Node getNode(String label) {
        return nodes.get(label);
    }

    // 获取一个新图
    public Graph transpose() {
        HashMap<String,Node> newNodes = new HashMap<>();
        for (String nodename : nodes.keySet()) {
            newNodes.put(nodename,new Node(nodename));
        }

        for (String nodename : nodes.keySet()) {
            Node node = nodes.get(nodename);
            Set<String> toNodeSet = node.getToNodeSet();
            HashMap<String, Integer> adjacency = node.getAdjacency();
            for (String nodename2 : toNodeSet) {
                newNodes.get(nodename2).addEdge(nodename,adjacency.get(nodename2));
            }
        }
        return new Graph(newNodes);
    }

    // 从文件中读图
    public void readFromFile(String fileName) {
        String[] split = fileName.split("\n");
        for (String s : split) {
            String[] split1 = s.split("\\s");
            if (split1.length == 3) {
                addEdge(split1[0],split1[1],Integer.parseInt(split1[2]));
            }
        }
    }
}
