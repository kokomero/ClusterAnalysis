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
public class DataSample extends DataItem {
    
    public DataSample(long id, Object[] features){
        super(id, features);
    }
   
    
    @Override
    public String toString(){
        String str = "Source: id: " + this.id + " Features: ";
        for (Object feature : features) {
            str += feature + ", ";
        }
        return str;
    }
    
    
}
