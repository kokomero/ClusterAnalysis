/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.database;

import com.clusteranalysis.datamodel.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author victor
 */
public interface ClusterResultReader {
    
    public List<Double> GetBandwidths() throws ResultReaderException ;    
    
    List<Mode> GetModes() throws ResultReaderException;
    List<Mode> GetModes(double bandwidth) throws ResultReaderException;
    public Mode GetMode(DataSample source, double bandwidth) throws ResultReaderException;
    public int NumberOfModes(double bandwidth) throws ResultReaderException;
     
    public int NumberOfSources(Mode mode) throws ResultReaderException;   
    public List<DataSample> GetSources(Mode mode) throws ResultReaderException;    
    
    public int NumberOfFeatures() throws ResultReaderException ;
    public List<String> FeatureNames()throws ResultReaderException ;
    
}
