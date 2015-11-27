FaultTracer

Authors:
	Lingming Zhang, Miryung Kim, Sarfraz Khurshid
Tool Page:
	http://www.utdallas.edu/~lxz144130/ftracer.html

VM created and configured by Sajal Garg (sgarg5@ncsu.edu)

INTRODUCTION
============
This file lists the steps to run the Fault Tracer tool (an Eclipse plugin) for analysing the change impact and regression fault for evolving Java programs. It takes the old and new version of the program and a regression test suites as inputs and then identifies affected tests. 
The plugin and its dependencies are pre-installed in the Eclipse directories and ready to be used. Two sample projects (bank account 1.0 and bank account 2.0) are used to demostrate the tool's capabilities.

INSTRUCTIONS
============

[Step 1 (optional)] Start Eclipse

The VM has been configured such that Eclipse should start automatically when you log in to this machine as 'faulttracer-user' user, so you may ignore this step, otherwise you can start eclipse from terminal using "eclipse/jee-mars/eclipse/eclipse"

---

[Step 2 (optional)] Create/import the projects to compare

Once you see the Eclipse UI,  you should see (on the left) the example projects (bank account 1 and bank account 2) already present in the workspace.
However, if you like to create or import your own Java projects to compare, you may import the project using the File->Import, or create the new project using File->New->Java Project, respectively.

---

[Step 3] Run the Fault Tracer on example projects.

First make sure that the eclipse perspective is set to Java and 'Atomic-Change View', 'Extended-Call-Graph View', 'Testing-Debugging View' are closed.
From the eclipse left pane, select both of the projects to compare and select the "Launch Fault Tracer" option in the right click menu. The Fault Tracer will execute silently.

---

[Step 4] Enable the plugin views

Go to Window->Show View->Other menu. In the 'Show View' window that appears, expand the 'FaultTracer' category and select 'Atomic-Change View', 'Extended-Call-Graph View', 'Testing-Debugging View' and click OK. This should now have enabled three tabbed views.

---

[Step 5] Understanding the Views

Atomic-Change View:
	Atomic-Change View illustrates the atomic change between two versions of a rea world program. The user can search a specific 	graph node, apply and unapply a specific change.
Extended-Call-Graph View:
	TheFault Tracer would automatically run the test suite for the program and record its ECG coverage. The user can visualize 	the ECG coverage for each test in the Extended-Call-Graph View.
Testing-Debugging View:
	The user can view the affecting changes for each affected test in the Testing-Debugging View. After clicking the affected test in the left table, the user can view all the affecting changes for the test in the right table.

---
