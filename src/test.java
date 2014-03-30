/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author victor
 */
import com.jscilib.io.parameter.ParameterHandler;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class test {

    public static String synopsis() throws Exception {
        StringBuilder sb = new StringBuilder("");
        sb.append("ClusterAnalysis -f <properties_file> ");
        sb.append("\n\t<properties_file> - properties file with info about the data base anda data to be analyzed");
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

        String fpath = null; //Path to properties file
        
        //Get path to configuration file
        if (!ParameterHandler.optionExist("-f", args)) {
            throw new Exception("Incorrect input parameters. No properties file given\n"
                    + test.synopsis());
        } else {
            fpath = ParameterHandler.getOption("-f", args);
        }
        
        //Read configuration file
        Properties properties = new Properties();
        properties.load(new FileReader( fpath ));

        
        String prop1 = properties.getProperty("com.analyzer.test");
        String prop2 = properties.getProperty("com.analyzer.test2");
        System.out.println("Esto es una prueba 1: " + prop1);
        System.out.println("Esto es una prueba 2: " + prop2);
        
    }
}
