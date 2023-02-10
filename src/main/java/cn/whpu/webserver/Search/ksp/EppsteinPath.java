package cn.whpu.webserver.Search.ksp;


import cn.whpu.webserver.Search.graph.Edge;
import cn.whpu.webserver.Search.util.Path;
import cn.whpu.webserver.Search.util.SPT;

import java.util.LinkedList;
import java.util.List;

/**
 * 在执行Eppstein算法时，隐式表示候选k条最短路径的优先队列内的源目标路径的数据结构
 */
public class EppsteinPath implements Comparable<EppsteinPath> {
    EppsteinHeap heap; // pointer to the heap node and last sidetrack edge in this candidate path
    int prefPath; // index of the shorter path that this path sidetracks from
    Integer cost; // the total cost of the path

    public EppsteinPath(EppsteinHeap heap, int prefPath, Integer cost) {
        this.heap = heap;
        this.prefPath = prefPath;
        this.cost = cost;
    }

    public int getPrefPath() {
        return prefPath;
    }

    public EppsteinHeap getHeap() {
        return heap;
    }

    public Integer getCost() {
        return cost;
    }

    // Convert from the implicit representation of the path to an explicit listing of all of the edges in the path
    // There are potentially three pieces to the path:
    // 1) the path from node s (source) to node u in the parent path
    // 2) the sidetrack edge (u,v)
    // 3) the shortest path (in the shortest path tree) from node v to node t (target)
    public Path explicitPath(List<Path> ksp, SPT tree) {
        Path explicitPath = new Path();

        // If path is not the shortest path in the graph...
        if (prefPath >= 0) {
            // Get the explicit representation of the shorter parent path that this path sidetracks from
            Path explicitPrefPath = ksp.get(prefPath);

            // 1a) Identify the s-u portion of the path
            // Identify and addNode the segment of the parent path up until the point where the current path sidetracks off
            // of it.
            // In other words, if (u,v) is the sidetrack edge of the current path off of the parent path, look for the
            // last instance of node u in the parent path.
            LinkedList<Edge> edges = explicitPrefPath.getEdges();
            int lastEdgeNum = -1;
            Edge heapSidetrack = heap.getSidetrack();
            for (int i = edges.size() - 1; i >= 0; i--) {
                Edge currentEdge = edges.get(i);
                if (currentEdge.getEndnode().equals(heapSidetrack.getStartnode())) {
                    lastEdgeNum = i;
                    break;
                }
            }

            // 1b) Add the s-u portion of the path
            // Copy the explicit parent path up to the identified point where the current/child path sidetracks
            explicitPath = new Path();
            for (int i = 0; i <= lastEdgeNum; i++) {
                explicitPath.addEdge(edges.get(i));
            }

            // 2) Add the (u,v) portion of the path
            // Add the last sidetrack edge to the explicit path representation
            explicitPath.addEdge(heap.getSidetrack());
        }

        // 3) Add the v-t portion of the path
        // Add the shortest path from v (either the source node, or the incoming node of the sidetrack edge associated
        // with the current path) to the explicit path representation
        String current = heap.getSidetrack().getEndnode();
        while (!current.equals(tree.getRoot())) {
            String next = tree.getParentOf(current);
            // System.out.println(next);
            try {
                Integer edgeWeight = tree.getNodes().get(current).getDist() - tree.getNodes().get(next).getDist();
                explicitPath.addEdge(new Edge(current, next, edgeWeight));
                current = next;
            } catch (Exception e) {
                break;
            }
        }

        return explicitPath;
    }

    public int compareTo(EppsteinPath comparedNode) {
        Integer cost1 = this.cost;
        Integer cost2 = comparedNode.getCost();
        return Integer.compare(cost1, cost2);
    }

}
