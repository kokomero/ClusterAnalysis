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
public class ModesDataSerie extends DataSerie {
    
    private List<Mode> modes;
    
    public ModesDataSerie(List<Mode> modes, Color color, Shape shape){
        super("Modes", color, shape);
        this.modes = modes;        
    }
    
    public ModesDataSerie(List<Mode> modes, Shape shape){
        super("Modes", shape);
        this.modes = modes;        
    }
       
    public double[][] GetDataSerie(){        
        
        int dataLength = modes.size();
        double[][] series = new double[2][dataLength];
        for (int i = 0; i < dataLength; i++) {
            Mode row = modes.get(i);
            series[0][i] = (Double) row.getFeatures()[0];
            series[1][i] = (Double) row.getFeatures()[1];
        }
        return series;
        
    }
    
    @Override
    public DataItem GetDataItem(int index){
        return modes.get(index);
    }
    
}
