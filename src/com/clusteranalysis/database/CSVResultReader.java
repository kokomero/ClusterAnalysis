/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clusteranalysis.database;

import com.clusteranalysis.datamodel.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author victor
 */
public class CSVResultReader implements ClusterResultReader {
    
    private final File sampleFile;
    private final File modeFile;
    
    private List<String> modeHeaders;
    private List<String> dataSampleHeaders;
    
    private HashMap<Long, Mode> modes;
    private HashMap<Long, Cluster> clusters;
    
    public CSVResultReader(String sampleFileName, String modeFileName) {
        this.sampleFile = new File(sampleFileName);
        this.modeFile = new File(modeFileName);
        
        modes = new HashMap<Long, Mode>();
        clusters = new HashMap<Long, Cluster>();
    }
    
    public void ParseCSVFiles() throws FileNotFoundException, IOException{
            
        String separator = ",";
        
        //Open Readers
        BufferedReader readerModes = new BufferedReader(new FileReader(modeFile));
        BufferedReader readerSamples = new BufferedReader(new FileReader(sampleFile));
        
        //Read headers from both files
        String line = readerModes.readLine();
        modeHeaders = Arrays.asList( line.split( separator ) );
        line = readerSamples.readLine();
        dataSampleHeaders = Arrays.asList( line.split( separator ) );
        
        //Read modes        
        while ((line = readerModes.readLine()) != null) {             
                String[] modeLine = line.split(separator);
                long id = Long.parseLong(modeLine[0]);
                double bw = Double.parseDouble(modeLine[1]);
                double features[] = new double[modeHeaders.size() - 2];
                for(int i = 2; i < modeHeaders.size(); i++){
                    features[i-2] = Double.parseDouble(modeLine[i]);
                }
                Mode mode = new Mode(id, bw, features);
                modes.put(id, mode);
                clusters.put(id, new Cluster(mode) );
        }
        
        //Read data samples
        while ((line = readerSamples.readLine()) != null) {             
                String[] sampleLine = line.split(separator);
                long sampleId = Long.parseLong(sampleLine[0]);
                long modeId = Long.parseLong(sampleLine[1]);
                Object features[] = new Object[dataSampleHeaders.size() - 1];
                for(int i = 2; i < modeHeaders.size(); i++){
                    features[i-2] = Double.parseDouble(sampleLine[i]);
                }            
                DataSample sample = new DataSample( sampleId, features);
                clusters.get( modeId ).AddSample( sample );                        
        }
        
        readerModes.close();
        readerSamples.close();
        
    }
    
    
    public List<Double> GetBandwidths() throws ResultReaderException{
        
        //Get a list of mode keys
        Set<Long> modesId = modes.keySet();
        Set<Double> bandwidths = new TreeSet<Double>();
        
        //Iterate over all modes getting bandwidths
        Iterator<Long> iterator = modesId.iterator();
        while( iterator.hasNext() ){
            bandwidths.add( modes.get( iterator.next() ).getBandwidth() );
        }
        
        //Convert set of bandwiths into a list
        return new ArrayList<Double>(bandwidths);      
        
    }
    
    @Override
    public List<Mode> GetModes() throws ResultReaderException{
        
        //Get a list of mode keys
        Set<Long> modesId = modes.keySet();
        ArrayList<Mode> modeList = new ArrayList<Mode>();
        
        //Iterate over all entries in the modes map
        Iterator<Long> iterator = modesId.iterator();
        while( iterator.hasNext() ){
            modeList.add( modes.get( iterator.next() ) );
        }     
        
        return modeList;
        
    }
    
    @Override
    public List<Mode> GetModes(double bandwidth) throws ResultReaderException{
        
         //Get a list of mode keys
        Set<Long> modesId = modes.keySet();
        ArrayList<Mode> modeList = new ArrayList<Mode>();
        
        //Iterate over all entries in the modes map
        Iterator<Long> iterator = modesId.iterator();
        while( iterator.hasNext() ){
            Mode mode = modes.get( iterator.next() );
            if( mode.getBandwidth() == bandwidth ){                    
                modeList.add( mode );      
            }
        }     
        
        return modeList;        
    }
    
    @Override
    public Mode GetMode(DataSample source, double bandwidth) throws ResultReaderException{

        //Modes for this bandwidth
        List<Mode> modes_bw = GetModes(bandwidth);
        Mode mode = null;
        
        //Return the mode within the cluster containing the data point
        for (Mode modes_bw1 : modes_bw) {
            Cluster cluster = clusters.get(modes_bw1.getId());
            if (cluster.ContainsData( source )) {
                mode = modes_bw1;
                break;
            }
        }
        
        return mode;
    }
    
    @Override
    public long NumberOfModes(double bandwidth) throws ResultReaderException{
        return GetModes(bandwidth).size();        
    }
     
    @Override
    public long NumberOfSources(Mode mode) throws ResultReaderException{
        return clusters.get( mode.getId() ).NumberOfData();
    }
    
    @Override
    public List<DataSample> GetSources(Mode mode, List<String> attributeNames) throws ResultReaderException{
        long modeId = mode.getId();
        return clusters.get( modeId ).GetSamples();
    }
    
    @Override
    public int NumberOfFeatures() throws ResultReaderException{
        return modeHeaders.size() - 2;
    }
    
    @Override
    public List<String> FeatureNames()throws ResultReaderException{
        return dataSampleHeaders.subList(2, dataSampleHeaders.size());
    }
    
    @Override
    public List<Cluster> GetClusters(double bandwidth, List<String> attributeNames)  throws ResultReaderException{
        
        List<Mode> modes_bw = GetModes(bandwidth);
        ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
      
        for (Mode modes_bw1 : modes_bw) {
            clusterList.add( clusters.get(modes_bw1.getId()) );
        }
        
        return clusterList;        
    }
    
    
}

