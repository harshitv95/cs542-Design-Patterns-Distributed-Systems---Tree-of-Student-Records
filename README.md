# CS542: Assignment 3
**Name: Harshit Vadodaria**

-----------------------------------------------------------------------

Following are the commands and the instructions to run ANT on your project.


Note: build.xml is present in [studentskills/src](./studentskills/src/) folder.

## Instruction to clean:

```commandline
ant -buildfile studentskills/src/build.xml clean
```

Description: It cleans up all the .class files that were generated when you
compiled your code.

## Instructions to compile:

```commandline
ant -buildfile studentskills/src/build.xml all
```
The above command compiles your code and generates .class files inside the BUILD folder.

## Instructions to run:

```commandline
ant -buildfile studentskills/src/build.xml run -Dinput="input.txt" -Dmodify="modify.txt" -Dout1="out1.txt" -Dout2="out2.txt" -Dout3="out3.txt" -Derror="error.txt" -Ddebug=3
```
Note: Arguments accept the absolute path of the files.


## Description:
A program implementing Observer Pattern, to make the Nodes of a tree stay consistent with changes,
just like in a distributed System. It also makes use of Factory Pattern to create a StudentRecord
instance, Prototype Pattern to clone a StudentRecord, and Single Pattern to restrict the Logger
to have only one instance throughout the program.

The program would work for any Generic Students' Store (Refer interface StudentStoreI), but we use a
specialized Store to hold StudentRecords - Binary Search Tree. A simple implementation of BST is used-
StudentsBST, which has a store and search Time Complexity of O(log n), and this tree can be replaced with any other 
implementation of a Tree (AVL Tree, Red-Black Tree etc.). The Tree just has to implement the StudentTreeI interface.

The TreeHelper is used to construct any number of replicas of the Tree, and these Trees are constructed with the help
of a StudentTreeFactory provided by the Driver. The StudentTreeFactory implements a Generic interface StudentStoreI,
which can also be imeplemented to instantiate any Store (DB, Graphs, List, Map, etc.).
The TreeHelper makes use of a StudentFactory to create a new StudentRecord, and also to create clones of the existing
StudentRecord, to insert in the trees initialized by the TreeFactory. After cloning a StudentRecord, an Observer-Publisher
relation is set up among all the nodes (created either by initialization or cloning), to model the Observer Pattern. All
nodes at the same position in all trees (and thus having the same data) are observers of each other, thus the TreeHelper
registers all of these as observers of each other. Thus if a node in any tree receives an update or a modification, it
makes the changes and then notifies all the observers, which are similar nodes in other trees, and these observers will
then update themselves with these changes.

This program also makes use of a well-featured Logger, which supports the following levels (along with their Level-Numbers):

ERROR(1), WARN(2), INFO(3), CONFIG(4), DEBUG_LOW(5), DEBUG_MED(6), DEBUG_HIGH(7)

Pass the Level-Numbers as the last (7th) command line parameter (with key -Ddebug) to switch between various debug modes.
Passing 3 as Logger Level in the Command Line args will make the program print all logs having level <= 3.

The minimum level is ERROR, which is a mandatory level, since ERRORs are critical and need to be logged under any
circumstances. The ERROR and WARN logs will also be printed to an error log file, whose absolute path needs to be
provided as a command line parameter with key -Derror. This too is configurable in the call to the Logger's
constructor.
