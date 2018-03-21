package com.pelletier;

import java.util.ArrayList;
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
		Box box = new Box();

		
		//will read from file and put 10,000 random 10*10 objects into the quadtree
		
		try {
			List<String> lines = Files.readAllLines(Paths.get("src/test/resources/objects.txt"));
						
			for(int i = 0; i < lines.size(); i++){
				box.insert(new SearchRectangleObject(Double.parseDouble(lines.get(i).split(" ")[0]), Double.parseDouble(lines.get(i).split(" ")[1]), 10.0, 10.0, String.valueOf(i)));
				quadTree.insert(new SearchRectangleObject(Double.parseDouble(lines.get(i).split(" ")[0]), Double.parseDouble(lines.get(i).split(" ")[1]), 10.0, 10.0, String.valueOf(i)));
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
				List<RectangleObject> itemsFromBox = box.search(searchRectangleObjects[i]);
				List<RectangleObject> itemsFromQuadTree = quadTree.search(searchRectangleObjects[i]);
				
				if(itemsFromBox.size() != itemsFromQuadTree.size()){
					System.out.println("Search Area-> " + " X:" + searchRectangleObjects[i].getX() + " Y: " + searchRectangleObjects[i].getY() + " W: " + searchRectangleObjects[i].getW() + " H: " + searchRectangleObjects[i].getH());
					List<RectangleObject> differentObjects = getDifferentObjects(itemsFromBox, itemsFromQuadTree);
					System.out.println("Objects Not Found By QuadTree ");
					for(int j = 0; j < differentObjects.size(); j++){
						System.out.println(" X:" + differentObjects.get(j).getX() + " Y: " + differentObjects.get(j).getY() + " W: " + differentObjects.get(j).getW() + " H: " + differentObjects.get(j).getH());
					}
				}
				
				numberOfObjectsQueriedFor += itemsFromQuadTree.size();
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
	
	//pass box first
	static List<RectangleObject> getDifferentObjects(List<RectangleObject> objects1, List<RectangleObject> objects2){
		List<RectangleObject> rectangleObjectsToReturn = new ArrayList<>();
		for(RectangleObject object1 : objects1){
			boolean found = false;
			for(RectangleObject object2 : objects2){
				if(object1.getId().equals(object2.getId())){
					//will break if found so it will never be added
					found = true;
					break;
				}
			}
			if(!found){
				rectangleObjectsToReturn.add(object1);
			}
		}

		return rectangleObjectsToReturn;
	}
	
}
