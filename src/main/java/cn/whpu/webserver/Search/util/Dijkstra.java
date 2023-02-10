package cn.whpu.webserver.Search.util;

import cn.whpu.webserver.Search.graph.Graph;
import cn.whpu.webserver.Search.graph.Node;

import java.util.HashMap;
import java.util.PriorityQueue;

public final class Dijkstra {

    private Dijkstra() {}

    /**
     * 标准的Dijkstra
     *
     * 用小根堆维护所有节点的权重，开始时将起点入队，起点的距离初始化为0
     *
     * bfs遍历小根堆，每次取堆顶元素，比如堆顶元素为B，起点是A
     * 如果A->B->C比A->C更短，就更新A->C的距离为A->B->C的距离，然后将C入队
     * 当小根堆为空时遍历完成
     */
    public static SPT shortestPathTree(Graph graph, String startNodeName) throws Exception {
        HashMap<String, Node> nodes = graph.getNodes();
        if (!nodes.containsKey(startNodeName))
            throw new Exception("Start node not found");
        // 存最终答案
        SPT predecessorTree = new SPT(startNodeName);
        // 优先队列模拟小根堆
        PriorityQueue<SPTNode> pq = new PriorityQueue<>();
        for (String nodeLabel:nodes.keySet()) {
            SPTNode newNode = new SPTNode(nodeLabel);
            newNode.setDist(Integer.MAX_VALUE);
            newNode.setDepth(Integer.MAX_VALUE);
            predecessorTree.addNode(newNode);
        }
        SPTNode sourceNode = predecessorTree.getNodes().get(predecessorTree.getRoot());
        sourceNode.setDist(0);
        sourceNode.setDepth(0);
        pq.add(sourceNode);

        while (!pq.isEmpty()) {
            SPTNode current = pq.poll();
            String currLabel = current.getNodename();
            HashMap<String, Integer> neighbors = nodes.get(currLabel).getAdjacency();
            for (String currNeighborLabel:neighbors.keySet()) {
                SPTNode neighborNode = predecessorTree.getNodes().get(currNeighborLabel);
                Integer currDistance = neighborNode.getDist();
                Integer newDistance = current.getDist() + nodes.get(currLabel).getAdjacency().get(currNeighborLabel);
                if (newDistance < currDistance) {
                    SPTNode neighbor = predecessorTree.getNodes().get(currNeighborLabel);

                    pq.remove(neighbor);
                    neighbor.setDist(newDistance);
                    neighbor.setDepth(current.getDepth() + 1);
                    neighbor.setParent(currLabel);
                    pq.add(neighbor);
                }
            }
        }

        return predecessorTree;
    }

}
