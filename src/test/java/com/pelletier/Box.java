package com.pelletier;

import java.util.ArrayList;
import java.util.List;

import com.pelletier.geometry.GeometryUtil;
import com.pelletier.geometry.RectangleObject;
import com.pelletier.geometry.SearchRectangleObject;

/**
 * This class is a container to test the QuadTree against. It simply keeps a list of RectangleObjects,
 * and searches by comparing every object in the list to the search area it is a O(n*n) solution to collision detection.
 */
public class Box {

    //list of items this container is holding
    public List<RectangleObject> rectangleObjects;
	
	public Box(){}

	//adds an item to this container
    public void insert(RectangleObject rectangleObject){
    	if(this.rectangleObjects == null){
    		this.rectangleObjects = new ArrayList<>();
    	}
    	this.rectangleObjects.add(rectangleObject);
    }


    //searches for items in this container that overlap the search area
    public List<RectangleObject> search(SearchRectangleObject searchRectangleObject){
    	List<RectangleObject> rectangleObjects = new ArrayList<>();
		for(RectangleObject objectInBox: this.rectangleObjects){
			if(GeometryUtil.rectangleObjectsOverlap(objectInBox, searchRectangleObject)){
				rectangleObjects.add(objectInBox);
			}
		}
    	
    	
		return rectangleObjects;
    }


}
