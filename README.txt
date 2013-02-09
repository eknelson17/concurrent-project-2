concurrent-project-2
====================

Maddison Hickson, Dan Lavoie, and Emma Nelson

To compile the program, open a Terminal window or other shell with Scala installed and type "scalac *.scala".
To run the program, type "TSA" and the simulation will execute on its own without any user input required.
The program can also be run by run.bat on Windows. 
The batch file is configured to run on the lab machines for the course, so you may have to edit the CLASSPATH variables 
to work for your set up. 

Please note that the script included was provided by the professor, so any errors are not of our making.

To change the settings for the simulation, open TSA.scala. 
Right under the object definition there are two global variables: NLINES and LENGTH. 
NLINES is the number of lines in the simulation. Changing this changes the level of concurrency in the program. 
LENGTH is the amount of time the program will run. The default setting is to run for 1 second. 
Passengers are randomly generated at any given time by the TSA object; 
	however, the Controller decides how many the generate at that time.