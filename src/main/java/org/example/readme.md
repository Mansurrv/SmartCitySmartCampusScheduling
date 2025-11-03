# Tarjan's algorithm to find strongly connected components

A directed graph is strongly connected if there is a path between all pairs of vertices. A strongly connected component of directed graph is maximal strongly connected subgraphs. 

## Tarjan's algorithm based on this facts:

### DFS search produces a DFS tree/forest;
1. SCC form subtrees of the DFS tree;
2. If we can find such subtree, then we can print/store all nodes in that subtree and it will be one Strongly Connected Component;
3. There is no back edge from one Strongly Connected Component to another.

To find the head of Strongly Connected Component, we must calculate the disc and low array. 
The low[u] indicates earliest visited vertex, that can be reached from a subtree rooted with u. 
A node `u` is head if `disc[u] == low[u]`. <br><br>

### Strongly Connected Component is relates to directed graph, but the Disc and Low values relate to bith directed and undirected graph, so in the above pick we must have taken an undeirected graph.

1. Disc: this is time, when a node visites first while DFS traversal.
2. FOR nodes: A,B,C and J;
3. The DFS tree Disc value are: 1,2,3 and 10;

#### Low: in the DFS tree, Tree takes us forward, from the ancestor to one of its descendants.

#### The `low-link` value is the smallest id that reachable from that node when doing a DFS.

## The time complexity of this algorithm: 
Time complexity: `O(V + E)`.<br>
Auxiliary space; `O(V)`.

# Topological Sorting using BFS - Kahn's Algorithm

A Directed Acyclic Graph having V vertices and E edges, find any Topological Sorted ordering of the graph.

## Topological Sorted order

It is a linear ordering of vertices such that for every directed edge u -> v, vertex u comes before v in the ordering.

## For Example:

Input: `adj[][] = [[1], [2], [], [2, 4], []]` <br>
Output: `[0, 3, 1, 4, 2]`

#### Explanation: For every pair of vertex(u -> v) in the ordering, u comes before v.

Input: `adj[][]= [[1], [2], [3], [], [5], [1, 2]]` <br>
Output: `[0, 4, 5, 1, 2, 3]`

#### Explanation: For every pair of vertex(u -> v) in the ordering, u comes before v.

The idea is to use Kahn’s Algorithm, which applies BFS to generate a valid topological ordering.  We first compute the in-degree of every vertex — representing how many incoming edges each vertex has. Then, all vertices with an in-degree of 0 are added to a queue, as they can appear first in the ordering.  We repeatedly remove a vertex from the queue, add it to our result list, and reduce the in-degree of all its adjacent vertices. If any of those vertices now have an in-degree of 0, they are added to the queue.  This process continues until the queue is empty, and the resulting order represents one valid topological sort of the graph.

## The time complexity of Topological sort Kahn:
Time complexity: `O(V + E)`. For performing BFS.
Auxiliary space: `O(V)`.

## Shortest Path in Directed Acyclic Graph

A Weighted Directed Acyclic Graph and a source vertex in the graph, find the shortest paths from given source to all other vertices.

For a general weighted graph, we can calculate single source shortest distances in O(VE) time using Bellman–Ford Algorithm. For a graph with no negative weights, we can do better and calculate single source shortest distances in O(E + VLogV) time using Dijkstra's algorithm. Can we do even better for Directed Acyclic Graph (DAG)? We can calculate single source shortest distances in O(V+E) time for DAGs. The idea is to use Topological Sorting.

We initialize distances to all vertices as infinite and distance to source as 0, then we find a topological sorting of the graph. Topological Sorting of a graph represents a linear ordering of the graph (See below, figure (b) is a linear representation of figure (a) ). Once we have topological order (or linear representation), we one by one process all vertices in topological order. For every vertex being processed, we update distances of its adjacent using distance of current vertex.

## Step by step finding shortest distances.

1. Initialize dist[] = {INF, INF, ....} and dist[s] = 0 where s is the source vertex. 
2. Create a topological order of all vertices. 
3. Do following for every vertex u in topological order. 
4. ...........Do following for every adjacent vertex v of u 
5. ..................if (dist[v] > dist[u] + weight(u, v)) 
6. ...........................dist[v] = dist[u] + weight(u, v) 

## Time Complexity.

Time complexity of topological sorting is `O(V+E)`. After finding topological order, the algorithm process all vertices and for every vertex, it runs a loop for all adjacent vertices. Total adjacent vertices in a graph is `O(E)`. So the inner loop runs `O(V+E)` times. Therefore, overall time complexity of this algorithm is `O(V+E)`.

## Auxiliary space: `O(V + E)`

# Further description of all algarith that i write.

## Aim of project:

The project combines graph structural analysis tasks with path analysis, which is a standard approach for planning in systems with dependencies.

## Aim of Tarjan's Algorithm.

Find all strongly connected components. Each strongly connected component is a set of tasks that are cyclically dependent on each other and should therefore be treated as a single logical element in the plan. 

After Tarjan's algorithm we takes list of strongly connected components and their sizes.

## Aim of Kahn.

Order strongly connected components in GSCCs so that for any dependency `A -> B`, component A always comes before component B.

After Kahn we takes topological order of components.

## Aim shortest/longest path.

In DAGs, the shortest and longest path can be found in linear time `O(V + E)` using dynamic programming in topological order, unlike general graphs, where a slower algorithm such as Dijkstra's (O(E+VlogV)) or Bellman-Ford (O(VE)) is required.

### Single-Source Shortest Paths (SSSP)

For each vertex v in topological order: `Shortest(v)=minu→v​(Shortest(u)+weight(u→v))`. Initial values: 0 for the source, ∞ for the rest.

### Longest Path

For each vertex v in topological order: `Longest(v)=maxu→v​(Longest(u)+weight(u→v))`. Initial values: 0 for the source, −∞ for the rest.

## Practical Recommendations

1. Resources should be allocated primarily to tasks that are part of the critical path. Accelerating tasks that are not part of the critical path will not affect the overall completion time.
2. Graphs with low SCC Count (e.g., 1) and high density (e.g., graph_6 with E=90) are indicative of a highly interdependent system. These large SCCs should be analyzed for the possibility of breaking them down into smaller, acyclic subtasks for more flexible planning.
3. Since all three algorithms (SCC, Topo Sort, DAG-SP) have linear complexity O(V+E), the planning system will scale effectively even with a significant increase in the number of tasks and dependencies in Smart Campus/City.

## The results in the directory [results](../../../../../results/results.csv).

