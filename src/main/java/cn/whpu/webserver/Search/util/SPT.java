package cn.whpu.webserver.Search.util;

import java.util.HashMap;

/**
 * 最短路径树，图中根节点到其他所有点的最短路径构成的树
 */
public class SPT {
    private final HashMap<String, SPTNode> nodes;
    private final String root;

    public SPT(String root) {
        this.nodes = new HashMap<>();
        this.root = root;
    }

    public HashMap<String, SPTNode> getNodes() {
        return nodes;
    }

    public String getRoot() {
        return root;
    }

    public void addNode(SPTNode newNode) {
        nodes.put(newNode.getNodename(),newNode);
    }

    public String getParentOf(String node) {
        if (nodes.containsKey(node))
            return nodes.get(node).getParent();
        else
            return null;
    }
}
