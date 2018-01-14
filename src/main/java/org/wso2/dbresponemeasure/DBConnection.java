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
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Initiate Database connection.
 */
public class DBConnection {

    /**
     * Static object of SQL connection.
     */
    private static Connection connection = null;

    /**
     * Create database connection if closed. Else return existing connection.
     * @param url url of database.
     * @param username username of database.
     * @param password password of database.
     * @return database connection.
     */
    public static Connection getConnection(String url, String username, String password) {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.out.println("Unable to connect to database.");
                e.printStackTrace();
                return null;
            }
            return connection;
        } else {
            return connection;
        }
    }

    /**
     * Load JDBC driver.
     * @param driverLoacation location of JAR file.
     * @param jdbcConnectionClass Connection classs name.
     */
    public static void loadDBDriver(String driverLoacation, String jdbcConnectionClass) {
        File file = new File(driverLoacation);
        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            System.out.printf("Unablto open url.");
            e.printStackTrace();
        }
        URLClassLoader ucl = new URLClassLoader(new URL[] {url});
        Driver driver = null;
        try {
            driver = (Driver) Class.forName(jdbcConnectionClass, true, ucl).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            DriverManager.registerDriver(new DriverShim(driver));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
