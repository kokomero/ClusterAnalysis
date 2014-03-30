/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.datamodel;

import java.util.Arrays;

/**
 *
 * @author victor
 */
public class DataSample {
    
    protected long id;
    protected Object[] features;
    
    public DataSample(long id, Object[] features){
        this.id = id;
        this.features = features;
    }
    
    public Object[] getFeatures(){
        return this.features;
    }
    
    public long getId(){
        return this.id;
    }
    
    public String toString(){
        String str = "Source: id: " + this.id + " Features: ";
        for(int i = 0; i < features.length; i++){
            str += features[i] + ", ";
        }
        return str;
    }
    
    
}
