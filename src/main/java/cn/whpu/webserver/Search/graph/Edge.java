package cn.whpu.webserver.Search.graph;

public class Edge implements Cloneable {
    private final String startnode;
    private final String endnode;
    private final Integer weight;

    public Edge(String startnode, String endnode, Integer weight) {
        this.startnode = startnode;
        this.endnode = endnode;
        this.weight = weight;
    }

    public String getStartnode() {
        return startnode;
    }

    public String getEndnode() {
        return endnode;
    }

    public Integer getWeight() {
        return weight;
    }

    public Edge clone() {
        return new Edge(startnode, endnode, weight);
    }
}
