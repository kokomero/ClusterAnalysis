/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.datamodel;

import java.util.List;
/**
 *
 * @author victor
 */
public class Mode extends DataItem{
        
    protected double bandwidth;
    
    public Mode(long id, double bw, Object[] features){
        super(id, features);
        this.bandwidth = bw;        
    }
    
    public double getBandwidth(){        
        return this.bandwidth;
    }
   
    @Override
    public String toString(){
        String str = "Mode: id: " + id + " Bandwidth: " + bandwidth + " Features: ";
        for(int i = 0; i < features.length; i++){
            str += features[i] + ", ";
        }
        return str;
    }
    

     
}
