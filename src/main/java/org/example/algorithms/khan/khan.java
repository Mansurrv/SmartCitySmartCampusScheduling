package org.example.algorithms.khan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class khan {
    static ArrayList<Integer> sort(ArrayList<ArrayList<Integer>> adj){
        int n = adj.size();
        int[] inDegree = new int[n];
        Queue<Integer> queue = new LinkedList<>();
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int next : adj.get(i)) {
                inDegree[next]++;
            }
        }

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int top = queue.poll();
            result.add(top);
            for (int next : adj.get(top)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.add(next);
                }
            }
        }

        return result;
    }
}
