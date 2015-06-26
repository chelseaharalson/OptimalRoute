/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.HashSet;
import java.io.FileWriter;

/**
 *
 * @author Chelsea
 */
public class OptimalRoute {
    static ArrayList<Location> visitedList = new ArrayList<Location>();
    static ArrayList<String> visitedStates = new ArrayList<String>();

    public static void main(String[] args) {
        String lat = "";
        String longitude = "";
        String name = "";
        String points = "";
        String state = "";
        String statePoints = "3";
        Double originLat = 0.0;
        Double originLong = 0.0;
        Double destLat = 0.0;
        Double destLong = 0.0;
        Double dist = 0.0;
        ArrayList<Location> locList = new ArrayList<Location>();
        ArrayList<Location> cList = new ArrayList<Location>();
        ArrayList<Location> closestDistList;
        ArrayList<Location> locRatio = new ArrayList<Location>();
        ArrayList<String> pathC = new ArrayList<String>();
        ArrayList<Location> pathObj = new ArrayList<Location>();
        Double pts = 0.0;
        Double time = 300.0;
        
        Location l = new Location(lat, longitude, name, points, state, statePoints);
        locList = l.getLocations();
        pathC.add(locList.get(1).name);
        pathObj.add(locList.get(1));

        while (time > 0) {
            int i = pathObj.size()-1;
            closestDistList = new ArrayList<Location>();
            originLat = Double.parseDouble(pathObj.get(i).latitude);
            originLong = Double.parseDouble(pathObj.get(i).longitude);
            visitedList.add(pathObj.get(i));
            visitedStates.add(pathObj.get(i).state);

            for (int j = 1; j < locList.size(); j++) {
                if ( (!(visitedList.contains(locList.get(j)))) && (!(visitedStates.contains(locList.get(j).state))) ) {
                    //System.out.println("Loc List: " + locList.get(j));
                    destLat = Double.parseDouble(locList.get(j).latitude);
                    destLong = Double.parseDouble(locList.get(j).longitude);
                    dist = l.calcDist(originLat, destLat, originLong, destLong);
                    pts = Double.parseDouble(locList.get(j).points);
                    closestDistList.add(new Location(locList.get(i).name, dist.toString(), 
                            locList.get(j).latitude, locList.get(j).longitude, 
                            locList.get(j).name, pts.toString(), "0", locList.get(j).state, locList.get(j).statePoints));
                }
                else if ( (!(visitedList.contains(locList.get(j)))) && (visitedStates.contains(locList.get(j).state)) ) {
                    destLat = Double.parseDouble(locList.get(j).latitude);
                    destLong = Double.parseDouble(locList.get(j).longitude);
                    statePoints = "0";
                    dist = l.calcDist(originLat, destLat, originLong, destLong);
                    pts = Double.parseDouble(locList.get(j).points);
                    closestDistList.add(new Location(locList.get(i).name, dist.toString(), 
                            locList.get(j).latitude, locList.get(j).longitude, 
                            locList.get(j).name, pts.toString(), "0", locList.get(j).state, statePoints));
                }
            }
            //System.out.println("CLOSEST DIST LIST !!!   " + closestDistList);
            cList = l.findClosestDist(closestDistList);
            //System.out.println("C-LIST: !!!!!!!  " + cList.get(0).name);
            
            locRatio = l.calcRatio(cList);
            time = time - 1 - (Double.parseDouble(locRatio.get(0).distance) / 65);
            pathC.add(locRatio.get(0).name);
            pathObj.add(locRatio.get(0));
        }
        System.out.println(pathC);
        String fname = "Team05.csv";
        writeCsvFile(fname, pathObj);
        //System.out.println(pathObj);
        System.out.println(total(pathObj));
    }
    
    public static void writeCsvFile(String fileName, ArrayList<Location> locList) {
        String COMMA_DELIMITER = ",";
	String NEW_LINE_SEPARATOR = "\n";
        String FILE_HEADER = "Name,Latitude,Longitude";

        FileWriter fileWriter = null;

        try {
                fileWriter = new FileWriter(fileName);

                //Write the CSV file header
                //fileWriter.append(FILE_HEADER.toString());

                //Add a new line separator after the header
                //fileWriter.append(NEW_LINE_SEPARATOR);

                //Write a new student object list to the CSV file
                for (Location locName : locList) {
                    fileWriter.append(locName.name);
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(locName.latitude);
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(locName.longitude);
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(NEW_LINE_SEPARATOR);
                }

                System.out.println("CSV file was created successfully !!!");

                } catch (Exception e) {
                        System.out.println("Error in CsvFileWriter !!!");
                        e.printStackTrace();
                } finally {

                        try {
                                fileWriter.flush();
                                fileWriter.close();
                        } catch (IOException e) {
                                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
                        }

                }
	}
    
    public static Double total(ArrayList<Location> local){
        Double total = 0.0;
        for (Location p : local) {
            total += Double.parseDouble(p.points);
        }
        return total;
    }
 
}
