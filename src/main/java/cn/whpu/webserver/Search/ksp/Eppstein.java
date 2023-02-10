package cn.whpu.webserver.Search.ksp;


import cn.whpu.webserver.Search.graph.Edge;
import cn.whpu.webserver.Search.graph.Graph;
import cn.whpu.webserver.Search.graph.Node;
import cn.whpu.webserver.Search.util.Dijkstra;
import cn.whpu.webserver.Search.util.Path;
import cn.whpu.webserver.Search.util.SPT;
import cn.whpu.webserver.Search.util.SPTNode;

import java.util.*;

/**
 * 计算两个点之间的k条最短路径M
 * <p>
 * 时间复杂度O(m+nlogn+k)
 */
public class Eppstein {

    public Eppstein() {
    }

    /**
     * 计算sidetrack edge cost的集合
     * <p>
     * 每个sidetrack edge(u,v) 都是最短路径树中没有的一条边
     * 对于每个sidetrack edge(u, v) 计算S(u, v)=w(u,v) + d(v) - d(u)，d(v)是从节点v到目标的最短路径的cost
     *
     * @param graph 图
     * @param tree  SPT
     */
    protected static HashMap<String, Integer> computeSidetrackEdgeCosts(Graph graph, SPT tree) {
        HashMap<String, Integer> sidetrackEdgeCostMap = new HashMap<>();
        List<Edge> edgeList = graph.getEdgeList();
        for (Edge edge : edgeList) {
            // 如果目标节点是否可以从当前边的出顶点到达并且当前边是sidetrack edge，则计算sidetrack edge的cost
            String tp = tree.getParentOf(edge.getStartnode());
            if (tp == null || !tp.equals(edge.getEndnode())) {
                Integer sidetrackEdgeCost = edge.getWeight() + tree.getNodes().get(edge.getEndnode()).getDist() - tree.getNodes().get(edge.getStartnode()).getDist();
                sidetrackEdgeCostMap.put(edge.getStartnode() + "," + edge.getEndnode(), sidetrackEdgeCost);
            }
        }

        return sidetrackEdgeCostMap;
    }

    /**
     * 计算节点v的子堆H_out(v)
     *
     * @param nodename            节点v的nodename
     * @param graph                图
     * @param sidetrackEdgeCostMap 图中每个sidetrack的cost
     * @param nodeHeaps            堆H_out(v)的索引
     * @param edgeHeaps            用sidetrack edge实现的H_out(v)的索引
     */
    protected static void computeOutHeap(String nodename, Graph graph, HashMap<String, Integer> sidetrackEdgeCostMap, HashMap<String, EppsteinHeap> nodeHeaps, HashMap<String, EppsteinHeap> edgeHeaps) {
        Node node = graph.getNode(nodename);
        // 维护除了第一个sidetrack，按sidetrack的cost排序
        ArrayList<Edge> sidetrackEdges = new ArrayList<>();
        Edge bestSidetrack = null;
        Integer minSidetrackCost = Integer.MAX_VALUE;
        // 迭代v的输出边
        for (String neighbor : node.getToNodeSet()) {
            String edgeLabel = nodename + "," + neighbor;
            // 如果当前边为sidetrack edge
            if (sidetrackEdgeCostMap.containsKey(edgeLabel)) {
                Integer sidetrackEdgeCost = sidetrackEdgeCostMap.get(edgeLabel);
                // 并且经过当前的sidetrack edge到节点v更近
                if (sidetrackEdgeCost < minSidetrackCost) {
                    // 如果已经有了bestsidetrack，则将bestsidetrack添加到list中
                    if (bestSidetrack != null) {
                        sidetrackEdges.add(bestSidetrack);
                    }
                    // 并更新当前的sidetrack为bestsidetrack
                    bestSidetrack = new Edge(nodename, neighbor, node.getAdjacency().get(neighbor));
                    minSidetrackCost = sidetrackEdgeCost;
                } else {
                    // 如果经过当前sidetrack edge不是成本最低的，就添加到list中
                    sidetrackEdges.add(new Edge(nodename, neighbor, node.getAdjacency().get(neighbor)));
                }
            }
        }

        if (bestSidetrack != null) {
            // 如果v有outgoning sidetrack edge，则用v的所有outgoing sidetrack edge构建一个堆，堆顶为cost最低的sidetrack edge
            EppsteinHeap bestSidetrackHeap = new EppsteinHeap(bestSidetrack, sidetrackEdgeCostMap.get(bestSidetrack.getStartnode() + "," + bestSidetrack.getEndnode()));

            // 用v的其余sidetrack edges作为另一个堆
            EppsteinArrayHeap arrayHeap = new EppsteinArrayHeap();
            if (sidetrackEdges.size() > 0) {
                bestSidetrackHeap.setNumOtherSidetracks(bestSidetrackHeap.getNumOtherSidetracks() + 1);
                for (Edge edge : sidetrackEdges) {
                    EppsteinHeap sidetrackHeap = new EppsteinHeap(edge, sidetrackEdgeCostMap.get(edge.getStartnode() + "," + edge.getEndnode()));
                    edgeHeaps.put(edge.getStartnode() + "," + edge.getEndnode(), sidetrackHeap);
                    arrayHeap.add(sidetrackHeap);
                }

                // 将从第二条到最后一条cost最低的2-heap添加为cost最低的sidetrack edge的子边
                bestSidetrackHeap.addChild(arrayHeap.toEppsteinHeap());
            }

            // 更新H_out(v)的索引
            nodeHeaps.put(nodename, bestSidetrackHeap);
            // 最低cost sidetrack edge的索引
            edgeHeaps.put(bestSidetrack.getStartnode() + "," + bestSidetrack.getEndnode(), bestSidetrackHeap);
        }
    }

