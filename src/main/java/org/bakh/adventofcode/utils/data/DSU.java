package org.bakh.adventofcode.utils.data;

import lombok.Getter;


public class DSU {

    private final int[] parent;
    private final int[] size;

    @Getter
    private int numberOfSets;

    public DSU(final int n) {
        this.parent = new int[n];
        this.size = new int[n];
        this.numberOfSets = n;
        for (var i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(final int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public boolean union(final int i, final int j) {
        final var rootA = find(i);
        final var rootB = find(j);

        if (rootA == rootB) {
            return false;
        }

        if (size[rootA] < size[rootB]) {
            parent[rootA] = rootB;
            size[rootB] += size[rootA];
        } else {
            parent[rootB] = rootA;
            size[rootA] += size[rootB];
        }

        numberOfSets--;
        return true;
    }

    public int getSize(final int i) {
        return size[find(i)];
    }

}
