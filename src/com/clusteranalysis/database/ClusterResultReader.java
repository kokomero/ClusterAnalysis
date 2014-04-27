/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.database;

import com.clusteranalysis.datamodel.*;
import java.util.List;

/**
 *
 * @author victor
 */
public interface ClusterResultReader {
    
    public List<Double> GetBandwidths() throws ResultReaderException ;    
    
    public List<Mode> GetModes() throws ResultReaderException;
    public List<Mode> GetModes(double bandwidth) throws ResultReaderException;
    public Mode GetMode(DataSample source, double bandwidth) throws ResultReaderException;
    public long NumberOfModes(double bandwidth) throws ResultReaderException;
     
    public long NumberOfSources(Mode mode) throws ResultReaderException;   
    public List<DataSample> GetSources(Mode mode, List<String> attributeNames) throws ResultReaderException;    
    
    public int NumberOfFeatures() throws ResultReaderException ;
    public List<String> FeatureNames()throws ResultReaderException ;
    
    public List<Cluster> GetClusters(double bandwidth, List<String> attributeNames)  throws ResultReaderException;
    
    
}