    /**
     * 将路径的子路径加入优先队列
     *
     * @param kpathImplicit 路径的隐式表示
     * @param ksp           最短路径列表
     * @param pathPQ        子路径的优先队列
     */
    protected static void addExplicitChildrenToQueue(EppsteinPath kpathImplicit, ArrayList<Path> ksp, PriorityQueue<EppsteinPath> pathPQ) {
        for (EppsteinHeap childHeap : kpathImplicit.getHeap().getChildren()) {
            int prefPath = kpathImplicit.getPrefPath();

            // 计算新的子路径的cost
            Integer candidateCost = ksp.get(prefPath).getTotalCost() + childHeap.getSidetrackCost();

            // 将子路径添加到优先队列中
            EppsteinPath candidate = new EppsteinPath(childHeap, prefPath, candidateCost);
            pathPQ.add(candidate);
        }
    }

    /**
     * @param outrootHeaps  堆H_T(v)的索引
     * @param kpathImplicit 路径的隐式表示
     * @param ksp           最短路径列表
     * @param pathPQ        子路径的优先队列
     */
    protected static void addCrossEdgeChildToQueue(HashMap<String, EppsteinHeap> outrootHeaps, EppsteinPath kpathImplicit, int prefPath, ArrayList<Path> ksp, PriorityQueue<EppsteinPath> pathPQ) {
        if (outrootHeaps.containsKey(kpathImplicit.getHeap().getSidetrack().getEndnode())) {
            EppsteinHeap childHeap = outrootHeaps.get(kpathImplicit.getHeap().getSidetrack().getEndnode());

            // 计算新的子路径的cost
            Integer candidateCost = ksp.get(prefPath).getTotalCost() + childHeap.getSidetrackCost();

            // 将子路径添加到优先队列
            EppsteinPath candidate = new EppsteinPath(childHeap, prefPath, candidateCost);
            pathPQ.add(candidate);
        }
    }

