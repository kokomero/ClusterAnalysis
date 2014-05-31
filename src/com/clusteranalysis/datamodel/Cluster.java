/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.datamodel;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author victor
 */
public class Cluster {
    
    private Mode mode;
    private List<DataSample> samples;    
  
    public Cluster(Mode mode){
        this.mode = mode;
        this.samples = new ArrayList<DataSample>();
    }
    
    public Cluster(Mode mode, List<DataSample> samples){
        this.mode = mode;
        this.samples = samples;
    }    
    
    public Mode GetMode(){
        return this.mode;
    }
    
    public List<DataSample> GetSamples(){
        return this.samples;
    }
    
    public DataSample GetSample(int index){
        return this.samples.get(index);
    }
    
    public void AddSample(DataSample sample){
        this.samples.add(sample);
    }
    
    public int NumberOfData(){
        return this.samples.size();
    }
    
    public boolean ContainsData(DataSample sample){
        return samples.contains( sample );
    }
}
