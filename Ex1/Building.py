#206398984

import copy
import json


class Building:

    # constructure
    def __init__(self, _minFloor, _maxFloor, _elevators):
        self._minFloor = _minFloor
        self._maxFloor = _maxFloor
        self._elevators = _elevators
        self._elevators2= _elevators


    def num_floor(self):
        num = self._maxFloor-self._minFloor+1
        return num


    def numOfElev(self):
        lens = len(self._elevators)
        return lens


    def under0(self):
        if self._minFloor < 0:
           return self._minFloor*(-1)
        else: 0


    def __str__(self):
        return f"_minFloor:{ self._minFloor}, _maxFloor:{ self._maxFloor}, _elevators:{ self._elevators2}"


    def speedest_elev(self, _elevators, numOfElev):
        num = self.numOfElev()
        speed=_elevators[0]["_speed"]
        speed_elev=0
        for i in range(1,num):
            if (speed<_elevators[i]["_speed"]):
                speed=_elevators[i]["_speed"]
                speed_elev=i
        return speed_elev


    def slowlest_elev(self, _elevators, numOfElev):
        num = numOfElev(_elevators)
        slow=_elevators[0]["_speed"]
        slow_elev=0
        for i in range(1,num):
            if (slow>_elevators[i]["_speed"]):
                slow=_elevators[i]["_speed"]
                slow_elev=i
        return slow_elev


    def speeder(self, num_more, num_all):
        num_elev_more = (self.numOfElev()*num_more)/(num_all)
        mod_num = num_elev_more-int(num_elev_more)
        if (mod_num>0.5):
            num_elev_more=int(num_elev_more)+1
        else:  num_elev_more=int(num_elev_more)
        list1=[]
        for i in range(0, self.numOfElev()):
            list1.append(self._elevators[i])
        more=0
        less=num_elev_more
        list2=copy.deepcopy(list1)
        for i in range(0, self.numOfElev()):
             if more<num_elev_more:
                elev_speed = self.speedest_elev(list1, self.numOfElev())
                list2[more]=list1[elev_speed]
                list1[elev_speed]["_speed"]=0
                more=more+1
             if less<self.numOfElev():
                elev_speed = self.speedest_elev(list1, self.numOfElev())
                list2[less] = list1[elev_speed]
                list1[elev_speed]["_speed"]=0
                less=less+1
        return list2
