package com.pelletier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import com.pelletier.geometry.RectangleObject;
import com.pelletier.geometry.SearchRectangleObject;

public class BaselinePerformanceReport {

	public static void main(String[] args) {

		Box box = new Box();
		
		//will read from file and put 10,000 random 10*10 objects into the quadtree
		
		try {
			List<String> lines = Files.readAllLines(Paths.get("src/test/resources/objects.txt"));
						
			for(String line: lines){
				box.insert(new SearchRectangleObject(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]), 10.0, 10.0));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("***** Quadtree Summary *****");
		System.out.println("Depth: " + box.rectangleObjects.size());
		System.out.println("******************************************");
		
		int numberOfObjectsQueriedFor = 0;
		try {
			List<String> lines = Files.readAllLines(Paths.get("src/test/resources/randomSearchAreas.txt"));
			
			//can't do parseInt here, will screw up results,should buffer all numbers into an array
			SearchRectangleObject[] searchRectangleObjects = new SearchRectangleObject[lines.size()];
			
			for(int i = 0; i < lines.size(); i++){
				searchRectangleObjects[i] = new SearchRectangleObject(Double.parseDouble(lines.get(i).split(" ")[0]), Double.parseDouble(lines.get(i).split(" ")[1]), 
						Double.parseDouble(lines.get(i).split(" ")[2]), Double.parseDouble(lines.get(i).split(" ")[3]));
			}
			
			System.out.println("***** Beginning Searches *****");
			long startMilliseconds = System.currentTimeMillis();
			System.out.println("Time: "  + new Date(startMilliseconds));
			
			for(int i = 0; i < searchRectangleObjects.length; i++){
				List<RectangleObject> items = box.search(searchRectangleObjects[i]);
				numberOfObjectsQueriedFor += items.size();
//				System.out.println((i + 1) + ". " + lines.get(i).split(" ")[0] + " " + lines.get(i).split(" ")[1] + " " + lines.get(i).split(" ")[2] + " " + lines.get(i).split(" ")[3] + " " + items.size());
			}
			long endMilliseconds = System.currentTimeMillis();

			System.out.println("***** Searches Complete *****");
			System.out.println("Total Searches: " + searchRectangleObjects.length);
			System.out.println("Total Objects Queried For: " + numberOfObjectsQueriedFor);
			System.out.println("Time Elapsed (millis): " + (endMilliseconds - startMilliseconds));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
