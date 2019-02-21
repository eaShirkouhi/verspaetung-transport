
<h2>Problem</h2>

In the fictional city of Verspaetung, public transport is notoriously unreliable. To tackle the problem, the city council has decided to make the public transport timetable and delay information public, opening up opportunities for innovative use cases.
You are given the task of writing a web API to expose the Verspaetung public transport information.
As a side note, the city of Verspaetung has been built on a strict grid - all location information can be assumed to be from a cartesian coordinate system.

<h2>Data</h2>

The Verspaetung public transport information is comprised of 4 CSV files:

lines.csv - the public transport lines.

stops.csv - the stops along each line.

times.csv - the time vehicles arrive & depart at each stop. The timetimestamp is in the format of HH:MM:SS.

delays.csv - the delays for each line. This data is static and assumed to be valid for any time of day.



<h2>Challenge</h2>


Build a web API which provides the following features:

Find a vehicle for a given time and X & Y coordinates

Return the vehicle arriving next at a given stop 

Indicate if a given line is currently delayed


Endpoints should be available via port 8081




<h2>Technologies</h2>


Java

Spring Boot

Spring Web MVC

RESTFull Webservices using JSON request/response

MockMVC Testing



<h2>How To Run</h2>

./gradlew bootRun



<h2>Endpoints</h2>

Listed in Routes-Class

<b>Indicate if a given line is currently delayed</b>
http://localhost:8081/vehicles/M4/10:00:00/delayed   


<b>Return the vehicle arriving next at a given stop</b>
http://localhost:8081/vehicles/0/10:00:00   


<b>Find a vehicle for a given time and X & Y coordinates</b>
http://localhost:8081/vehicles/2/9/10:08:00      

