/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.plot;


import org.jfree.data.xy.DefaultXYDataset;

/**
 *
 * @author victor
 */
public abstract class DataSet extends DefaultXYDataset  {
    
    public DataSet(){
        
    }
    
    abstract void addSeries(Comparable seriesKey, DataSerie serie);
}
