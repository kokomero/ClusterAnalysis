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
import java.text.NumberFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;

import com.clusteranalysis.database.*;
import com.clusteranalysis.datamodel.*;
import com.clusteranalysis.plot.PlotUtils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

import com.clusteranalysis.plot.ClustersDataSet;
import com.clusteranalysis.plot.ModesDataSerie;
import com.clusteranalysis.plot.ClusterDataSerie;
import com.clusteranalysis.plot.ClusterToolTipGenerator;
import java.util.Arrays;

public class mainprogram {

    private static ClusterResultReader db;
    private JFrame guiFrame;
    
    private JPanel dataPanel;

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
       
        //Get a result reader        
        db = ClusterResultReaderFactory.GetClusterResultReader(properties);

        mainprogram gui = new mainprogram();
        gui.GUIDesign();

    }

    public void GUIDesign() throws Exception {

        guiFrame = new JFrame();

        //make sure the program exits when the frame closes and center on the screen
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Cluster Analyzer");
        guiFrame.setSize(600, 550);
        guiFrame.setMinimumSize(new Dimension(400, 300));
        guiFrame.setLocationRelativeTo(null);
        Container contentPane = guiFrame.getContentPane();

        //Create Chart and data panels
        final JFreeChart chart = CreateEmptyChart();
        ChartPanel plotPanel = new ChartPanel(chart, false);
        dataPanel = new JPanel();

        //Create left panel and add components
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(plotPanel);
        leftPanel.add(dataPanel);

        //create right panel for tools
        JPanel toolPanel = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        contentPane.add(leftPanel);
        contentPane.add(toolPanel);

        //Tool Panel
        Box toolBox = new Box(BoxLayout.Y_AXIS);
        toolPanel.add(toolBox);

        //Data bases Panel
        Box dataBasesBox = new Box(BoxLayout.X_AXIS);
        toolBox.add(dataBasesBox);

        JLabel dbLabel = new JLabel("Databases:");
        dataBasesBox.add(dbLabel);
        String dbNames[] = {"Astrostatistics_pm_sh", "DB2", "DB3", "DB4"};
        final JComboBox dbCombo = new JComboBox(dbNames);
        dataBasesBox.add(dbCombo);

        toolBox.add(Box.createRigidArea(new Dimension(0, 10)));
        toolBox.add(new JSeparator(SwingConstants.HORIZONTAL));
        toolBox.add(Box.createRigidArea(new Dimension(0, 10)));

        //Bandwidth Panel
        Box bandwidthBox = new Box(BoxLayout.X_AXIS);
        toolBox.add(bandwidthBox);

        JLabel bdLabel = new JLabel("Bandwidths:");
        bandwidthBox.add(bdLabel);
        List<Double> bandwidths = db.GetBandwidths();
        final JComboBox bandwidthsCombo = new JComboBox(new Vector<Double>(bandwidths));
        bandwidthBox.add(bandwidthsCombo);

        toolBox.add(Box.createRigidArea(new Dimension(0, 10)));
        toolBox.add(new JSeparator(SwingConstants.HORIZONTAL));
        toolBox.add(Box.createRigidArea(new Dimension(0, 10)));

        //Variable Panels
        Box variablesBox = new Box(BoxLayout.Y_AXIS);
        toolBox.add(variablesBox);

        JLabel dbVariables = new JLabel("Variables:");
        variablesBox.add(dbVariables);

        List<String> features = db.FeatureNames();
        final DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < features.size(); i++) {
            model.addElement(features.get(i));
        }
        final JList variableList = new JList(model);
        JScrollPane scrollVariables = new JScrollPane(variableList);
        variablesBox.add(scrollVariables);

        toolBox.add(Box.createRigidArea(new Dimension(0, 10)));
        toolBox.add(new JSeparator(SwingConstants.HORIZONTAL));
        toolBox.add(Box.createRigidArea(new Dimension(0, 10)));

        //Button Panels
        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        buttonBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        toolBox.add(buttonBox);
        JButton applyButton = new JButton("Plot data");
        buttonBox.add(applyButton);

        ActionListener buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                //Get selected parameters
                String dbName = (String) dbCombo.getSelectedItem();
                Double bandwidth = (Double) bandwidthsCombo.getSelectedItem();
                List<String> variables = variableList.getSelectedValuesList();

                //DEBUG
                System.out.println("Plot: " + dbName + " bandwidth: " + bandwidth + " variables: " + variables);

                //PLot graph
                PlotQuery(bandwidth, variables, chart);
            }
        };

        applyButton.addActionListener(buttonListener);

        //make sure the JFrame is visible
        guiFrame.setVisible(true);
        System.out.println("Minimum size for tool pane: " + toolBox.getMinimumSize());


    }

    void PlotQuery(double bandwidth, List<String> variables, JFreeChart chart) {

        //Check at least two variables have been chosen
        if (variables.size() < 2) {
            JOptionPane.showMessageDialog(guiFrame, "Choose at least two variables", "Variable selection", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Get modes for this bandwidth and data series for each mode
        List<Cluster> clusters;
        int numberClusters;
        List<Integer> modeColumnIndexes;
        
        try {
            clusters = db.GetClusters(bandwidth, variables);
            numberClusters = clusters.size();
            modeColumnIndexes = db.GetFeatureIndexes(variables);
        } catch (ResultReaderException e) {
            JOptionPane.showMessageDialog(guiFrame, "Query to database failed", "Database error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
                
        //Create the data set
        final ClustersDataSet dataset = new ClustersDataSet();     
          
        //Add a serie for mode
        ModesDataSerie modes = new ModesDataSerie( PlotUtils.GetModes(clusters),
                Color.BLACK, 
                ShapeUtilities.createDiagonalCross(3, 1));                
        dataset.addSeries(modes);  
  
        //for each mode add data points belonging to cluster
        for (int i = 0; i < numberClusters; i++) {
            String serieId = "Mode: " + clusters.get(i).GetMode().getId();
            
            //Create and add the data serie
            ClusterDataSerie serie = new ClusterDataSerie(clusters.get(i), 
                    serieId,
                    ShapeUtilities.createDiamond(2));
            dataset.addSeries(serie);
        }

        //El renderer recibe un XYPlot, que internamente tiene un dataset
        //Si creamos una clase DataSet con dataseries, cada una tiene la información del color
        //y del simbolo quizás sería más facil.
        //Y luego creamos un renderer derivado que admite nuestro particular DataSet

        //Get a reference to the renderer of the plot  and modify color and shapes
        //for each serie
        XYItemRenderer renderer = (XYItemRenderer) chart.getXYPlot().getRenderer();
        renderer.setToolTipGenerator(new ClusterToolTipGenerator() );
        
        for(int i = 0; i < dataset.getSeriesCount(); i++){
            renderer.setSeriesPaint(i, dataset.getSeries(i).GetColor() );
            renderer.setSeriesShape(i, dataset.getSeries(i).GetShape() );
        }
        
        //Change title and axis names
        chart.setTitle("Modes bandwidth=" + bandwidth);
        chart.getXYPlot().getDomainAxis().setLabel(variables.get(0));
        chart.getXYPlot().getRangeAxis().setLabel(variables.get(1));

        //Change dataset
        chart.getXYPlot().setDataset(dataset);

        // set the renderer's stroke for each data serie        
        Stroke modesStroke = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        renderer.setSeriesStroke(0, modesStroke);
        
        //Populate the table model with data from clusters
        ModeTableModel modeTableModel = new ModeTableModel(variables);
        for (Cluster cluster : clusters) {
            ArrayList<Object> row = new ArrayList<Object>();
            Mode mode = cluster.GetMode();
            
            row.add( mode.getId() );
            row.add( true );
            row.add(cluster.GetNumberOfSamples());
            Object features[] = mode.getFeatures( modeColumnIndexes );
            row.addAll(Arrays.asList(features));
            
            modeTableModel.addRow(row);
        }
        
        //Create the table of the data panel
        JTable modeTable = new JTable(modeTableModel);
        JScrollPane modeTableScrollPane = new JScrollPane(modeTable);
        modeTable.setFillsViewportHeight(true);
        dataPanel.add(modeTableScrollPane);
        
        dataPanel.repaint(); 
        
        

    }  

    /**
     * Create a chart.
     *
     * @param dataset the dataset
     * @return the chart
     */
    private JFreeChart CreateEmptyChart() {

        // create the empty dataset...
        DefaultXYDataset dataset = new DefaultXYDataset();

        //Create the chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "", // chart title
                "", // domain axis label
                "", // range axis label
                dataset, // initial series
                PlotOrientation.VERTICAL, // orientation
                false, // include legend
                true, // tooltips?
                false // URLs?
                );

        // set chart background
        chart.setBackgroundPaint(Color.white);

        // set a few custom plot features
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(0xffffe0));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        // set the plot's axes to display integers
        TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setStandardTickUnits(ticks);
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setStandardTickUnits(ticks);

        // render shapes and lines
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
        plot.setRenderer(renderer);
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);

        // label the points        
//        NumberFormat format = NumberFormat.getNumberInstance();
//        format.setMaximumFractionDigits(2);
//        
//        XYItemLabelGenerator generator =
//        new StandardXYItemLabelGenerator(
//        StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT,
//        format, format);
//        
//        renderer.setBaseItemLabelGenerator(generator);
//        renderer.setBaseItemLabelsVisible(true);
         
        
        return chart;
    }
}
