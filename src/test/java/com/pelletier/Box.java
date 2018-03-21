package com.pelletier;

import java.util.ArrayList;
import java.util.List;

import com.pelletier.geometry.GeometryUtil;
import com.pelletier.geometry.RectangleObject;
import com.pelletier.geometry.SearchRectangleObject;

public class Box {
	
	public List<RectangleObject> rectangleObjects;
	
	public Box(){}
	
    public void insert(RectangleObject rectangleObject){
    	if(this.rectangleObjects == null){
    		this.rectangleObjects = new ArrayList<>();
    	}
    	this.rectangleObjects.add(rectangleObject);
    }
    
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