    /**
     * 为最短路径树T中的节点v及其子节点生成子堆H_T(v)，使用递归深度优先搜索最短路径树T的转置图T'
     * <p>
     * 树T是用指向父树的指针来表示的
     *
     * @param nodeLabel        节点v
     * @param currentArrayHeap v的父结点在最短路径树中的堆
     * @param nodeHeaps        堆H_out(v)的索引
     * @param outrootHeaps     堆H_T(v)的索引
     * @param reversedSPT      最短路径树T的转置图T'
     */
    protected static void recursiveOutrootHeaps(String nodeLabel, EppsteinArrayHeap currentArrayHeap, HashMap<String, EppsteinHeap> nodeHeaps, HashMap<String, EppsteinHeap> outrootHeaps, Graph reversedSPT) {
        EppsteinHeap sidetrackHeap = nodeHeaps.get(nodeLabel);

        if (sidetrackHeap != null) {
            currentArrayHeap = currentArrayHeap.clone();
            currentArrayHeap.addOutroot(sidetrackHeap);
        }

        EppsteinHeap currentHeap = currentArrayHeap.toEppsteinHeap2();

        if (currentHeap != null) {
            outrootHeaps.put(nodeLabel, currentHeap);
        }

        for (String neighbor : reversedSPT.getNode(nodeLabel).getAdjacency().keySet()) {
            recursiveOutrootHeaps(neighbor, currentArrayHeap, nodeHeaps, outrootHeaps, reversedSPT);
        }
    }

