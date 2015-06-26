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

/**
 *
 * @author V635460
 */
public class Location {
    
    public String distance;
    public String latitude;
    public String longitude;
    public String name;
    public String orig;
    public String points;
    public String ratio;
    public String state;
    public String statePoints;
    
    public Location(String pLat, String pLong, String pName, String pPoints, String pState, String pStatePoints) {
        latitude = pLat;
        longitude = pLong;
        name = pName;
        points = pPoints;
        state = pState;
        statePoints = pStatePoints;
    }
    
    public Location(String pOrig, String pDistance, String pLat, String pLong, String pName, 
            String pPoints, String pRatio, String pState, String pStatePoints) {
        orig = pOrig;
        distance = pDistance;
        latitude = pLat;
        longitude = pLong;
        name = pName;
        points = pPoints;
        ratio = pRatio;
        state = pState;
        statePoints = pStatePoints;
    }
    
    public ArrayList<Location> getLocations() {
 
	String csvFile = "/Users/chelsea/NetBeansProjects/OptimalRoute/Locs.csv";
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
        ArrayList<Location> locationList = new ArrayList<Location>();
        String lat = "";
        String longitude = "";
        String name = "";
        String points = "";
        String state = "";

	try {
		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
		        // use comma as separator
			String[] position = line.split(cvsSplitBy);
                        lat = position[1];
                        longitude = position[0];
                        name = position[3];
                        points = position[8];
                        state = position[6];
 
			System.out.println("[Latitude= " + lat 
                                 + " , Longitude= " + longitude 
                                + " , Name= " + name 
                                + " , State= " + state 
                                + " , Points= " + points + "]");
                        
                        locationList.add(new Location(lat, longitude, name, points, state, "3"));
		}
                
                //System.out.println(locationList);
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	System.out.println("Done");
        
        return locationList;
  }
    
  public double calcDist(double lat1, double lat2, double lon1, double lon2) {

      final int R = 6371; // Radius of the earth

      Double latDistance = Math.toRadians(lat2 - lat1);
      Double lonDistance = Math.toRadians(lon2 - lon1);
      Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
              + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
              * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
      Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      double distance = R * c; // convert to kilometers

      distance = Math.pow(distance, 2);

      return Math.sqrt(distance);
    }
  
  public ArrayList<Location> calcRatio(ArrayList<Location> lList) {
      ArrayList<Location> highR = new ArrayList<Location>();
      ArrayList<Double> ratioList = new ArrayList<Double>();
      ArrayList<Location> best = new ArrayList<Location>();
      
      for (Location loc : lList) {
          Double tempDist = Double.parseDouble(loc.distance);
          Double tempPoints = Double.parseDouble(loc.points);
          Double tempStatePoints = Double.parseDouble(loc.statePoints);
          Double tempR = (tempPoints + tempStatePoints) / tempDist;
          loc.ratio = tempR.toString();
          highR.add(loc);
      }
      for (Location l : highR) {
          ratioList.add(Double.parseDouble(l.ratio));
      }
      Collections.sort(ratioList);
      Collections.reverse(ratioList);
      for (Location item : highR) {
          if (Double.parseDouble(item.ratio) == ratioList.get(0)) {
              best.add(item);
          }
      }
      //System.out.println("BEST!! " + best.get(0).ratio);
      return best;
  }
  
  public ArrayList<Location> findClosestDist(ArrayList<Location> locaList) {
        ArrayList<Location> sortedDist = new ArrayList<Location>();
        ArrayList<Double> numbers = new ArrayList<Double>();
        Double tempDist;
        
        for (Location l : locaList) {
            numbers.add(Double.parseDouble(l.distance));
        }
        
        Collections.sort(numbers);

        for (int i = 1; i < 11; i++) {
            if (numbers.get(i) != 0.0) {
                tempDist = numbers.get(i); 
                for (Location lt : locaList) {
                    if (Double.parseDouble(lt.distance) == tempDist) {
                        sortedDist.add(lt);
                    }
                }
            }
        }
        
        //System.out.println("SORTED DISTANCE: " + sortedDist.get(0).distance);
        return sortedDist;
    }
  
    @Override
    public boolean equals(Object o){
      if(o instanceof Location) {
        Location toCompare = (Location) o;
        return this.name.equals(toCompare.name);
      }
      return false;
    }
    
    @Override
    public int hashCode(){
        return 1;
    }

}
