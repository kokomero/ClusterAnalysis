/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clusteranalysis.gui;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author victor
 */
public class ModeTableModel extends AbstractTableModel {
        
    //Stores names of each column
    private String columnNames[];
    
    //Stores actual data
    private ArrayList< ArrayList<Object> > data;    
    
    public ModeTableModel(List<String> variables){
    
        //Define column names and clases
        columnNames = new String[3 + variables.size()]; 
        columnNames[0] = "Mode id";              
        columnNames[1] = "Visible";        
        columnNames[2] = "Num Elements";
        for(int i = 0; i < variables.size(); i++){
            columnNames[3+i] = variables.get(i);
        }
        
        //Initialize data table
        data = new ArrayList< ArrayList<Object> >();        
    }
    
    public void addRow(ArrayList<Object> row){
        data.add( row );
    }
    
    public int getColumnCount() {
            return columnNames.length;
        }
    
     public int getRowCount() {
            return data.size();
        }
     
     public String getColumnName(int col) {
            return columnNames[col];
        }
     
     public Object getValueAt(int row, int col) {
            return data.get(row).get(col);
        }

     public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    
    
}
