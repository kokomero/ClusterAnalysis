/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.plot;

import java.awt.Color;
import java.awt.Shape;

/**
 *
 * @author victor
 */
public abstract class DataSerie {
    
    private Color color;
    private Shape shape;
    
    public DataSerie(Color color, Shape shape){
        this.color = color;
        this.shape = shape;        
    }
    
    public Color GetColor(){
        return color;
    }
    
    public Shape GetShape(){
        return shape;
    }
    
    abstract public double[][] GetDataSerie();
    
}
