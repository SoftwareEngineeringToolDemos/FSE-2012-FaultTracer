FaultTracer is a tool set that can help with various software testing tasks, including
regression test selection, fault localization, coverage collection and so on.

>>>>>>>>>>>>>>>>>Installation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Import the FaultTracer project into Eclipse to compile it. You need to
install:
1. Eclipse GEF SDK 3.7.2, including the Zest Visualization Toolkit (http://www.eclipse.org/gef/updates/index.php, recommended Update Site: http://download.eclipse.org/tools/gef/updates-pre-3_8/releases/)
2. Ant 1.7.0 and above (http://ant.apache.org/)

>>>>>>>>>>>>>>>>>Configuration>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Before starting FaultTracer, the user would need to provide a
configuration file, faulttracer-config.xml, stored in the base
directory of each program version under test:

<project name="FaultTracer-Configuration">
  <property name="subject" value="..."/>
  <property name="prefix" value="..."/>
  <property name="testsuite" value="..."/>
  <property name="junit4" value="..."/>
  <property name="newversion" value="..."/>
  <property name="faulttracer" value="..."/>
  <property name="cp" value="..."/>
  <property name="maxmemory" value="..."/>
  <import file="${faulttracer}\resources\faulttracer.xml"/>
</project>

To illustrate, see the fault-config-junit4.xml and fault-config-junit3.xml
for a small example project under the examples/bank-account1.0 (and 2.0)
directory.

In the config file:
1. "subject" shows the name of the version under test;

2. "prefix" shows the common package name shared by the project;

3. "testsuite" shows the fully qualified name for the test suite
class; For project without a main suite, you can simply create a
AllTests.java test suite for the whole project. To facilitate you
creating that test suite file, I now implemented a pop up menu in
FaultTracer. You can run FaultTracer as Eclipse plugin (right-click
FaultTracer and select "Run As -> Eclipse Application"). Then, in the
new workspace started by FaultTracer, you can import the project that
you want to collect coverage. Right-click the test package root (e.g.,
src/test/java), and select "GetAllTests". Then, the FaultTracer
console will print the automatically constructed test suite in both
JUnit 3 and JUnit4 formats. You can simply copy the one you want for
your project, and create a class file for it. Note that some tests
included in the test suite may not be tests. You need to manually
exclude them -- you can run that test suite from Eclipse first to
exclude all the non-test classes from the test suite. 

4. "junit4" shows whether the test suite is under JUnit4.0+ (for JUnit3
put the value to be "false");

5. "newversion" shows the absolute path of the newer version compared
with the current version (used when you are using the test selection
functionality);

6. "faulttracer" provides the absolute path of the FaultTracer project;

7. "cp" provides the class path needed for executing the program under
test; Note that to get the library classpath for project using maven
to build, please use the following command: "mvn org.apache.maven.plugins:maven-dependency-plugin:2.10:build-classpath| awk '/Dependencies classpath:/{getline; print}'". It will output all the library info. You need to put all the library info separated by
":" (linux OS) or ";" (Windows OS), together with the binary class file
directory for your source code and test code.

8. Finally, "maxmemory" provides the maximum memory allowed for executing
FaultTracer (for the concern of performance, we suggest a value of
greater than or equal to "1024m").

>>>>>>>>>>>>>>>>Regression Test Selection>>>>>>>>>>>>>>>>>>>>>

1. Extract Program Edits

FaultTracer automatically extracts atomic changes between the version
pair after the user right-clicks the two versions under test and
select "Launch FaultTracer".

Then the user can also see the visualized atomic changes in the
Atomic-Change View. All the FaultTracer views can be found in the
following Eclipse path: "Window"->"Show View"->"Other"->"FaultTracer".
When the user double click a specific change node, FaultTracer would
show change details in the Java Editor.

2. Collect ECGs

The collection of ECGs has been implemented as an Ant task, the user
can easily collect ECG coverage of program under test using the
following command-line script:

cd path-of-program-to-trace
ant -f config.xml collectECGCoverage

which navigates to the base directory of the program under test, and
then runs the Ant task collectECGCoverage. Then FaultTracer would
automatically run the test suite for the program and record its ECG
coverage for each test. The user can choose to see the visualized ECG
coverage for each test in the Extended-Call-Graph View. 

3. Select Affected Tests

The selection of affected tests has also been implemented as an Ant
task, the user can easily perform affected test selection using the
following command-line script:

cd path-of-old-version
ant -f config.xml selectAffectedTests

which will select affected tests and store affected test handles in
the new program version's FaultTracer files. Then the user can easily
only run affected tests on the new version by simply using the
following script:

cd path-of-new-version
ant -f config.xml runAffectedTests

4. Determine Affecting Changes

The user can perform affecting change determination by the following script:

cd path-of-new-version
ant -f config.xml determineAffectingChanges

After the affecting change determination, the user can view the
affecting changes for each affected tests in the Testing-Debugging
View. The left table in the view lists all the
affected tests. When the user double-clicks an affected test in the
left table, the view would display all the affecting changes for the
test in the right table. Note that for this step, the view would not
rank affecting changes. Also, the suspicious values and heuristic
values for each affecting change would all be 0.  

5. Rank Affecting Changes

The user can perform affecting change ranking by the following script:

cd path-of-new-version
ant -f config.xml rankAffectingChanges

After the affecting change ranking, the user can view the ranked
affecting changes for each affected tests in the Testing-Debugging
View. Note that for this step, the view would rank affecting changes
and display the suspicious values and heuristic values for each
affecting change.

>>>>>>>>>>>>>>>>>Collect Coverage>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

This section presents examples on how to collect various code coverage
for the project under test.

1. Collect Coverage

Go to the examples/bank-acount1.0 directory, compile the project to
generate class files in "bin/" dir (e.g., using eclipse). Then execute:

$ ant -f faulttracer-config-junit4.xml collectMethodCoverage

to collect the method coverage for the JUnit4 test suite: edu.ut.ece.bank.tests.JUnit4Driver

Or, execute:

$ ant -f faulttracer-config-junit3.xml collectMethodCoverage

to collect method coverage for the JUnit3 test suite: edit.ut.ece.bank.tests.TestDriver

Note that by replacing "collectMethodCoverage" with
"collectStatementCoverage" or "collectBranchCoverage", you can get
statement or branch coverage. All the coverage results are stored in
the "faulttracer-files" dir created under your project.

2. Load Collected Coverage

After you collect the coverage for your project, e.g.,
examples/bank-acount1.0, stay in the same dir. Then execute:

$ ant -f faulttracer-config-junit4.xml loadMethodCoverage

to load your method coverage. Or

$ ant -f faulttracer-config-junit4.xml loadStatementCoverage

to load your statement coverage. Or

$ ant -f faulttracer-config-junit4.xml loadBranchCoverage

to load your branch coverage.

Note that after executing the load commands, you can find
branch-coverage.dat/statement-coverage.dat/method-coverage.dat under
your faulttracer-files directory. For example, in the
statement-coverage.dat file, each line start with a test name, then
followed by all the statements covered by it. They are all separated
using space. To illustrate, consider the following line:

edu.ut.ece.bank.tests.JUnit4Tests.check1-false edu.ut.ece.bank.Account.withdraw:(D)D:23-1 edu.ut.ece.bank.tests.JUnit4Tests.check1:()V:21-1 edu.ut.ece.bank.Account.withdraw:(D)D:25-1 edu.ut.ece.bank.Account.withdraw:(D)D:24-1 ...

In that line, "edu.ut.ece.bank.tests.JUnit4Tests.check1-false" is the
test name, and "false" means the test failed while "true" means the
test passed. "edu.ut.ece.bank.Account.withdraw:(D)D:23-1" means the
23th line in class "edu.ut.ece.bank.Account" (the line is also inside
method "withdraw:(D)D") is executed for 1 times by the test.


For another example, in the branch-coverage.dat file, each line follow
the same pattern. However, the covered elements are different from
statement coverage, since here we consider the true/false branch, or
switch branch covered. To illustrate, consider the following line:

edu.ut.ece.bank.tests.BankTests.test0-true edu.ut.ece.bank.tests.BankTests:73:15-1 edu.ut.ece.bank.Account:23:5<true>-1 ...

In that line, "edu.ut.ece.bank.tests.BankTests.test0" is the test
name, and "true" means the test
passed. "edu.ut.ece.bank.Account:23:5<true>-1" means the true branch
of the 5th branch statement (5 is the global branch ID) at line 23 of
class "edu.ut.ece.bank.Account" is executed 1 time by the
test. "edu.ut.ece.bank.tests.BankTests:73:15-1" means the 15th (15 is
the global switch target ID) switch branch (or switch target) at line
73 of class "edu.ut.ece.bank.tests.BankTests" is covered 1 time.  Note
that for the branch coverage file, the class name and line number are
just used to help understanding. You can simply use the branchID with
<true> or <false> to identify each boolean branch, and the labelID to
identify each switch branch/target, in order to speed up analysis.

If you want to further access the coverage data through APIs, please
read the following source code files:

1. tracer.coverage.io.MethodTraceLoader
2. tracer.coverage.io.StatementTraceLoader
3. tracer.coverage.io.BranchTraceLoader

>>>>>>>>>>>>>>>>>More Info>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

If you are intested in how each Ant task is implemented, please read
"resources/faulttracer.xml"
