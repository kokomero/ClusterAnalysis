/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.plot;

import java.util.List;
import java.awt.Color;
import java.awt.Shape;

import com.clusteranalysis.datamodel.*;

/**
 *
 * @author victor
 */
public class ClusterDataSerie extends DataSerie {
    
    final private Cluster cluster;
    
    public ClusterDataSerie(Cluster cluster, String name, Color color, Shape shape){
        super(name, color, shape);
        this.cluster = cluster;        
    }
    
    public ClusterDataSerie(Cluster cluster, String name, Shape shape){        
        super(name, shape);
        this.cluster = cluster;         
    }
    
    public Cluster GetCluster(){
        return cluster;
    }    
    
    @Override
    public DataItem GetDataItem(int index){
        return cluster.GetSample(index);
    }
       
    public double[][] GetDataSerie(){
        
        List<DataSample> sources = cluster.GetSamples();
        
        int dataLength = sources.size();
        double[][] series = new double[2][dataLength];
        for (int i = 0; i < dataLength; i++) {
            DataSample row = sources.get(i);
            series[0][i] = (Double) row.getFeatures()[0];
            series[1][i] = (Double) row.getFeatures()[1];
        }
        return series;
    }
    
    
}
