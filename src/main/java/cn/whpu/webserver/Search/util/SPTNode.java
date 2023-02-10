package cn.whpu.webserver.Search.util;


import cn.whpu.webserver.Search.graph.Node;

import java.util.HashMap;
import java.util.Set;

/**
 * 最短路径树的节点
 */

public class SPTNode extends Node implements Comparable<SPTNode> {
    // 距离
    private Integer dist;
    // 深度
    private int depth;

    public SPTNode(String label) {
        super(label);
        this.dist = 0;
    }

    public Integer getDist() {
        return dist;
    }

    public void setDist(Integer dist) {
        this.dist = dist;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setParent(String parent) {
        super.adjacency = new HashMap<>();
        super.adjacency.put(parent,0);
    }

    public String getParent() {
        Set<String> neighborLabels = super.adjacency.keySet();
        if (neighborLabels.size() != 1) {
            return null;
        }
        return super.adjacency.keySet().iterator().next();
    }

    public int compareTo(SPTNode comparedNode) {
        return Integer.compare(this.dist, comparedNode.getDist());
    }
}
