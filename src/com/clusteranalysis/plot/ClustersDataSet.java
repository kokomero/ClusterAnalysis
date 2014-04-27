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
public class ClustersDataSet extends DataSet {

    
    //Dataset que contiene varios Cluster Data Serie
    
    //Es un data set que dinamicamente se peude seleccionar los dataseries que
    //se muestran y los que no, sin necesidad de crear y destruir objetos una vez que se han
    //leido los datos
    
    public ClustersDataSet(){
        
    }
    
    
    
    public void addSeries(Comparable seriesKey, DataSerie serie){
        super.addSeries(seriesKey, serie.GetDataSerie() );        
    }
}
