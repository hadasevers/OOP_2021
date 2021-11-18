Ex1

My algorithm consists of several steps:

In the first stage - it is based on sorting the calls for calls up and for calls down.
    The sorting will be into two lists, with each list the size of the number of floors that the building it has receives as input.
    Each cell in the list will contain the calls from the same source floor, with cell 0 in the list being defined the minimum floor of the building.
    When at the time of sorting the calls to the lists, there will be two counters that will count the number of calls up and the number of calls down.

In the second stage, the elevators will be sorted into a new list, according to their speeds.
    When the sorting will be proportional to the amount of calls in the same direction. That is, the list will not be sorted by the speed of the elevators but relatively.
    If, for example, calls upwards constitute 2/3 of the calls, then the fastest elevator will receive the list in the upward direction,
    and the second fastest elevator will receive the list in the downward direction. And the third fastest elevator - again upwards and so on.
    That is, the list The list of elevators will assign elevators upwards and in the additional third - elevators downwards.
    All this is done within the "building" department. That is, internal action.
    Also, consider what is the average number of calls each elevator should receive. Of course each elevator is given a larger allocation,
    for anomalies, and because the numbers are not completely complete.

Finally we will insert the elevators for calls.
    We will start with a direction for which there are more calls. He will start his placement with the elevator at the top of the list. That is the fastest elevator.
    The placement will be done by the "threading" method, that is, at the beginning the elevator will deputative go to the minimum floor (if it is towards the top, and the maximum floor if it is down)
    for which there are calls in the list. That is, the list of calls from that source floor is not empty.
    The elevator will pick up everyone who is on the same floor, and will only go to destination floors of the same calls. That is - you will not pick up people on the way.
    But- of course we will probably not yet fill in the average number of calls for that elevator, and therefore, the next calls that will be embedded in the elevator will be the destinations of the people in the elevator.
    That is, if three people entered the elevator on the 2nd floor, and their destination is on the 1st, 3rd, and 7th floors.
    So that the elevator will stop only for those people in the elevator and will not add stops along the way for other calls.
    If the elevator has not yet reached the required number of calls - it will repeat the operation - that is, it will go to the minimum floor, etc.
    If the elevator is full - then we will move on to the next elevator, and so on until the list of the same direction is completed.
    Of course, because of the calculation made, the elevator that will start the list of the other direction will be the second fastest elevator.

Choosing the fastest elevator for the first elevator to be filled in a certain direction is intentional.
    Because the elevator that begins to fill at the end of the building has the potential for extensive traffic - between the minimum floor and the maximum floor of the building.
    In addition, as the elevator fills up, some floors are emptied of calls, so it turns out that the first elevator that fills up for any direction will have more calls than average, and therefore, it is better to be the fastest.


In implementing the algorithm, I worked with two classes.
Building, and readings. For them I have created methods and tools that will help me write the algorithm in the simplest way.
In the third file I wrote the algorithm itself, which uses and is aided by the actions of the departments.


Links to sources of inspiration for my algorithm:
https://idogreenberg.neocities.org/linked%20files/Articles/Elevators%20weighting%20time%20optimization.pdf
https://www.youtube.com/watch?v=JXqVvmBOyyQ
https://www.youtube.com/watch?v=5QqMM6esz2M
https://www.youtube.com/watch?v=TDww3MjL-0A

