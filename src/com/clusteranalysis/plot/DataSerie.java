/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.plot;

import java.util.Random;
import java.awt.Color;
import java.awt.Shape;

import com.clusteranalysis.datamodel.DataItem;

/**
 *
 * @author victor
 */
public abstract class DataSerie {
    
    private Color color;
    private Shape shape;
    private String name;
    
    public DataSerie(String name, Color color, Shape shape){
        this.name = name;
        this.color = color;
        this.shape = shape;        
    }
    
    public DataSerie(String name, Shape shape){
        
        this.name = name;        
        Random randColor = new Random();
        Color randomColor = new Color(randColor.nextInt(127) + 127,
                    randColor.nextInt(127) + 127,
                    randColor.nextInt(127) + 127);
                
        this.color = randomColor;
        this.shape = shape;   
    }
    
  
    public Color GetColor(){
        return this.color;
    }
    
    public Shape GetShape(){
        return this.shape;
    }
    
    public String GetName(){
        return this.name;
    }
    
    abstract public DataItem GetDataItem(int index);
    
    abstract public double[][] GetDataSerie();
    
}
