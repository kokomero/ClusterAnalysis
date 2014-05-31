/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.database;

/**
 *
 * @author victor
 */
import com.clusteranalysis.datamodel.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class MySQLResultsReader implements ClusterResultReader {

    private Connection conn;

    public MySQLResultsReader(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Double> GetBandwidths() throws ResultReaderException {

        String query = "SELECT value FROM Bandwidth";
        List<Double> bandwidths = new ArrayList<Double>();

        try {
            //execute query and return result as a ResultSet
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            //output list
            while (rs.next()) {
                bandwidths.add(rs.getDouble(1));
            }

        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

        return bandwidths;

    }

    private double GetBandwidth(long bandwidth_id) throws SQLException {

        //execute query and return result as a ResultSet
        String query = "SELECT value FROM Bandwidth WHERE id=" + bandwidth_id;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        rs.next();
        return rs.getDouble(1);
    }

    private long GetBandwidthId(double bandwidth) throws SQLException {

        //execute query and return result as a ResultSet
        String query = "SELECT id FROM Bandwidth WHERE value=" + bandwidth;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        rs.next();
        return rs.getInt(1);
    }

    @Override
    public long NumberOfModes(double bandwidth) throws ResultReaderException {

        try {
            //Get the bandwidth id for this bandwidth
            long bandwidth_id = GetBandwidthId(bandwidth);

            //execute query and return result as a ResultSet
            String query = "SELECT COUNT(*) FROM Mode WHERE bandwidth=" + bandwidth_id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0;
            }

        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

    }

    private Mode GetMode(long mode_id) throws SQLException, ResultReaderException {

        String query = "SELECT * FROM Mode WHERE id=" + mode_id;
        double bandwidth;
        Object[] features;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int numFeatures = NumberOfFeatures();

            rs.first();

            //Get bandwidth
            long bandwidth_id = rs.getLong(2);
            bandwidth = GetBandwidth(bandwidth_id);

            //Get features
            features = new Object[numFeatures];
            for (int i = 0; i < numFeatures; i++) {
                features[i] = rs.getDouble(i + 3);
            }

        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

        return new Mode(mode_id, bandwidth, features);


    }

    @Override
    public List<Mode> GetModes() throws ResultReaderException {

        List<Mode> modes = new ArrayList<Mode>();

        try {
            //execute query and return result as a ResultSet
            String query = "SELECT * FROM Mode";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int numFeatures = NumberOfFeatures();

            //Get the mode id, bandwidth and features            
            while (rs.next()) {
                long mode_id = rs.getLong(1);
                long bandwidth_id = rs.getLong(2);
                double bandwidth = GetBandwidth(bandwidth_id);

                Object[] features = new Object[numFeatures];
                for (int i = 0; i < numFeatures; i++) {
                    features[i] = rs.getDouble(i + 3);
                }

                modes.add(new Mode(mode_id, bandwidth, features));
            }

        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

        return modes;

    }

    @Override
    public List<Mode> GetModes(double bandwidth) throws ResultReaderException {

        List<Mode> modes = new ArrayList<Mode>();

        try {
            //Get bandwidth id
            long bandwidth_id = GetBandwidthId(bandwidth);

            //execute query and return result as a ResultSet
            String query = "SELECT * FROM Mode WHERE bandwidth=" + bandwidth_id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int numFeatures = NumberOfFeatures();

            //Get the mode id, bandwidth and features            
            while (rs.next()) {
                long mode_id = rs.getLong(1);
                Object[] features = new Object[numFeatures];
                for (int i = 0; i < numFeatures; i++) {
                    features[i] = rs.getDouble(i + 3);
                }
                modes.add(new Mode(mode_id, bandwidth, features));
            }
        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

        return modes;

    }

    @Override
    public Mode GetMode(DataSample source, double bandwidth) throws ResultReaderException {

        try {
            long mode_id = GetModeId(GetBandwidthId(bandwidth), source.getId());
            return GetMode(mode_id);
        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

    }

    @Override
    public int NumberOfFeatures() throws ResultReaderException {
        //create a fake statement
        String query = "SELECT * FROM Mode";
        int numberOfColumns;

        try {
            Statement stmt = conn.createStatement();

            //execute query and return result as a ResultSet
            ResultSet rs = stmt.executeQuery(query);

            //Get metadata
            ResultSetMetaData rsMetaData = rs.getMetaData();
            numberOfColumns = rsMetaData.getColumnCount();

        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

        return numberOfColumns - 3;
    }

    @Override
    public List<String> FeatureNames() throws ResultReaderException {

        //create a fake statement
        String query = "SELECT * FROM Mode";
        List<String> names;

        try {
            Statement stmt = conn.createStatement();

            //execute query and return result as a ResultSet
            ResultSet rs = stmt.executeQuery(query);

            //Get metadata
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int numberOfColumns = rsMetaData.getColumnCount();

            //output list
            names = new ArrayList<String>(numberOfColumns - 3);

            //First two column are id and bandwidth
            for (int i = 3; i < numberOfColumns; i++) {
                names.add(rsMetaData.getColumnName(i));
            }

        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

        return names;
    }

    private long GetModeId(long bandwidth_id, long id_source) throws SQLException {

        //execute query and return result as a ResultSet
        String query = "SELECT id_mode FROM Cluster_membership WHERE";
        query += " id_source=" + id_source + " AND bandwidth=" + bandwidth_id;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        rs.next();
        return rs.getLong(1);

    }

    /**
     * 
     * @param bandwidth_id
     * @param id_mode
     * @param attributeNames
     * @return
     * @throws SQLException 
     */
    private List<DataSample> GetSources(long bandwidth_id,
            long id_mode,
            List<String> attributeNames) throws SQLException {

        //Get the column names for the query
        String colNames;
        if (attributeNames.size() == 0) {
            colNames = "*";
        } else {
            colNames = "id";
            for (int i = 0; i < attributeNames.size(); i++) {
                colNames += ", " + attributeNames.get(i);
            }
        }

        //Subquery to get the Source Id 
        String subQuerySourceId = "( SELECT id_source FROM Cluster_membership WHERE";
        subQuerySourceId += " id_mode=" + id_mode + " AND bandwidth=" + bandwidth_id + ")";

        //Query returning the sources with the column selected
        String query = "SELECT " + colNames + " FROM Astrosources.Dataset_pleiades";
        query += " WHERE id IN " + subQuerySourceId;

        //Execute query
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        //Get number of columns
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();
        int numberFeatures = numberOfColumns - 1;

        //Create DataSamples object
        List<DataSample> sources = new ArrayList<DataSample>();
        while (rs.next()) {

            Object[] features = new Object[numberFeatures];
            long source_id = rs.getLong(1);
            for (int i = 0; i < numberFeatures; i++) {
                features[i] = rs.getObject(i + 2);
            }

            sources.add(new DataSample(source_id, features));
        }

        return sources;
    }

    @Override
    public long NumberOfSources(Mode mode) throws ResultReaderException {

        try {
            //Create count query
            long bandwidth_id = GetBandwidthId(mode.getBandwidth());
            long mode_id = mode.getId();
            String query = "SELECT COUNT(*) FROM Cluster_membership WHERE";
            query += " id_mode=" + mode_id + " AND bandwidth=" + bandwidth_id;

            //Execute query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0;
            }

        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

    }

    @Override
    public List<DataSample> GetSources(Mode mode, List<String> attributeNames) throws ResultReaderException {

        List<DataSample> sources;

        try {
            //Get the id of sources under this mode
            long bandwidth_id = GetBandwidthId(mode.getBandwidth());
            long mode_id = mode.getId();
            sources = GetSources(bandwidth_id, mode_id, attributeNames);
        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

        return sources;

    }

    @Override
    public List<Cluster> GetClusters(double bandwidth, List<String> attributeNames)  throws ResultReaderException{
        
        
        List<Mode> modes = GetModes(bandwidth);
        int numberModes = modes.size();
        List<Cluster> clusters = new ArrayList<Cluster>( modes.size() );                
        
        for(int i = 0; i < numberModes; i++){
            Mode mode = modes.get(i);
            List<DataSample> sources = GetSources(mode, attributeNames);
            //DEBUG
            System.out.println("Retrieving sources for mode: " + (i+1) + " out of " + numberModes + ". Number of sources: " + sources.size() );
            clusters.add( new Cluster(mode, sources ) );            
        }
        
        return clusters;
        
    }
    
    /**
     * For test purposes
     * 
     */
    public static void main(String[] args) throws Exception {

        String fpath = null; //Path to properties file

        //Get path to configuration file
        if (!com.jscilib.io.parameter.ParameterHandler.optionExist("-f", args)) {
            throw new Exception("Incorrect input parameters. No properties file given\n");
        } else {
            fpath = com.jscilib.io.parameter.ParameterHandler.getOption("-f", args);
        }

        //Read configuration file
        java.util.Properties properties = new java.util.Properties();
        properties.load(new java.io.FileReader(fpath));
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
        Connection connSources = java.sql.DriverManager.getConnection(dbURLSources, dbUser, dbpassword);

        //Test this class
        MySQLResultsReader resultsDB = new MySQLResultsReader(conn);

        //Get features names
        System.out.print("There are: " + resultsDB.NumberOfFeatures() + " features ");
        System.out.println(" whose names are: " + resultsDB.FeatureNames());

        List<Double> bandwidths = resultsDB.GetBandwidths();
        System.out.println("Bandwidths: " + bandwidths);

        System.out.print("Number of Modes for bandwidth=" + bandwidths.get(0));
        System.out.println(" is " + resultsDB.NumberOfModes(bandwidths.get(0)));

        List<Mode> modes = resultsDB.GetModes(bandwidths.get(0));
        System.out.println("First Mode for bandwidth=" + bandwidths.get(0) + " is: " + modes.get(0));

        System.out.println("Data samples for first mode=" + resultsDB.NumberOfSources(modes.get(0)));

        ArrayList<String> variables = new ArrayList<String>( );
        variables.add("muAcosD");
        variables.add("muD");
        System.out.println("First Data samples for first mode=" + resultsDB.GetSources(modes.get(0), variables).get(0) );

    }
}
