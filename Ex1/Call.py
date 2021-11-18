#206398984
import csv


class Call:


    def __init__(self, _call, _time, _src, _dest, _state, _elev):
        self._call = _call
        self._time = _time
        self._src = _src
        self._dest = _dest
        self._state = _state
        self._elev = _elev


    def getsrc(self):
        return int(self._src)


    def getdest(self):
        return int(self._dest)


    def setelev(self, number):
        self._elev=number


    def printcall(self):
        print(self._call, self._time, self._src, self._dest, self._state, self._elev)


    def direct(self, call):
        if (call.getsrc() < call.getdest()): return 1
        else:
            if(call.getdest() < call.getsrc()): return -1


    def tolist(self, call):
        return [self._call,self._time,self._src,self._dest,self._state,self._elev]