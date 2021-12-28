206398984

Similar to the previous task for finding the shortest route I used Dijkstra's shortest path algorithm.
The algorithm goes through all the nodes of the graph, and checks whether it is possible to reach them from another node in the graph in a faster way,
- and if so, updates the path to this node. I used this algorithm in order to find not only the length of the short route, but also the route itself,
- through the edges. And of course, by this I also wrote the center function, which finds for each node the maximum distance from any node in the graph,

and then the node having the maximum path to another node, the minimum is the center node.
The complexity of Dijkstra's algorithm depends on the data structure that contains the nodes we will have to go through,
for me the data structure is like a list,- and array, etc. and therefore the complexity is in O(V^2).
For each vertex we will need to go through all the vertices.

The graphical interface opens for the first time according to the name of the graph you have chosen to display,
meaning the json file name. You can then perform actions on the graph as you wish. When the graph has two main choices- file->save or ->load graphs as you wish.
When load loads a new graph from a file you select, and save saves the current graph after the changes you made to it. A second option is to select edit -> and edit the graph as you wish.
In all operations there is communication with the "console", which will ask to insert a file name, or the relevant node that you want to attach, etc ..
I used pygame.

The code is documented and explained.

I will add that for the convenience of realizing the graph and functions, I implemented a class of nodes,
as well as a class of buttons for the menu in the graph view.
In the class of nodes I used a tag as a flag - to mark whether I passed the vertex or not for the various algorithms we were required to implement.


The implementation of the entire graph is based on a dictionary-type data structure
When there are 2 main dictionaries:
1. For the vertices of the graph, where the key is the id of the node, and the value is the vertex itself.
   According to the my node class I implemented.
2. A dictionary for the edges of the graph - so that there is a key - which is the vertex of the source,
   and its value is another dictionary for all the edges that come out of the same node when the key of the sub-dictionary is the target node,
   and the value - is the weight of the edge.

