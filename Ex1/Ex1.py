#206398984

import csv
import json
import sys

from Call import Call
from Building import Building


def from_json(Building_json):
    try:
        with open(Building_json, "r") as file:
            b = json.load(file)
            building = Building(b["_minFloor"], b["_maxFloor"], b["_elevators"])
            return building
    except IOError as e:
        print(e)



def from_csv(Calls_csv):
    try:
        with open(Calls_csv, "r") as file:
            calls = csv.reader(file)
            mycalls = list(calls)
            for i in range(0,len(mycalls)):
                call = mycalls[i]
                mycalls[i] = Call(call[0], call[1], call[2], call[3], call[4], call[5])
            return mycalls
    except IOError as e:
        print(e)



def match(list1, building, num, list_elev, index):
    if is_empty(list1) == True: return 0
    else:
        j =0
        count=0
        while list1[j]==[]:
            j=j+1
        st=list1[j][0].direct(list1[j][0])
        if st==1:
            i=index
            j = 0
            while is_is_empty(list1)==False:
                if (i<len(list_elev)): i_elev = list_elev[i]["_id"]
                while is_empty(list1[j])==True:
                    j=j+1
                list2 = list1[j]
                count = count+ len(list2)
                list_=into(list2, i_elev)
                clear(list1[j])
                for p in range(0, len(list_)):
                    if count<=num:
                        floor = list_[p].getdest() + building.under0()
                        count = count + len(list1[floor])
                        into(list1[floor],i_elev)
                        clear(list1[floor])
                if count>=num:
                    i = i + 1
                    count=0
                j=j+1
            if (building.numOfElev()==1):
                return i
            else: return i+1
        else:
            i = index
            j = building._maxFloor+building.under0()
            while is_is_empty(list1) == False:
                if (i<len(list_elev)): i_elev = list_elev[i]["_id"]
                while is_empty(list1[j]) == True:
                    j = j - 1
                list2 = list1[j]
                count = count+len(list2)
                list_ = into(list2, i_elev)
                clear(list1[j])
                for p in range(0, len(list_)):
                    if count <= num:
                        floor = list_[p].getdest() + building.under0()
                        count = count + len(list1[floor])
                        into(list1[floor], i_elev)
                        clear(list1[floor])
                if count >= num:
                    i = i + 1
                    count=0
                j = j - 1
            if (building.numOfElev()==1):
                return i
            else: return i+1




def into (list, num_elev):
    list2=[]
    for i in range(0, len(list)):
        list[i].setelev(num_elev)
        list2.append(list[i])
    return list2




def clear(list):
    re = 0
    while list!= []:
        list.remove(list[re])




def is_is_empty(list):
    ans = True
    for i in range(0, len(list)):
        for j in range(0, len(list[i])):
            if ans == False:
               break
            else:
                if list[i]==[]:
                   ans = True
                else:
                   ans = False
    return ans




def is_empty(list):
    ans=True
    for i in range(0, len(list)):
        if ans==False: break
        else:
            if list[i]=="null":
                ans=True
            else: ans=False
    return ans




def to_csv(calls):
    try:
        with open('out.csv', 'w', newline='') as newFile:
            myWriter = csv.writer(newFile)
            for i in range(0,len(calls)):
                myWriter.writerow(calls[i])
    except IOError as e:
        print(e)




def call_elev(Building_json, Calls_csv):

    building = from_json(Building_json)
    calls = from_csv(Calls_csv)

    num_floor = building.num_floor()
    up = []
    for i in range(0, num_floor):
        up.append([])
    down = []
    for i in range(0, num_floor):
        down.append([])
    count_up=0
    count_down=0
    n=building.under0()

    for i in range(0, len(calls)):
        _call = calls[i]
        floor=_call.getsrc()+n
        if _call.direct(_call)==1:
            count_up = count_up+1
            if up[floor]=="null": up[floor]=_call
            else: up[floor].append(_call)
        else:
            count_down = count_down+1
            if down[floor]=="null": down[floor]=_call
            else: down[floor].append(_call)

    num=int(((count_down+count_up)/building.numOfElev()))
    num=num+int(num*0.2)+1

    if count_up > count_down:
        list_elev = building.speeder(count_up,count_up+count_down)
        index= match(up, building, num, list_elev,0)
        if index>building.numOfElev()-1: index=index-1
        match(down, building, num, list_elev, index)
    else:
        list_elev = building.speeder(count_down, (count_up+count_down))
        index= match(down, building, num, list_elev, 0)
        if index>building.numOfElev()-1: index = index - 1
        match(up, building, num, list_elev, index)


    mycalls= list(calls)
    for i in range(0,len(mycalls)):
       mycalls[i]=mycalls[i].tolist(mycalls[i])
    to_csv(mycalls)



