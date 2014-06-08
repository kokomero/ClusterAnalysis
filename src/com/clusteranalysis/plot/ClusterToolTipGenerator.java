/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clusteranalysis.plot;


import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;

import com.clusteranalysis.datamodel.DataItem;
import java.util.List;

/**
 *
 * @author victor
 */
public class ClusterToolTipGenerator implements XYToolTipGenerator{
    
    public ClusterToolTipGenerator(){
        
    }
    
    @Override
    public String generateToolTip(XYDataset dataset, int series, int item){
        
        ClustersDataSet clusterDataSet = (ClustersDataSet)(dataset);
        DataItem dataItem = clusterDataSet.getSeries(series).GetDataItem(item);
        
        return dataItem.toString();
        //TODO Return enhanced tool tip
       //return dataItem.toFormattedString(null);
    }
    
    //TODO: Generar la inteligencia del formateo en esta clase
    //Usar formateadores de numeros para no mostrar tantos decimales
    
//    @Override
//    public String toFormattedString(List<String> featuresNames){
//        String str = "Source: id: " + this.id + "\n";
//        str += "Features: \n";
//        for (int i = 0; i < features.length; i++) {
//            str += featuresNames.get(i) + ": " + features[i] + "\n";
//        }
//        return str;        
//    }
    
        
//     public String toFormattedString(List<String> featuresNames){
//        
//        String str = "Mode: id: " + id + "\n";
//        str += "Bandwidth: " + bandwidth + "\n";
//        str += "Features: \n";
//        for (int i = 0; i < features.length; i++) {
//            str += featuresNames.get(i) + ": " + features[i] + "\n";
//        }
//        return str;
//    }
     
}
