from random import random
import random

from GraphInterface import GraphInterface
from my_node import my_node


class DiGraph (GraphInterface):

    def __init__(self, nodes: dict, node_edge: dict):
        # dict of nodes: key = node_id, value = node object
        self.nodes = nodes
        # dict of edge: key = src node, value = dict, that key = dest node, value = weight of edge
        self.node_edge = node_edge
        self.node_Size = len(nodes.values())
        self.mc = 0
        # this.myEdgeList = myEdgeList;
        sum = 0
        for x in self.node_edge.keys():
            sum += len(node_edge.get(x))
        self.edge_Size = sum

    def __init__(self):
        # dict of nodes: key = node_id, value = node object
        self.nodes = {}
        # dict of edge: key = src node, value = dict, that key = dest node, value = weight of edge
        self.node_edge = {}
        self.node_Size = 0
        self.mc = 0
        # this.myEdgeList = myEdgeList;
        self.edge_Size = 0

    def v_size(self) -> int:
        return self.node_Size

    def e_size(self) -> int:
        return self.edge_Size

    def get_mc(self) -> int:
        return self.mc

    def add_edge(self, id1: int, id2: int, weight: float) -> bool:
        # this edge already exist
        if self.node_edge.__contains__(id1) and self.node_edge.get(id1).__contains__(id2):
            return False
        else:
            # the nodes exists
            if self.nodes.__contains__(id1) and self.nodes.__contains__(id2):
                # if this key already exist so update it - add to his dictionary the new edge
                if self.node_edge.__contains__(id1):
                    self.node_edge.get(id1).update({id2: weight})
                # if this key not exist so add to edge_node dictionary the new edge
                else:
                    self.node_edge.update({id1: {id2: weight}})
                self.edge_Size += 1
                self.mc += 1
                return True
            else:
                return False

    def add_node(self, node_id: int, pos: tuple = None) -> bool:
        # this node already exist
        if self.nodes.__contains__(node_id):
            return False
        else:
            # create a new node from the data obtained in the functions
            if pos is None:
                x = random.randint(32, 35)
                x += random.random()
                y = random.randint(32, 35)
                y += random.random()
                new_node = my_node(node_id, x, y, 0)
            else:
                new_node = my_node(node_id, float(pos[0]), float(pos[1]), float(pos[2]))
            # update the dict of nodes, and edge_node
            self.nodes.update({node_id: new_node})
            self.node_edge.update({node_id: {}})
            self.node_Size += 1
            self.mc += 1
            return True

    def remove_edge(self, node_id1: int, node_id2: int) -> bool:
        # this edge exist
        if self.node_edge.__contains__(node_id1) and self.node_edge.get(node_id1).__contains__(node_id2):
            self.node_edge.get(node_id1).__delitem__(node_id2)
            self.edge_Size -= 1
            self.mc += 1
            return True
        # this edge not exist
        else:
            return False

    def remove_node(self, node_id: int) -> bool:
        # if this node exist
        if self.nodes.__contains__(node_id):
            # delete from the nodes dict
            self.nodes.__delitem__(node_id)
            # delete the edge that this node is their src:
                # count the number of edges
            sum = len(self.node_edge.get(node_id))
            self.node_edge.__delitem__(node_id)
            self.edge_Size -= sum
            self.mc += sum
            # delete the edge that this node is their dest:
            for i in self.node_edge.keys():
                for j in self.node_edge.get(i).keys():
                    # if this node is dest of edge, delete this edge
                    if j == node_id:
                        self.node_edge.get(i).__delitem__(node_id)
                        self.edge_Size -= 1
                        self.mc += 1
                        # if in src node, you find dest node, that it is the node_id, break of the loop and go to the next i.
                        # because not exist more edge that start in this src, and end in this dest.
                        break
            self.node_Size -= 1
            self.mc += 1
            return True
        else:
            return False

    def get_all_v(self) -> dict:
        return self.nodes.__str__()

    def all_in_edges_of_node(self, id1: int) -> dict:
        if self.node_edge.__contains__(id1):
            dict_dest = {}
            for i in self.node_edge.keys():
                for j in self.node_edge.get(i).keys():
                    if j == id1:
                        dict_dest.update({i: self.node_edge.get(i).get(j)})
                        break
            return dict_dest
        else:
            return None

    def all_out_edges_of_node(self, id1: int) -> dict:
        if self.node_edge.__contains__(id1):
            return self.node_edge.get(id1).__str__()
        else:
            return None

    def __repr__(self):
        return f"Graph: |V|={self.node_Size}, |E|={self.edge_Size}"
