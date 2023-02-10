package cn.whpu.webserver.Search.ksp;


import cn.whpu.webserver.Search.graph.Edge;

import java.util.ArrayList;

/**
 * n元堆
 */
public class EppsteinHeap {
    // 与堆或其子堆相关的边(u, v)
    private final Edge sidetrack;
    private Integer sidetrackCost = 0;
    private final ArrayList<EppsteinHeap> children;
    // H_out(u)-1
    private int numOtherSidetracks = 0;

    public EppsteinHeap(Edge sidetrack) {
        this.sidetrack = sidetrack;
        this.children = new ArrayList<>();
    }

    public EppsteinHeap(Edge sidetrack, Integer sidetrackCost) {
        this.sidetrack = sidetrack;
        this.sidetrackCost = sidetrackCost;
        this.children = new ArrayList<>();
    }

    public EppsteinHeap(Edge sidetrack, Integer sidetrackCost, ArrayList<EppsteinHeap> children, int numOtherSidetracks) {
        this.sidetrack = sidetrack;
        this.sidetrackCost = sidetrackCost;
        this.children = children;
        this.numOtherSidetracks = numOtherSidetracks;
    }

    public Edge getSidetrack() {
        return sidetrack;
    }

    public Integer getSidetrackCost() {
        return sidetrackCost;
    }

    public ArrayList<EppsteinHeap> getChildren() {
        return children;
    }

    public void addChild(EppsteinHeap child) {
        this.children.add(child);
    }

    public int getNumOtherSidetracks() {
        return numOtherSidetracks;
    }

    public void setNumOtherSidetracks(int numOtherSidetracks) {
        this.numOtherSidetracks = numOtherSidetracks;
    }

    public EppsteinHeap clone() {
        ArrayList<EppsteinHeap> children_clone = new ArrayList<>(children.size());
        children_clone.addAll(children);

        return new EppsteinHeap(sidetrack, sidetrackCost, children_clone, numOtherSidetracks);
    }
}
