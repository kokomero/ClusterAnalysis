/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clusteranalysis.datamodel;

/**
 *
 * @author victor
 */
public abstract class DataItem {
    
    protected long id;
    protected Object[] features;
    
    public DataItem(long id, Object[] features){
        this.id = id;
        this.features = features;
    }
    
    public long getId(){
        return this.id;
    }
    
    public Object[] getFeatures(){
        return this.features;
    }    
    
    @Override
    public abstract String toString();
    
}
