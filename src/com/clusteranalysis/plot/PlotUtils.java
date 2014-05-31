/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.plot;

import java.util.List;
import java.util.ArrayList;
import com.clusteranalysis.datamodel.*;

/**
 *
 * @author victor
 */

public class PlotUtils {

    public static List<Mode> GetModes(List<Cluster> clusters){
            
        int numClusters = clusters.size();
        List<Mode> modes = new ArrayList<Mode>(numClusters);
        
        for(int i = 0; i < numClusters; i++){
            modes.add( clusters.get(i).GetMode() );                        
        }
        
        return modes;        
    }
    
    public static double[][] GetModesDataSeries(List<Cluster> clusters) {
        int dataLength = clusters.size();
        double[][] series = new double[2][dataLength];
        for (int i = 0; i < dataLength; i++) {
            Mode row = clusters.get(i).GetMode();
            series[0][i] = (Double)row.getFeatures()[0];
            series[1][i] = (Double)row.getFeatures()[1];
        }
        return series;
    }
   
    public static double[][] GetSourcesDataSeries(List<DataSample> sources) {
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
