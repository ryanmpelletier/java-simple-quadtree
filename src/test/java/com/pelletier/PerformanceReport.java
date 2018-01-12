package com.pelletier;

import java.util.Date;
import java.util.List;

import com.pelletier.geometry.RectangleObject;
import com.pelletier.geometry.SearchRectangleObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PerformanceReport {
	
	
	
	public static void main(String[] args){
	
		/**
		 * A really big quadtree, can hold 25 objects per tree,
		 * will go up to 10 levels deep, starts at (0,0) and is 10,000 * 10,000, no parent
		 */
		QuadTree quadTree = new QuadTree(25, 3, 0, 0, 0, 10000, 10000, null);
		
		//will read from file and put 10,000 random 10*10 objects into the quadtree
		
		try {
			List<String> lines = Files.readAllLines(Paths.get("src/test/resources/objects.txt"));
						
			for(String line: lines){
				quadTree.insert(new SearchRectangleObject(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]), 10.0, 10.0));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("***** Quadtree Summary *****");
		System.out.println("Total Objects: " + quadTree.getTotalObjects());
		System.out.println("Depth: " + quadTree.getDepth());
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
				List<RectangleObject> items = quadTree.search(searchRectangleObjects[i]);
				numberOfObjectsQueriedFor += items.size();
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
