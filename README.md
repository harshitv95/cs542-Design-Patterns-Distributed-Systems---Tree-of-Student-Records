# CSX43: Assignment 3
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
just like in a distributed System.

The program would work for any Generic StudentsStore (Refer interface StudentStoreI), but we use a
specialized Store to hold StudentRecords - Binary Search Tree. A simple implementation of BST is used-
StudentsBST, which has a store and search Time Complexity of O(log n), and this tree can be replaced with any other 
implementation of a Tree. The Tree just has to implement the StudentTreeI interface.

The TreeHelper is used to construct any number of replicas of the Tree, and these Trees are constructed with the help
of a StudentTreeFactory provided by the Driver. The StudentTreeFactory implements a Generic interface StudentStoreI,
which can also be imeplemented to instantiate any Store (DB, Graphs, List, Map, etc.).
The TreeHelper makes use of a StudentFactory to create a new StudentRecord, and clone an existing StudentRecord, to
insert in the trees initialized by the TreeFactory.

This program also makes use of a well-featured Logger, which supports the following levels (along with their Level-Numbers):

ERROR(1), WARN(2), INFO(3), CONFIG(4), DEBUG_LOW(5), DEBUG_MED(6), DEBUG_HIGH(7),;

Pass the Level-Numbers as the last (6th) command line parameter to switch between various debug modes.

## Academic Honesty statement:

"I have done this assignment completely on my own. I have not copied
it, nor have I given my solution to anyone else. I understand that if
I am involved in plagiarism or cheating an official form will be
submitted to the Academic Honesty Committee of the Watson School to
determine the action that needs to be taken. "

Date: [ADD_DATE_HERE]

