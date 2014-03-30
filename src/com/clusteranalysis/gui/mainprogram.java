/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clusteranalysis.gui;

//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import com.clusteranalysis.database.*;

public class mainprogram {
    
    private static MySQLResultsReader db;

    public mainprogram() {
        
    }
    
    //Note: Typically the main method will be in a
    //separate class. As this is a simple one class
    //example it's all in the one class.
    public static void main(String[] args) throws Exception {        
        
        
        String fpath = null; //Path to properties file

        //Get path to configuration file
        if (!com.jscilib.io.parameter.ParameterHandler.optionExist("-f", args)) {
            throw new Exception("Incorrect input parameters. No properties file given\n");
        } else {
            fpath = com.jscilib.io.parameter.ParameterHandler.getOption("-f", args);
        }

        //Read configuration file
        java.util.Properties properties = new java.util.Properties();
        properties.load(new java.io.FileReader(fpath));
        String dbDriver = properties.getProperty("jdbc.driver");
        String dbUser = properties.getProperty("jdbc.user");
        String dbpassword = properties.getProperty("jdbc.password");
        String dbURL = properties.getProperty("jdbc.url.results");
        String dbURLSources = properties.getProperty("jdbc.url.sources");
        System.out.println("URL: " + dbUser + "@" + dbURL);
        System.out.println("URL Sources: " + dbUser + "@" + dbURLSources);

        //Get the Driver and the connection
        Class.forName(dbDriver);
        Connection conn = java.sql.DriverManager.getConnection(dbURL, dbUser, dbpassword);
        Connection connSources = java.sql.DriverManager.getConnection(dbURLSources, dbUser, dbpassword);

        //Test this class
        db = new MySQLResultsReader(conn, connSources);
        
        
        mainprogram gui = new mainprogram();
        gui.GUIDesign();
        
    }
    
    public void GUIDesign() throws Exception {
        
        JFrame guiFrame = new JFrame();

        //make sure the program exits when the frame closes and center on the screen
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Cluster Analyzer");
        guiFrame.setSize(600, 550);
        guiFrame.setLocationRelativeTo(null);
        Container contentPane = guiFrame.getContentPane();

        //Split screen in two
        JPanel plotPanel = new JPanel();
        JPanel dataPanel = new JPanel();
        JSplitPane leftPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, plotPanel, dataPanel);
        JPanel toolPanel = new JPanel();
        JSplitPane root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, toolPanel);
        contentPane.add(root);

        //Plot Panel
        plotPanel.setBackground(Color.red);

        //Data Panel
        dataPanel.setBackground(Color.green);

        //Tool Panel
        Box toolBox = new Box(BoxLayout.Y_AXIS);
        toolPanel.add(toolBox);

        //Data bases Panel
        Box dataBasesBox = new Box(BoxLayout.X_AXIS);
        toolBox.add(dataBasesBox);
        
        JLabel dbLabel = new JLabel("Databases:");
        dataBasesBox.add( dbLabel );
        String dbNames[] = {"Astrostatistics_pm_sh", "DB2", "DB3", "DB4"};
        final JComboBox dbCombo = new JComboBox(dbNames);
        dataBasesBox.add( dbCombo );
        
        toolBox.add(Box.createRigidArea(new Dimension(0,10)));
        toolBox.add( new JSeparator(SwingConstants.HORIZONTAL) );
        toolBox.add(Box.createRigidArea(new Dimension(0,10)));

        //Bandwidth Panel
        Box bandwidthBox = new Box(BoxLayout.X_AXIS);
        toolBox.add(bandwidthBox);
        
        JLabel bdLabel = new JLabel("Bandwidths:");
        bandwidthBox.add( bdLabel );
        List<Double> bandwidths = db.GetBandwidths();
        final JComboBox bandwidthsCombo = new JComboBox( new Vector<Double>(bandwidths) );
        bandwidthBox.add( bandwidthsCombo );
        
        toolBox.add(Box.createRigidArea(new Dimension(0,10)));
        toolBox.add( new JSeparator(SwingConstants.HORIZONTAL) );
        toolBox.add(Box.createRigidArea(new Dimension(0,10)));
        
        //Variable Panels
        Box variablesBox = new Box(BoxLayout.Y_AXIS);
        toolBox.add(variablesBox);
        
        JLabel dbVariables = new JLabel("Variables:");
        variablesBox.add( dbVariables );
        
        List<String> features = db.FeatureNames();
        final DefaultListModel model = new DefaultListModel();
        for(int i = 0; i < features.size(); i ++){
            model.addElement( features.get(i) );
        }
        final JList variableList = new JList(model);
        JScrollPane scrollVariables = new JScrollPane( variableList );
        variablesBox.add( scrollVariables );
        
        toolBox.add(Box.createRigidArea(new Dimension(0,10)));
        toolBox.add( new JSeparator(SwingConstants.HORIZONTAL) );
        toolBox.add(Box.createRigidArea(new Dimension(0,10)));
        
        //Button Panels
        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        buttonBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        toolBox.add(buttonBox);
        JButton applyButton = new JButton("Plot data");
        buttonBox.add( applyButton );
        
        ActionListener buttonListener = new ActionListener(){            
            public void actionPerformed(ActionEvent actionEvent){                
                //Get selected parameters
                String dbName = (String)dbCombo.getSelectedItem();
                Double bandwidth = (Double)bandwidthsCombo.getSelectedItem();
                List<String> variables = variableList.getSelectedValuesList();  
                
                //Plot this query
                //PlotQuery(bandwidth, variables, tableName);
                System.out.println("Plot: " + dbName + " bandwidth: " + bandwidth + " variables: " + variables);
            }            
        };
        
        applyButton.addActionListener( buttonListener );

        //make sure the JFrame is visible
        guiFrame.setVisible(true);
        leftPanel.setDividerLocation(0.85);
        root.setDividerLocation(0.85);
        
    }
    
    void PlotQuery(double bandwidth, List<String> variables){
                    
        //List<Double> table = db.GetQuery(bandwidth, variables);
        
    }
            
    
}
