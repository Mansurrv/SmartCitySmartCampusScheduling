# Tarjan's algorithm to find strongly connected components

A directed graph is strongly connected if there is a path between all pairs of vertices. A strongly connected component of directed graph is maximal strongly connected subgraphs. 

# Tarjan's algorithm based on this facts:

### DFS search produces a DFS tree/forest;
### SCC form subtrees of the DFS tree;
### If we can find such subtree, then we can print/store all nodes in that subtree and it will be one Strongly Connected Component;
### There is no back edge from one Strongly Connected Component to another.

To find the head of Strongly Connected Component, we must calculate the disc and low array. 
The low[u] indicates earliest visited vertex, that can be reached from a subtree rooted with u. 
A node `u` is head if `disc[u] == low[u]`.

### Strongly Connected Component is relates to directed graph, but the Disc and Low values relate to bith directed and undirected graph, so in the above pick we must have taken an undeirected graph.

## Disc: this is time, when a node visites first while DFS traversal.
## FOR nodes: A,B,C and J;
## The DFS tree Disc value are: 1,2,3 and 10;

## Low: in the DFS tree, Tree takes us forward, from the ancestor to one of its descendants.

### The `low-link` value is the smallest id that reachable from that node when doing a DFS.

## The time complexity of this algorithm: O(V + E);
