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
    private Connection connSources;

    public MySQLResultsReader(Connection conn, Connection connSources) {
        this.conn = conn;
        this.connSources = connSources;
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
    public int NumberOfModes(double bandwidth) throws ResultReaderException {

        try {
            //Get the bandwidth id for this bandwidth
            long bandwidth_id = GetBandwidthId(bandwidth);

            //execute query and return result as a ResultSet
            String query = "SELECT COUNT(*) FROM Mode WHERE bandwidth=" + bandwidth_id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return rs.getInt(1);
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
        double[] features;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int numFeatures = NumberOfFeatures();

            rs.first();

            //Get bandwidth
            long bandwidth_id = rs.getLong(2);
            bandwidth = GetBandwidth(bandwidth_id);

            //Get features
            features = new double[numFeatures];
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

                double[] features = new double[numFeatures];
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
                double[] features = new double[numFeatures];
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

    private List<Long> GetSources(long bandwidth_id, long id_mode) throws SQLException {

        //execute query and return result as a ResultSet
        String query = "SELECT id_source FROM Cluster_membership WHERE";
        query += " id_mode=" + id_mode + " AND bandwidth=" + bandwidth_id;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        //output list
        List<Long> sources = new ArrayList<Long>();
        while (rs.next()) {
            sources.add(rs.getLong(1));
        }
        return sources;
    }

    private DataSample GetSource(long source_id) throws SQLException {

        //Get row for this data sample
        String query = "SELECT * FROM Dataset_pleiades WHERE id=" + source_id;
        Statement stmt = connSources.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.first();

        //Get number of columns
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();
        int numberFeatures = numberOfColumns - 1;

        //Extract features
        Object[] features = new Object[numberFeatures];
        for (int i = 1; i < numberFeatures; i++) {
            features[i] = rs.getObject(i + 1);
        }

        //TODO: Some fields should be mapped to string after conversion of keys
        return new DataSample(source_id, features);
    }

    @Override
    public int NumberOfSources(Mode mode) throws ResultReaderException {

        try {
            //Get the id of sources under this mode
            List<Long> sources_id = GetSources(GetBandwidthId(mode.getBandwidth()),
                    mode.getId());
            return sources_id.size();
        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }

    }

    @Override
    public List<DataSample> GetSources(Mode mode) throws ResultReaderException {

        List<DataSample> sources;

        try {
            //Get the id of sources under this mode
            List<Long> sources_id = GetSources(GetBandwidthId(mode.getBandwidth()),
                    mode.getId());

            //Construct source objects


            sources = new ArrayList<DataSample>();
            for (int i = 0; i < sources_id.size(); i++) {
                sources.add(GetSource(sources_id.get(i)));
            }


        } catch (SQLException e) {
            throw new ResultReaderException(e.getMessage(), e.getCause());
        }


        return sources;

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
        MySQLResultsReader resultsDB = new MySQLResultsReader(conn, connSources);

        //Get features names
        System.out.print("There are: " + resultsDB.NumberOfFeatures() + " features ");
        System.out.println(" whose names are: " + resultsDB.FeatureNames());

        List<Double> bandwidths = resultsDB.GetBandwidths();
        System.out.println("Bandwidths: " + bandwidths);

        System.out.print("Number of Modes for bandwidth=" + bandwidths.get(0));
        System.out.println(" is " + resultsDB.NumberOfModes(bandwidths.get(0)));

        List<Mode> modes = resultsDB.GetModes(bandwidths.get(0));
        System.out.println("First Mode for bandwidth=" + bandwidths.get(0) + " : " + modes.get(0));

        System.out.println("Data samples for first mode=" + resultsDB.NumberOfSources(modes.get(0)));
        System.out.println("First Data samples for first mode=" + resultsDB.GetSources(modes.get(0)).get(0) );

    }
}
