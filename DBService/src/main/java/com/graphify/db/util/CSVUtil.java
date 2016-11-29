package com.graphify.db.util;

import com.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sushant on 22-11-2016.
 */
public class CSVUtil {

    public static void main(String[] args) throws IOException {
        // Getting ClassLoader obj
        ClassLoader classLoader = CSVUtil.class.getClassLoader();
        CSVReader csvReader = new CSVReader(new FileReader(classLoader.getResource("./mysql/ENTITY.csv").getPath()));
        String[] nextLine;
        while ((nextLine = csvReader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            System.out.println(nextLine[0] + nextLine[1] + nextLine[2]);
        }
    }

    public static String[] getHeader(String fileName) {
        // Getting ClassLoader obj
        ClassLoader classLoader = CSVUtil.class.getClassLoader();
        //Always expecting it to be in /mysql folder and in caps
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(classLoader.getResource("./mysql/" + fileName.toUpperCase() + ".csv").getPath()));
            String[] nextLine;
            if ((nextLine = csvReader.readNext()) != null) {
                //return first line
                //System.out.println(CSVUtil.class.getCanonicalName() + " returning header of size "+ nextLine.length);
                return nextLine;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                csvReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String[] getHeaderFromSource(String file) {
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(file));
            String[] nextLine;
            if ((nextLine = csvReader.readNext()) != null) {
                //return first line
                //System.out.println(CSVUtil.class.getCanonicalName() + " returning header of size "+ nextLine.length);
                return nextLine;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (csvReader != null)
                    csvReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static List<String[]> getContent(String fileName) {
        // Getting ClassLoader obj
        ClassLoader classLoader = CSVUtil.class.getClassLoader();
        //Always expecting it to be in /mysql folder and in caps
        CSVReader csvReader = null;
        List<String[]> content = new ArrayList<>();
        try {
            csvReader = new CSVReader(new FileReader(classLoader.getResource("./mysql/" + fileName.toUpperCase() + ".csv").getPath()));
            String[] nextLine;
            if ((csvReader.readNext()) != null) {  //Skip first line
                while ((nextLine = csvReader.readNext()) != null) {
                    //return first line
                    content.add(nextLine);
                }
                return content;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                csvReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
