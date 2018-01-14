/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.dbresponemeasure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class initiate DB conection and mesure the time.
 */
public class DbReasponceMeasure {

    /**
     * Main class of the programme.
     * @param args List of command line arguments.
     */
    public static void main(String[] args) {
        ReadConfigFile configs = new ReadConfigFile();
        String url = configs.getProperty("CONNECTION.URL");
        String username = configs.getProperty("CONNECTION.USERNAME");
        String password = configs.getProperty("CONNECTION.PASSWORD");
        String driverClass = configs.getProperty("CONNECTION.DRIVERCLASS");
        String driverLocation = configs.getProperty("CONNECTION.JDBCDRIVER");
        String querytorun = configs.getProperty("SQL.QUERYTOEXECUTE");
        int threadSllepTime = Constents.DEFAULT_THREAD_SLEEP_TIME;
        int iternations = Constents.DEFAULT_ITERATION_COUNT;
        if (configs.getProperty("RUN.THREADSLEEPTIME") != null) {
            threadSllepTime = Integer.parseInt(configs.getProperty("RUN.THREADSLEEPTIME"));
        }
        DBConnection.loadDBDriver(driverLocation, driverClass);
        Connection connection = DBConnection.getConnection(url, username, password);
        if (connection != null) {
            System.out.println("-----------------------------------------------------------------------------------------------------------------");
            System.out.println("                              Connected to database and query execution started                                  ");
            System.out.println("-----------------------------------------------------------------------------------------------------------------\n");
        }
        Statement statement = null;
        ResultSet tempResultset;
        long startTime = 0;
        long compleatedTime = 0;
        long totalTime = 0;
        if (querytorun == null) {
            querytorun = Constents.DEFAULT_QUERY_TO_EXECUTE;
        }
        if (configs.getProperty("SQL.ITERATIONS") != null) {
            iternations = Integer.parseInt(configs.getProperty("SQL.ITERATIONS"));
        }
        for (int index = 0; index < iternations; index++) {
            try {
                startTime = System.currentTimeMillis();
                statement = connection.createStatement();
                tempResultset = statement.executeQuery(querytorun);
                if (tempResultset.next()) {
                    System.out.println("Query Executed");
                }
                statement.close();
                compleatedTime = System.currentTimeMillis();
                totalTime += (compleatedTime - startTime);
                System.out.println("Time taken to execute query : " + (compleatedTime - startTime) + "ms.");
            } catch (SQLException e) {
                System.out.println("Unable to execute query.");
                e.printStackTrace();
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            try {
                Thread.sleep(threadSllepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        System.out.println("\n-----------------------------------------------------------------------------------------------------------------");
        System.out.println("                       Avarage time taken to execute query : " + (totalTime * 1.0 / iternations) + "ms.");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
