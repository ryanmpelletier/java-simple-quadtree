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
		QuadTree quadTree = new QuadTree(25, 6, 0, 0, 0, 10000, 10000, null);
		
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
		
		
		try {
			List<String> lines = Files.readAllLines(Paths.get("src/test/resources/randomSearchAreas.txt"));
			
			//can't do parseInt here, will screw up results,should buffer all numbers into an array
			double[][] searchAreas = new double[10000][4];
			
			for(int i = 0; i < lines.size(); i++){
				searchAreas[i][0] = Double.parseDouble(lines.get(i).split(" ")[0]);
				searchAreas[i][1] = Double.parseDouble(lines.get(i).split(" ")[1]);
				searchAreas[i][2] = Double.parseDouble(lines.get(i).split(" ")[2]);
				searchAreas[i][3] = Double.parseDouble(lines.get(i).split(" ")[3]);
			}
			
			System.out.println("***** Beginning Searches *****");
			long startMilliseconds = System.currentTimeMillis();
			System.out.println("Time: "  + new Date(startMilliseconds));
			
			for(int i = 0; i < searchAreas.length; i++){
				List<RectangleObject> items = quadTree.search(new SearchRectangleObject(searchAreas[i][0], searchAreas[i][1], searchAreas[i][2], searchAreas[i][3]));
			}
			System.out.println("***** Searches Complete *****");
			System.out.println("Time Elapsed: " + (System.currentTimeMillis() - startMilliseconds));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
