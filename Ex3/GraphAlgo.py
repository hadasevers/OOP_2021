import json
import math
import random
from copy import copy, deepcopy
from typing import List
from GraphInterface import GraphInterface
from GraphAlgoInterface import GraphAlgoInterface
from DiGraph import DiGraph
from Munche_A_2022.Ex3.src.my_node import my_node
from my_node import my_node
import pygame
from pygame import *
from button_menu import *




class GraphAlgo(GraphAlgoInterface):

    def __init__(self, graph: DiGraph = None):
        self.graph = graph


    def get_graph(self) -> GraphInterface:
        return self.graph

    def load_from_json(self, file_name: str) -> bool:
        new_graph = DiGraph()

        try:
            with open(file_name, "r") as file:
                list_of_data = json.load(file)

                for node in list_of_data.get("Nodes"):
                    if node.get('pos') is None:
                        x = random.randint(32, 35)
                        x += random.random()
                        y = random.randint(32, 35)
                        y += random.random()
                        pos = [x, y, 0]
                    else:
                        pos = node.get('pos').split(',', 3)
                    new_graph.add_node(node.get('id'), pos)

                for edge in list_of_data.get("Edges"):
                    new_graph.add_edge(edge.get('src'), edge.get('dest'), edge.get('w'))

                self.graph = new_graph
                return True

        except IOError as e:
            print(e)
            return False


    def save_to_json(self, file_name: str) -> bool:

        list_edge = []
        node_edge = self.graph.node_edge
        for n_s in node_edge.keys():
            for n_d in node_edge.get(n_s).keys():
                w = node_edge.get(n_s).get(n_d)
                edge = {"src": n_s, "w": w, "dest": n_d}
                list_edge.append(edge)

        list_node=[]
        nodes = self.graph.nodes
        for n in nodes.values():
            node = {"pos": n.get_location(), "id": n.get_id()}
            list_node.append(node)

        graph_data = {"Edges": list_edge, "Nodes": list_node}

        try:
            with open(file_name, 'w') as json_file:
                json.dump(graph_data, json_file, indent=2)
                return True

        except IOError as e:
            print(e)
            return False


    def shortest_path(self, id1: int, id2: int) -> (float, list):

        route = []
        # if one of the node not exist
        if (not self.graph.nodes.__contains__(id1)) or (not self.graph.nodes.__contains__(id2)):
            return float('inf'), route
        else:
            # hash in order to update the weight of the nodes
            node = deepcopy(self.graph.nodes)

            # dict to previous node that pointed me. key - id node, value - the node that pointed at me
            pointer = {}

            # start when the weight of the node is very high, and they don't view
            for n in node.values():
                n.set_tag(0)
                n.set_weight(1000000)

            # queue for the connection nodes
            queue = []
            # the first node
            node.get(id1).set_weight(0)
            node.get(id1).set_tag(1)
            queue.append(node.get(id1))
            pointer.update({id1: None})

            # while the queue is not empty
            while queue.__len__() != 0:
                current = self.get_min(queue)
                queue.remove(current)
                if current.get_id() == id2:
                    break
                edge = deepcopy(self.graph.node_edge.get(current.get_id()))
                for e in edge.keys():
                    new_weight = current.get_weight()+edge.get(e)
                    if new_weight < node.get(e).get_weight():
                        node.get(e).set_weight(new_weight)
                        pointer.update({e: current})
                        if node.get(e).get_tag() == 0:
                            node.get(e).set_tag(1)
                            queue.append(node.get(e))

            current = node.get(id2)
            if current.get_tag() == 0:
                return float('inf'), route  # or current.get_weight()==1000000 , or pointer.get(id2) == None   -> There is no route from id1 to id2
            else:
                current_id = current.get_id()
                route.append(current_id)
                previous = pointer.get(current.get_id())
                while previous is not None:
                    current = previous
                    current_id = current.get_id()
                    route.insert(0, current_id)
                    previous = pointer.get(current.get_id())
            current_w = node.get(id2).get_weight()
            return current_w, route

    # return the node with lowest weight
    def get_min(self, queue: list) -> my_node:
        mini = queue[0]
        for current in queue:
            if mini.get_weight() > current.get_weight():
                mini = current
        return mini

    def centerPoint(self) -> (int, float):
        mini = 1000000
        for n in self.graph.nodes.values():
            maxi = 0
            current_node = self.center(n.get_id())
            # found the maximum distance from node n
            for n2 in current_node.values():
                # if there is node that not view - this graph not connected
                if n2.get_tag == 0:
                    return None, float('inf')
                if n2.get_weight() > maxi:
                    maxi = n2.get_weight()
                # if there is node that not view - this graph not connected
                if maxi == 1000000:
                    return None, float('inf')
                if maxi < mini:
                     mini = maxi
                     ans = n
        current_id = ans.get_id()
        return current_id, mini

    # Dijkstra's_algorithm
    def center(self, node_id: int) -> dict:
        # if one of the node not exist
        if not self.graph.nodes.__contains__(node_id):
            return self.graph.nodes
        else:
            # hash in order to update the weight of the nodes
            node = deepcopy(self.graph.nodes)

            # start when the weight of the node is very high, and they don't view
            for n in node.values():
                n.set_tag(0)
                n.set_weight(1000000)

            # queue for the connection nodes
            queue = []
            # the first node
            node.get(node_id).set_weight(0)
            node.get(node_id).set_tag(1)
            queue.append(node.get(node_id))

            # while the queue is not empty
            while queue.__len__() != 0:
                current = self.get_min(queue)
                queue.remove(current)
                edge = deepcopy(self.graph.node_edge.get(current.get_id()))
                for e in edge.keys():
                    new_weight = current.get_weight() + edge.get(e)
                    if new_weight < node.get(e).get_weight():
                        node.get(e).set_weight(new_weight)
                        if node.get(e).get_tag() == 0:
                            node.get(e).set_tag(1)
                            queue.append(node.get(e))
            return node


    def TSP(self, node_lst: List[int]) -> (List[int], float):
        route = []
        dis = 0

        # the loop get the node with the min id, to start
        min_id = 1000000
        for n in node_lst:
            n_id = int(self.graph.nodes.get(n).get_id())
            if n_id < min_id:
                min_id = n_id
                min_node = self.graph.nodes.get(n)
        current = min_node
        node_lst.remove(min_node.get_id())
        route.append(min_node.get_id())

        while node_lst.__len__() != 0:
            min_id = 1000000
            # the loop get the node with the min id
            for n in node_lst:
                n_id = int(self.graph.nodes.get(n).get_id())
                if n_id < min_id:
                    min_id = n_id
                    min_node = self.graph.nodes.get(n)
                # found the short route from current node to min node
                short_route_dis, short_route_list = self.shortest_path(current.get_id(), min_node.get_id())
                # if there isn't exist route
                if short_route_dis == float('inf') or short_route_list is None:
                    return float('inf'), []
                short_route_list.pop(0)
                dis += short_route_dis
                # add the short route to route
                route.extend(short_route_list)
                node_lst.remove(min_node.get_id())

                # if there is a node in the short route list that we found, remove it from the node_lst list
                while short_route_list.__len__() != 0:
                    if node_lst.__contains__(short_route_list.__getitem__(0)):
                        node_lst.remove(short_route_list.__getitem__(0))
                    short_route_list.pop(0)
                current = min_node

        return route, dis


    def plot_graph(self) -> None:

        pygame.init()
        screen = pygame.display.set_mode((1100, 700))
        screen.fill(Color(250, 250, 250))
        display.update()

        button_w = 0
        button_h = 0

        file = Button("file", (button_h, button_w))
        save = Button("save", (button_h, button_w + 20))
        load = Button("load", (button_h, button_w + 40))

        edit = Button("edit", (button_h + 70, button_w))
        add_node = Button("add node", (button_h + 70, button_w + 20))
        remove_node = Button("remove node", (button_h + 70, button_w + 40))
        add_edge = Button("add edge", (button_h + 70, button_w + 60))
        remove_edge = Button("remove edge", (button_h + 70, button_w + 80))

        while True:

            Font2 = font.SysFont('Arial', 10, bold=False)

            def paint(screen):

                min_x = 1100
                min_y = 700
                max_x = 0
                max_y = 0
                for n in self.graph.nodes.values():
                    x = n.pos.get('x')
                    y = n.pos.get('y')
                    if x > max_x:
                        max_x = x
                    if x < min_x:
                        min_x = x
                    if y > max_y:
                        max_y = y
                    if y < min_y:
                        min_y = y


                # get the scaled data with proportions min_data, max_data relative to min and max screen dimensions
                def scale(data, min_screen, max_screen, min_data, max_data):
                    return ((data - min_data) / (max_data - min_data)) * (max_screen - min_screen) + min_screen

                # decorate scale with the correct values
                def my_scale(data, x=False, y=False):
                    if x:
                        return scale(data, 50, screen.get_width() - 50, min_x, max_x)
                    if y:
                        return scale(data, 50, screen.get_height() - 50, min_y, max_y)

                # Calculation to find the point of intersection between the rib and the vertex - to find the tip of the arrow
                def x2point(xs, ys, xd, yd, d):
                    m = (ys - yd) / (xs - xd)
                    n = yd - (m * xd)
                    a = (1 + m * m)
                    b = (-2 * xd) + (2 * n * m) - (2 * yd * m)
                    c = (-(d * d)) + (xd * xd) + (yd * yd) + (n * n) - (2 * yd * n)
                    x1 = (-b - ((b * b) - (4 * a * c))**0.5) / (2 * a)
                    x2 = (-b + ((b * b) - (4 * a * c))**0.5) / (2 * a)
                    return x1, x2, m, n

                # Calculation to find the other 2 points of the arrow
                def x1point(xd, yd, d, m1):
                    if m1 == 0:
                        m = 0
                    else:
                        m = -1 / m1
                    n = yd - (m * xd)
                    a = (1 + m * m)
                    b = (-2 * xd) + (2 * n * m) - (2 * yd * m)
                    c = (-(d * d)) + (xd * xd) + (yd * yd) + (n * n) - (2 * yd * n)
                    x1 = (-b - ((b * b) - (4 * a * c))**0.5) / (2 * a)
                    x2 = (-b + ((b * b) - (4 * a * c))**0.5) / (2 * a)
                    return x1, x2, m, n

                radius = 7

                # draw edges
                for s in self.graph.node_edge.keys():
                    src = self.graph.nodes.get(s)
                    src_x = my_scale(src.pos.get('x'), x=True)
                    src_y = my_scale(src.pos.get('y'), y=True)
                    for d in self.graph.node_edge.get(s):
                        # find the edge nodes
                        dest = self.graph.nodes.get(d)
                        dest_x = my_scale(dest.pos.get('x'), x=True)
                        dest_y = my_scale(dest.pos.get('y'), y=True)
                        # draw the line
                        pygame.draw.line(screen, Color(61, 72, 126), (src_x, src_y), (dest_x, dest_y), width=1)
                        x1, x2, m, n = x2point(src_x, src_y, dest_x, dest_y, 10)
                        # draw the arrow
                        if dest_x > src_x:
                            x = min(x1, x2)
                        else: x = max(x1, x2)
                        y = m * x + n
                        x1, x2, m, n = x2point(src_x, src_y, x, y, radius)
                        if dest_x > src_x:
                            x = min(x1, x2)
                        else: x = max(x1, x2)
                        y = m * x + n
                        x3, x4, m, n = x1point(x, y, 5, m)
                        y3 = m * x3 + n
                        y4 = m * x4 + n
                        pygame.draw.polygon(screen, Color(61, 72, 126), [(dest_x,dest_y), (x3,y3), (x4,y4)])


                # draw nodes
                for n in self.graph.nodes.values():
                    x = my_scale(n.pos.get('x'), x=True)
                    y = my_scale(n.pos.get('y'), y=True)
                    pygame.draw.circle(screen, Color(230, 219, 230), (int(x), int(y)), radius=radius)
                    gfxdraw.aacircle(screen, int(x), int(y), radius, Color(85, 0, 85))
                    # draw the node id
                    id_srf = Font2.render(str(n.get_id()), True, Color(85, 0, 85))
                    rect = id_srf.get_rect(center=(x, y))
                    screen.blit(id_srf, rect)



            screen.fill((255, 255, 255))
            file.draw(screen)
            edit.draw(screen)
            paint(screen)
            pygame.display.flip()

            # Draws the graph - with the menu, and changes after each edit of the graph
            for event in pygame.event.get():
                if event.type == QUIT:
                    quit()
                    exit(0)
                elif event.type == MOUSEBUTTONDOWN:
                    mouse_pos = pygame.mouse.get_pos()
                    clicked, _, _ = pygame.mouse.get_pressed()
                    # If you select the file
                    if file.rect.collidepoint(mouse_pos):
                        while clicked:
                            screen.fill((255, 255, 255))
                            file.draw(screen)
                            edit.draw(screen)
                            save.draw(screen)
                            load.draw(screen)
                            paint(screen)
                            pygame.display.update()
                            for event in pygame.event.get():
                                if event.type == MOUSEBUTTONDOWN:
                                    mouse_pos = pygame.mouse.get_pos()
                                    if save.rect.collidepoint(mouse_pos):
                                        print("enter a file name to save")
                                        file_name = input()
                                        self.save_to_json(file_name)
                                    elif load.rect.collidepoint(mouse_pos):
                                        print("enter a file name to load")
                                        file_name = input()
                                        self.load_from_json(file_name)
                                    clicked = False
                    # If you select the edit
                    elif edit.rect.collidepoint(mouse_pos):
                        while clicked:
                            screen.fill((255, 255, 255))
                            file.draw(screen)
                            edit.draw(screen)
                            add_node.draw(screen)
                            remove_node.draw(screen)
                            add_edge.draw(screen)
                            remove_edge.draw(screen)
                            paint(screen)
                            pygame.display.flip()
                            pygame.display.update()
                            for event in pygame.event.get():
                                if event.type == MOUSEBUTTONDOWN:
                                    mouse_pos = pygame.mouse.get_pos()
                                    if add_node.rect.collidepoint(mouse_pos):
                                        print("enter id for the new node")
                                        id = input()
                                        print("enter pos-> x, y for the new node")
                                        pos = input()
                                        posi = pos.split(',', 2)
                                        posi.append(0)
                                        posi[0] = "35.1948"+posi[0]
                                        posi[0] = posi[0].replace(' ', '')
                                        posi[1] = "32.103"+posi[1]
                                        posi[1] = posi[1].replace(' ', '')
                                        self.get_graph().add_node(int(id), posi)
                                    elif remove_node.rect.collidepoint(mouse_pos):
                                        print("enter id node to remove")
                                        id = input()
                                        self.get_graph().remove_node(int(id))
                                    elif add_edge.rect.collidepoint(mouse_pos):
                                        print("enter id node for src edge")
                                        src = input()
                                        print("enter id node for dest edge")
                                        dest = input()
                                        print("enter edge weight")
                                        w = input()
                                        self.get_graph().add_edge(int(src), int(dest), float(w))
                                    elif remove_edge.rect.collidepoint(mouse_pos):
                                        print("enter id src of this edge")
                                        src = input()
                                        print("enter id dest of this edge")
                                        dest = input()
                                        self.get_graph().remove_edge(int(src), int(dest))
                                    clicked = False




"""

def main():
    g = GraphAlgo()
    g.load_from_json("../data/A2.json")
    g.plot_graph()





if __name__ == '__main__':
    main()
"""
