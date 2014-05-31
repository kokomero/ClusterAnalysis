/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clusteranalysis.plot;


import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.data.xy.XYDataset;

import com.clusteranalysis.datamodel.DataItem;

/**
 *
 * @author victor
 */
public class ClusterToolTipGenerator extends StandardXYToolTipGenerator{
    
    public ClusterToolTipGenerator(){
        
    }
    
    public String generateToolTip(XYDataset dataset, int series, int item){
        
        ClustersDataSet clusterDataSet = (ClustersDataSet)(dataset);
        DataItem dataItem = clusterDataSet.getSeries(series).GetDataItem(item);
        
        return dataItem.toString();
    }
}
