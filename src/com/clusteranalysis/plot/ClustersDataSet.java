/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.plot;

import java.util.ArrayList;
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
    
    private ArrayList<DataSerie> series;
    
    public ClustersDataSet(){
        this.series = new ArrayList<DataSerie>();
    }    
    
    public void addSeries(DataSerie serie){
        this.series.add(serie);
        super.addSeries(serie.GetName(), serie.GetDataSerie() );        
    }
    
    public DataSerie getSeries(int index){
        return this.series.get(index);
    }
}
