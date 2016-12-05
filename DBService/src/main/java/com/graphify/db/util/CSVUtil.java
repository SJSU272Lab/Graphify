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
        CSVReader csvReader = new CSVReader(new FileReader(classLoader.getResource("./mysql/entity.csv").getPath()));
        String[] nextLine;
        while ((nextLine = csvReader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            System.out.println(nextLine[0] + nextLine[1] + nextLine[2]);
        }
    }

    public static String[] getHeader(String fileLocation, String fileName) {
        //Always expecting it to be in /mysql folder and in caps
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(fileLocation + "\\" + fileName.toUpperCase() + ".csv"));
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


    public static String[] getHeaderFromSource(String fileLocation, String fileName) {
        CSVReader csvReader = null;
        try {
            if(System.getProperty("os.name").contains("Windows")) {
                csvReader = new CSVReader(new FileReader(fileLocation + "\\" + fileName + ".csv"));
            }
            else {
                csvReader = new CSVReader(new FileReader(fileLocation + "/" + fileName + ".csv"));
            }
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


    public static List<String[]> getContent(String fileLocation, String fileName) {
        //Always expecting it to be in /mysql folder and in caps
        CSVReader csvReader = null;
        List<String[]> content = new ArrayList<>();
        try {
            if(System.getProperty("os.name").contains("Windows")) {
                csvReader = new CSVReader(new FileReader(fileLocation + "\\" + fileName + ".csv"));
            }
            else {
                csvReader = new CSVReader(new FileReader(fileLocation + "/" + fileName + ".csv"));
            }
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
