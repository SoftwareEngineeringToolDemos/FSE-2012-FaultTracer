>>>>>>>>>>>>>>>>>Installation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Import the FaultTracer project into Eclipse to compile it. You need to
install:
1. Eclipse GEF Zest Visualization Toolkit 1.3.0 and above (http://www.eclipse.org/gef/updates/index.php)
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

To illustrate, see the fault-config.xml and fault-config-junit3.xml
for a small example project under the examples/bank-account1.0
directory.

In the config file:
1. "subject" shows the name of the version under test;
2. "prefix" shows the common package name shared by the project;
3. "testsuite" shows the fully qualified name for the test suite class;
4. "junit4" shows whether the test suite is under JUnit4.0+ (for JUnit3
put the value to be "false")
5. "newversion" shows the absolute path of the newer version compared
with the current version (do not use it if you are not using the test
selection functionality);
6. "faulttracer" provides the absolute path of the FaultTracer project;
7. "cp" provides the class path needed for executing the program under
test;
8. Finally, "maxmemory" provides the maximum memory allowed for executing
FaultTracer (for the concern of performance, we suggest a value of
greater than or equal to "1024m").

Note that if you are using Windows, please check the path format into
Windows-based format.

>>>>>>>>>>>>>>>>>Collect Coverage>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Go to the examples/bank-acount1.0 directory, compile the project to
generate class files in "bin/" dir (e.g., using eclipse). Then execute:

$ ant -f faulttracer-config.xml collectMethodCoverage

to collect the method coverage for the JUnit4 test suite: edu.ut.ece.bank.tests.JUnit4Driver

Or, execute:

$ ant -f faulttracer-config-junit3.xml collectMethodCoverage

to collect method coverage for the JUnit3 test suite: edit.ut.ece.bank.tests.TestDriver

Note that by replacing "collectMethodCoverage" with
"collectStatementCoverage", you can get statement coverage. All the
coverage results are stored in the "faulttracer-files" dir created
under your project.

>>>>>>>>>>>>>>>>>Load Coverage>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

After you collect the coverage for your project, e.g.,
examples/bank-acount1.0, stay in the same dir. Then execute:

$ ant -f faulttracer-config.xml loadMethodCoverage

to load your method coverage. Or

$ ant -f faulttracer-config.xml loadStatementCoverage

to load your statement coverage.

If you want to further access the coverage data through APIs, please
read the following source code files:

1. tracer.coverage.io.MethodTraceLoader

2. tracer.coverage.io.StatementTraceLoader

>>>>>>>>>>>>>>>>>More Info>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

If you are intested in how each Ant task is implemented, please read
"resources/faulttracer.xml"