    /**
     * Eppstein算法计算k短路
     * <p>
     * 首先用Dijkstra算最短路，第二短路必须在节点u的某个地方偏离这条最短路径，边(u,v)为sidetrack edge。然后它遵循从v到t的最短路径
     * <p>
     * 设T表示以目标节点T为根的最短路径树，每个节点v都包含一条到节点v(w)的父节点的边(v,w)，沿着它到节点T的最短路径。
     * <p>
     * 图中的所有其他边(u,v)都是sidetrack edge。也就是说这包括任何一条节点v不在从u到t的最短路径上的边(u,v)。
     * <p>
     * 从s到t的所有路径都可以用路径中出现的sidetrack edge唯一地表示。
     * <p>
     * 路径中所有不是sidetrack edge的边都隐式地表示在最短路径树T中。
     * <p>
     * 所有从s到t的路径都可以表示为一棵树H，树的根节点没有sidetrack edge，它的所有子节点可能是一条sidetarck edge，每个sidetrack edge的子节点是有第二条sidetrack edge的路径，以此类推。
     * <p>
     * 从start到end的每一条路径都对应着一条从H的根到H的某个子节点的路径。
     * <p>
     * 每一个额外的sidetrack edge创建一个新的路径的成本与其父路径相同或大于其父路径的。
     * <p>
     * 找到k条最短路径的一种可能的方法是，保持一个候选路径的优先队列，从优先队列中取出路径后，它在堆中的子路径将被添加到优先队列中。
     * <p>
     * 关于这个堆的两个结论:
     * <p>
     * H中的每个节点最多有O(E)个子节点，其中E是图中的边数。
     * <p>
     * 如果G有环，那么这个堆是无限的。
     * <p>
     * 上面的可以参考如何用堆解决KSP问题
     * <p>
     * epppstein的算法是一种重新建立这个堆的方法，使每个堆节点最多有4个子节点
     * <p>
     * 按照原始堆H的形式，这个重新建立的堆R有以下形式:
     * <p>
     * 对于H中的每个堆节点，选择它的最佳子节点B。
     * <p>
     * 这个子节点被称为“cross edge”子节点。
     * <p>
     * 子节点 B在H中有N个兄弟节点。
     * <p>
     * 把这N个兄弟节点从它的父结点中移除，更改为堆R中B的子结点。
     * <p>
     * 最佳子结点B有多达3个兄弟结点作为直接子结点(加上它自己在原始堆H中的最佳“cross edge”子结点，这在R中总共产生了4个可能的子结点)。
     * <p>
     * 在它的兄弟子堆中，它在R中的一个子堆是一个新的二叉堆的根堆，这个二叉堆包含了B在H中的所有兄弟堆，它们都是指向G中同一个节点的sidetrack edges。
     * <p>
     * 所有B在H中的兄弟结点都放在一个二叉堆中，B作为R中的根结点。
     * <p>
     * 因为这是一个二叉堆，B有最多两个这种类型的子堆。
     * <p>
     * 因此，B在R中最多有4个子节点。R中的任何“cross edge”子节点最多有4个子节点，而其他节点属于2-heap，最多只能有2个子节点。
     *
     * @param graph 图文件
     * @param start 起点
     * @param end   终点
     * @param K     k短路
     * @return 返回从起点到终点的最短路列表，从小到大排序
     */
    public List<Path> ksp(Graph graph, String start, String end, int K) {
        // 计算目标节点的最短路径树T(图中每个节点到目标节点的最短路径)
        SPT tree;
        try {
            tree = Dijkstra.shortestPathTree(graph.transpose(), end);
        } catch (Exception e) {
            tree = new SPT(end);
        }

        // 计算 sidetrack edge 的cost
        HashMap<String, Integer> sidetrackEdgeCostMap = computeSidetrackEdgeCosts(graph, tree);

        // 创建索引
        HashMap<String, EppsteinHeap> nodeHeaps = new HashMap<>(graph.getNodeNum());
        HashMap<String, EppsteinHeap> edgeHeaps = new HashMap<>(graph.getEdgeNum());
        HashMap<String, EppsteinHeap> outrootHeaps = new HashMap<>();

        // 计算每个节点v的子堆H_out(v)
        // H_out(v) 由v的所有outgoing sidetrack edges组成
        for (String nodeLabel : graph.getNodes().keySet()) {
            computeOutHeap(nodeLabel, graph, sidetrackEdgeCostMap, nodeHeaps, edgeHeaps);
        }

        // 计算每个节点v的子堆H_T(v)
        // H_T(v)是v到T的最短路径上每个节点的所有best sidetrack edges组成的堆
        // 计算步骤为将v的最小迭代sidetrack edges加到堆H_T(nextT(v))中，其中nextT(v)是节点v以目标节点T为根的最短路径树中的父节点
        // 因此可以递归计算H_T(v)，这里需要从下往上递归(bottom-up)从树的根节点T开始
        // 为了能够bottom-up需要将整棵树翻转，每个节点指向其子节点
        Graph reversedSPT = new Graph();
        for (SPTNode node : tree.getNodes().values()) {
            reversedSPT.addEdge(node.getParent(), node.getNodename(), graph.getNode(node.getNodename()).getAdjacency().get(node.getParent()));
        }

        // 从T开始dfs计算每个H_T(nextT(v))的H_T(v)
        EppsteinArrayHeap rootArrayHeap = new EppsteinArrayHeap();
        recursiveOutrootHeaps(end, rootArrayHeap, nodeHeaps, outrootHeaps, reversedSPT);

        // 整个EppsteinHeap的根
        EppsteinHeap hg = new EppsteinHeap(new Edge(start, start, 0));

        // 存储待更新的k条最短路径和实际找到的k条最短路径
        ArrayList<Path> ksp = new ArrayList<>();
        PriorityQueue<EppsteinPath> pathPQ = new PriorityQueue<>();

        // 根入队
        pathPQ.add(new EppsteinPath(hg, -1, tree.getNodes().get(start).getDist()));

        // 遍历优先队列
        for (int i = 0; i < K && pathPQ.size() > 0; i++) {
            EppsteinPath kpathImplicit = pathPQ.poll();
            Path kpath = kpathImplicit.explicitPath(ksp, tree);
            ksp.add(kpath);
            // 将Eppstein堆中该路径的子节点入队
            addExplicitChildrenToQueue(kpathImplicit, ksp, pathPQ);
            // 判断是否存在cross edge
            addCrossEdgeChildToQueue(outrootHeaps, kpathImplicit, i, ksp, pathPQ);
        }

        // 返回最终结果
        return ksp;
    }
}