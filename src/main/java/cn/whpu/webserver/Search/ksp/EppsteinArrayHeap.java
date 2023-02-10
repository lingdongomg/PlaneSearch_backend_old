package cn.whpu.webserver.Search.ksp;

import java.util.ArrayList;

/**
 * 用数组表示二进制堆
 */
public class EppsteinArrayHeap {
    private final ArrayList<EppsteinHeap> arrayHeap;

    public EppsteinArrayHeap() {
        arrayHeap = new ArrayList<>(0);
    }

    public int getParentIndex(int i) {
        return (i - 1) / 2;
    }

    public void add(EppsteinHeap h) {
        arrayHeap.add(h);
        bubbleUp(arrayHeap.size() - 1);
    }

    public void addOutroot(EppsteinHeap h) {
        int current = arrayHeap.size();

        while (current > 0) {
            int parent = getParentIndex(current);
            EppsteinHeap newHeap = arrayHeap.get(parent).clone();
            arrayHeap.set(parent, newHeap);
            current = parent;
        }
        arrayHeap.add(h);
        bubbleUp(arrayHeap.size() - 1);
    }

    private void bubbleUp(int current) {
        if (current == 0)
            return;

        int parent = getParentIndex(current);
        if (arrayHeap.get(current).getSidetrackCost() >= arrayHeap.get(parent).getSidetrackCost())
            return;

        EppsteinHeap temp = arrayHeap.get(current);
        arrayHeap.set(current, arrayHeap.get(parent));
        arrayHeap.set(parent, temp);
        bubbleUp(parent);
    }

    // 将数组表示的二进制堆转换成树形式
    public EppsteinHeap toEppsteinHeap() {
        if (arrayHeap.size() == 0)
            return null;

        EppsteinHeap eh = arrayHeap.get(0);
        for (int i = 1; i < arrayHeap.size(); i++) {
            EppsteinHeap h = arrayHeap.get(i);
            arrayHeap.get(getParentIndex(i)).addChild(h);
        }

        return eh;
    }

    // 将数组表示的二进制堆转换为树形式
    //  which can fit consistently within an overall non-binary heap.
    public EppsteinHeap toEppsteinHeap2() {
        int current = arrayHeap.size() - 1;
        if (current == -1)
            return null;

        while (current >= 0) {
            EppsteinHeap childHeap = arrayHeap.get(current);
            while (childHeap.getChildren().size() > childHeap.getNumOtherSidetracks()) {
                childHeap.getChildren().remove(childHeap.getChildren().size() - 1);
            }

            int child1 = current * 2 + 1;
            int child2 = current * 2 + 2;

            if (child1 < arrayHeap.size()) {
                arrayHeap.get(current).addChild(arrayHeap.get(child1));
            }
            if (child2 < arrayHeap.size()) {
                arrayHeap.get(current).addChild(arrayHeap.get(child2));
            }
            if (current > 0) {
                current = getParentIndex(current);
            } else {
                current = -1;
            }
        }

        return arrayHeap.get(0);
    }

    public EppsteinArrayHeap clone() {
        EppsteinArrayHeap clonedArrayHeap = new EppsteinArrayHeap();
        for (EppsteinHeap heap : arrayHeap) {
            clonedArrayHeap.add(heap);
        }

        return clonedArrayHeap;
    }
}
