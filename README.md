Introduction
--------------
Purpose of this DBClient is to isolate whether DB becoming a performance bottleneck in production deployment.
(It's not possible to enable JDBC spy logs in production systems, since it need some config file changes and server
restarts)

What this client do is, execute a given SQL query for given number of iterations, and log the query execution time
taken in each iteration.

This client will help to identify scenarios such as;
- Database itself has some tuning/network issues. eg: You see high fluctuation in the query execution times
- Database query execution time is too high. eg: Number of records in the Db has grown. Not having proper indexes etc.

Note:
You need to have idea on the issue that you are going to isolate and define the SQL query as relevant for that.
eg: Token generation, Registry, JDBC user store issues need to pick the query as relevant for each.


How to build ?
---------------
1. Checkout the source and execute following command from the project home folder (database-response-time-measure)
mvn clean install

2. Build jar will available in project target directory, once build is success.


How to execute the client ?
----------------------------
1. Copy the built jar file "db.response.time.measure-1.0.jar" and "config.properties" file into a same directory
location.

2. Update the config.properties file with correct db details.

3. Execute following command to run the client and get query execution times.
java -jar db.response.time.measure-1.0.jar  

4. On a successful execution of client, you will see result similar to following.
-----------------------------------------------------------------------------------------------------------------
                              Connected to database and query execution started
-----------------------------------------------------------------------------------------------------------------

Query Executed<br />
Time taken to execute query : 38ms.<br />
Query Executed<br />
Time taken to execute query : 98ms.<br />
Query Executed<br />
Time taken to execute query : 98ms.<br />
Query Executed<br />
Time taken to execute query : 42ms.<br />
Query Executed<br />
Time taken to execute query : 38ms.<br />

-----------------------------------------------------------------------------------------------------------------
                       Avarage time taken to execute query : 62.8ms.
-----------------------------------------------------------------------------------------------------------------
