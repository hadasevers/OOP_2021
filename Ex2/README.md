206398984

The algorithms I wrote for the heavy actions - such as finding the center node, etc., are the "known" algorithms.
That is - for checking the graph connected I wrote the BFS algorithm that performs a search across the graph.
To check if the graph is connected, I used a function that changes edges direction for each side, and also checks
the connect components against the movement in the opposite direction.
That is - is it possible to get out of any node, and in addition is it also possible to reach it.
The time and place complexity of the algorithm in a link graph is O(V+E),
where V represents the group of nodes in the graph and E represents the group of arcs in the graph.

Also for finding the shortest route I used Dijkstra's shortest path algorithm.
The algorithm goes through all the nodes of the graph, and checks whether it is possible to reach them from another node in the graph
in a faster way, - and if so, updates the path to this node.
I used this algorithm in order to find not only the length of the short route, but also the route itself, through the edges.
And of course, by this I also wrote the center function, which finds for each node the maximum distance from any node in the graph,
- and then the node having the maximum path to another node, the minimum is the center node.
The complexity of Dijkstra's algorithm depends on the data structure that contains the nodes we will have to go through,
- for me the data structure is like a list,- and array, etc. and therefore the complexity is in O(V^2).
For each vertex we will need to go through all the vertices.

The graphical interface opens for the first time according to the name of the graph you have chosen to display, meaning the json file name.
You can then perform actions on the graph as you wish.
When the graph has two main choices- file->save or ->load graphs as you wish. When load loads a new graph from a file you select,
and save saves the current graph after the changes you made to it.
A second option is to select edit -> and edit the graph as you wish.
In all operations there is communication with the "console", which will ask to insert a file name, or the relevant node that you want to attach, etc ..
I used java's Graphics folder.

