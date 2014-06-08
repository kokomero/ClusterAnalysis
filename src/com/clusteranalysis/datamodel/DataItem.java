/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clusteranalysis.datamodel;

import java.util.List;

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
    
    public Object[] getFeatures(List<Integer> columnIndex){
        Object[] tmp = new Object[columnIndex.size()];
        for(int i = 0; i < columnIndex.size(); i++){
            tmp[ i ] = features[ columnIndex.get(i)];
        }
        return tmp;
    }

    @Override
    public abstract String toString();
    
}
