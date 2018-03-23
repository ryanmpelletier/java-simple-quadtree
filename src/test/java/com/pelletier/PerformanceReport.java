package com.pelletier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pelletier.geometry.RectangleObject;
import com.pelletier.geometry.SearchRectangleObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Compares a brute force 2d collision detection method vs the QuadTree.
 * Inserts 10,000 10 * 10 objects at randomly generated coordinates.
 * Compares the amount of time it takes to query both the Box (brute force) and the QuadTree.
 */
public class PerformanceReport {
	
	
	
	public static void main(String[] args){
	
		/**
		 * A 10,000 * 10,000 QuadTree. It will expand to at most 3 levels, and will carry a maximum of 25
		 * objects per node before splitting, unless the maxLevels has been reached.
		 */
		QuadTree quadTree = new QuadTree(25, 3, 0, 0, 0, 10000, 10000, null);

		/**
		 * A simple container for storing objects.
		 * It doesn't need size bounds because it just stores a list of objects.
		 */
		Box box = new Box();

        /**
         * objectCoordinates.txt contains 10,000 pairs of randomly generated coordinates
         * A 10 * 10 object will be inserted into both the Box and the QuadTree at each of these coordinates.
         */
		
		try {
			List<String> lines = Files.readAllLines(Paths.get("src/test/resources/objectCoordinates.txt"));
						
			for(int i = 0; i < lines.size(); i++){
				box.insert(new SearchRectangleObject(Double.parseDouble(lines.get(i).split(" ")[0]), Double.parseDouble(lines.get(i).split(" ")[1]), 10.0, 10.0, String.valueOf(i)));
				quadTree.insert(new SearchRectangleObject(Double.parseDouble(lines.get(i).split(" ")[0]), Double.parseDouble(lines.get(i).split(" ")[1]), 10.0, 10.0, String.valueOf(i)));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("***** Quadtree Summary *****");
		System.out.println("Total Objects: " + quadTree.getTotalObjects());
		System.out.println("Actual Depth: " + quadTree.getDepth());
        System.out.println("Configured Max Levels: " + quadTree.getMaxLevels());
        System.out.println("Configured Max Objects Per Node: " + quadTree.getMaxObjects());
		System.out.println("******************************************");
		System.out.println("\n");

        System.out.println("***** Box Summary *****");
        System.out.println("Total Objects: " + box.rectangleObjects.size());
        System.out.println("***********************");
        System.out.println("\n");


        /**
         * Stat variables for the Box and QuadTree.
         */
        int quadTreeTotalObjectsQueriedFor = 0;
        long quadTreeTotalQueryTime = 0;
        long quadTreeMaxQueryTime = 0;

        int boxTotalObjectsQueriedFor = 0;
        long boxTotalQueryTime = 0;
        long boxMaxQueryTime = 0;


		try {
			List<String> lines = Files.readAllLines(Paths.get("src/test/resources/randomSearchAreas.txt"));

            /**
             * I am going to create a bunch of SearchRectangleObjects now so that I don't need to later.
             * I don't want it to affect the query time.
             */
			SearchRectangleObject[] searchRectangleObjects = new SearchRectangleObject[lines.size()];
			for(int i = 0; i < lines.size(); i++){
				searchRectangleObjects[i] = new SearchRectangleObject(Double.parseDouble(lines.get(i).split(" ")[0]), Double.parseDouble(lines.get(i).split(" ")[1]), 
						Double.parseDouble(lines.get(i).split(" ")[2]), Double.parseDouble(lines.get(i).split(" ")[3]));
			}
			
			System.out.println("***** Beginning Searches *****\n");

			
			for(int i = 0; i < searchRectangleObjects.length; i++){

			    long boxSearchStartTime = System.currentTimeMillis();
				List<RectangleObject> itemsFromBox = box.search(searchRectangleObjects[i]);
				long now = System.currentTimeMillis();
				long boxQueryTime = now - boxSearchStartTime;
				boxTotalQueryTime = boxTotalQueryTime + boxQueryTime;
				boxMaxQueryTime = Math.max(boxMaxQueryTime, boxQueryTime);

				long quadTreeSearchStartTime = System.currentTimeMillis();
				List<RectangleObject> itemsFromQuadTree = quadTree.search(searchRectangleObjects[i]);
				now = System.currentTimeMillis();
				long quadTreeQueryTime = now - quadTreeSearchStartTime;
				quadTreeTotalQueryTime = quadTreeTotalQueryTime + quadTreeQueryTime;
				quadTreeMaxQueryTime = Math.max(quadTreeMaxQueryTime, quadTreeQueryTime);

				if(itemsFromBox.size() != itemsFromQuadTree.size()){
					System.out.println("Search Area-> " + " X:" + searchRectangleObjects[i].getX() + " Y: " + searchRectangleObjects[i].getY() + " W: " + searchRectangleObjects[i].getW() + " H: " + searchRectangleObjects[i].getH());
					List<RectangleObject> differentObjects = getDifferentObjects(itemsFromBox, itemsFromQuadTree);
					System.out.println("Objects Not Found By QuadTree ");
					for(int j = 0; j < differentObjects.size(); j++){
						System.out.println(" X:" + differentObjects.get(j).getX() + " Y: " + differentObjects.get(j).getY() + " W: " + differentObjects.get(j).getW() + " H: " + differentObjects.get(j).getH());
					}
					throw new RuntimeException("Identical Query for Box and QuadTree returned different objects!");
				}

                quadTreeTotalObjectsQueriedFor = quadTreeTotalObjectsQueriedFor + itemsFromQuadTree.size();
				boxTotalObjectsQueriedFor = boxTotalObjectsQueriedFor + itemsFromBox.size();
			}


            System.out.println("***** QuadTree Performance Summary *****");
            System.out.println("Total Queries Run: " + searchRectangleObjects.length);
            System.out.println("Total Objects Queried For: " + quadTreeTotalObjectsQueriedFor);
            System.out.println("Total Query Time: " + quadTreeTotalQueryTime);
            System.out.println("Max Query Time: " + quadTreeMaxQueryTime);
            System.out.println("Average Query Time: " + ((float) quadTreeTotalQueryTime / (float) searchRectangleObjects.length));
            System.out.println("\n");

            System.out.println("***** Box Performance Summary *****");
            System.out.println("Total Queries Run: " + searchRectangleObjects.length);
            System.out.println("Total Objects Queried For: " + boxTotalObjectsQueriedFor);
            System.out.println("Total Query Time: " + boxTotalQueryTime);
            System.out.println("Max Query Time: " + boxMaxQueryTime);
            System.out.println("Average Query Time: " + ((float)boxTotalQueryTime / (float)searchRectangleObjects.length));
            System.out.println("\n");



		} catch (IOException e) {
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
