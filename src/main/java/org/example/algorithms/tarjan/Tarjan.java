package org.example.algorithms.tarjan;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class Tarjan {
    private int V;

    private final LinkedList<Integer>[] adj;
    private int time;


    @SuppressWarnings("unchecked") Tarjan(int v) {
        V = v;
        adj = new LinkedList[v];

        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }

        time = 0;
    }


    void addEdge(int v, int w) {
        adj[v].add(w);
    }


    void SccUtil(int u, int low[], int disc[],
                 boolean stackMember[], Stack<Integer> stack) {
        disc[u] = time;
        low[u] = time;
        time++;
        stackMember[u] = true;
        stack.push(u);

        int n;
        Iterator<Integer> i = adj[u].iterator();
        while (i.hasNext()) {
            n = i.next();
            if (disc[n] == -1) {
                SccUtil(n, low, disc, stackMember, stack);
                low[u] = Math.min(low[u], low[n]);
            }
            else if (stackMember[n]) {
                low[u] = Math.min(low[u], disc[n]);
            }
        }

        int w = -1;
        if (low[u] == disc[u]) {
            while (w != u) {
                w = (int)stack.pop();
                System.out.print(w + " ");
                stackMember[w] = false;
            }
            System.out.println();
        }
    }


    void SCC() {
        int[] disc = new int[V];
        int[] low = new int[V];
        for (int i = 0; i < V; i++) {
            disc[i] = -1;
            low[i] = -1;
        }

        boolean[] stackMember = new boolean[V];
        Stack<Integer> stack = new Stack<Integer>();

        for (int i = 0; i < V; i++) {
            if (disc[i] == -1) {
                SccUtil(i, low, disc, stackMember, stack);
            }
        }
    }


    public static void main(String[] args) {
        Tarjan tarjan = new Tarjan(5);

        tarjan.addEdge(1,0);
        tarjan.addEdge(0,2);
        tarjan.addEdge(2,1);
        tarjan.addEdge(0,3);
        tarjan.addEdge(3,4);

        System.out.println("SSC in this graph ");
        tarjan.SCC();
    }
}
