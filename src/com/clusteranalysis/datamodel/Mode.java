/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.datamodel;

/**
 *
 * @author victor
 */
public class Mode{
    
    protected long id;
    protected double bandwidth;
    protected double[] features;
    
    public Mode(long id, double bw, double[] features){
        this.id = id;
        this.features = features;
        this.bandwidth = bw;        
    }
    
    public double getBandwidth(){        
        return this.bandwidth;
    }
    
    public long getId(){
        return this.id;
    }
    
    public double[] getFeatures(){
        return this.features;
    }
    
    public String toString(){
        String str = "Mode: id: " + id + " Bandwidth: " + bandwidth + " Features: ";
        for(int i = 0; i < features.length; i++){
            str += features[i] + ", ";
        }
        return str;
    }
    
}
