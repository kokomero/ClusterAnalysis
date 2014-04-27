/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clusteranalysis.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author victor
 */
public class ClusterResultReaderFactory {
    

    public static ClusterResultReader GetClusterResultReader(Properties properties) throws Exception{

        ClusterResultReader reader;
        
        String db_type = properties.getProperty("database.type");
        if( db_type.equals( new String("csv") ) ){
            String samplesFile = properties.getProperty("csv.datasample_file");
            String modesFile = properties.getProperty("csv.mode_file");
            CSVResultReader csvReader = new CSVResultReader(samplesFile, modesFile);
            csvReader.ParseCSVFiles();
            reader = csvReader;
        }
        else if( db_type.equals( new String("jdbc") ) ){
            String dbDriver = properties.getProperty("jdbc.driver");
            String dbUser = properties.getProperty("jdbc.user");
            String dbpassword = properties.getProperty("jdbc.password");
            String dbURL = properties.getProperty("jdbc.url.results");
            String dbURLSources = properties.getProperty("jdbc.url.sources");
            System.out.println("URL Results: " + dbUser + "@" + dbURL);
            System.out.println("URL Sources: " + dbUser + "@" + dbURLSources);

            //Get the Driver and the connection
            Class.forName(dbDriver);
            Connection conn = java.sql.DriverManager.getConnection(dbURL, dbUser, dbpassword);
            reader = new MySQLResultsReader(conn);
        }
        else{
            throw new Exception("Unkown database type");
        }
        
        return reader;
    }
    
}
