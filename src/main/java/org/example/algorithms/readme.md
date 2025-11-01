# Toplogical sorting using BFS - Khan's algorithm

Topological sorted order: it is linear ordering of vertices such that `u -> v`, vertex `u` comes before `v` in the ordering.

### Example no.1: <br>
`Input: adj[][] = [[1],[2], [], [2,4], []]` <br>
`Output: [0,3,1,4,2]` <br>

Explanation: for every pair of vertex `u -> v`, the vertex u comes before v in the ordering.

### Example no.2: <br>
`Input: adj[][] = [[1],[2],[3],[],[5],[1,2]]` <br>
`Output: [0,4,5,1,2,3]` <br>

Explanation: for every vertex of pair `u -> v`, the vertex u comes before the vertex v.

### The idea use to Khan's algorithm, which applies BFS to generate a valid topological ordering.
### We first compute the in-degree of every vertex - representing how many incoming edges each vertex has.
### Then all vertices with an in-degree 0 are added in queue, as they can appear first in the ordering. 
### We repeatedly remove a vertex from a queue, add it to our result list, and reduce the in-degree of all its adjacent vertices.
### If any of those vertices now have in-degree 0, they are added to queue.
### This process continuous until the queue is empty, and the resulting value represents one valid topological order of sort.

## The time complexity is: O(v + E);
