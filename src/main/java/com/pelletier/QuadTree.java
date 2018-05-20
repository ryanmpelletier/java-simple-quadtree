package com.pelletier;

import com.pelletier.geometry.RectangleObject;
import com.pelletier.geometry.SearchRectangleObject;
import com.pelletier.geometry.GeometryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
//TODO limit use of SearchRectangleObject, also consider refactoring split method into Utility class and getting rid of SRO
//TODO limit recursion

public class QuadTree {

    public static final int THIS_QUADTREE = -1;
    public static final int NE_CHILD = 0;
    public static final int NW_CHILD = 1;
    public static final int SW_CHILD = 2;
    public static final int SE_CHILD = 3;

    //config
    private int maxObjects = 10;
    private int maxLevels = 5;
    private int defaultWidth = 100;
    private int defaultHeight = 100;

    //link to parent
    private QuadTree parent;
    
    //children
    private QuadTree[] children;
    
    //objects in node
    private List<RectangleObject> rectangleObjects;

    //how deep this QuadTree is
    private Integer level;

    //bounds
    private Integer x;
    private Integer y;
    private Integer w;
    private Integer h;

    //create a QuadTree with no parent, intended for creating root node
    public QuadTree(){
        this.level = 0;
        this.x = 0;
        this.y = 0;
        this.w = defaultWidth;
        this.h = defaultHeight;
        this.rectangleObjects = new ArrayList<>();
        this.parent = null;
        this.children = new QuadTree[4];
    }

    public QuadTree(int maxObjects, int maxLevels, int level, int x, int y, int w, int h, QuadTree parent){
        this.maxObjects = maxObjects;
        this.maxLevels = maxLevels;
        this.level = level;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.rectangleObjects = new ArrayList<>();
        this.parent = parent;
        this.children = new QuadTree[4];
    }

    public void insert(RectangleObject rectangleObject){
        //if this QuadTree has children (only need to check the first one to be sure of that)
        if(children[0] != null){
            int indexToPlaceObject = getChildIndexRectangleBelongsIn(rectangleObject);

            //if it doesn't belong on this QuadTree, let one of the children find where to put it
            if(indexToPlaceObject != QuadTree.THIS_QUADTREE){
                children[indexToPlaceObject].insert(rectangleObject);
                return;
            }
        }
        //add the object to the list for this QuadTree
        rectangleObjects.add(rectangleObject);

        //if we need to split up this QuadTree
        if(rectangleObjects.size() > this.maxObjects && this.level < this.maxLevels) {
            //code I am stealing had a check right here, which I think is unnecessary
            split();

            //just split QuadTree, lets put the objects that are on this QuadTree where they belong
            int i = 0;
            while (i < rectangleObjects.size()) {
                int indexToPlaceObject = getChildIndexRectangleBelongsIn(rectangleObjects.get(i));
                //if the object does not belong on this QuadTree
                if (indexToPlaceObject != QuadTree.THIS_QUADTREE) {
                    //if we remove an object don't increment i, the next object bumps down to where i is
                    children[indexToPlaceObject].insert(rectangleObjects.remove(i));
                } else {
                    i++;
                }
            }
        }
    }
    
    //probably should add a re-balance function to quadtrees where it takes all its kids and re-inserts them
    public RectangleObject remove(RectangleObject rectangleObject){
        int index = getChildIndexRectangleBelongsIn(rectangleObject);
        if(index == QuadTree.THIS_QUADTREE || this.children[index] == null){
            for(int i = 0; i < this.rectangleObjects.size(); i++){
                if(rectangleObjects.get(i).getId().equals(rectangleObject.getId())){
                    return rectangleObjects.remove(i);
                }
            }
        }else{
            return this.children[index].remove(rectangleObject);
        }

        return null;
    }
    
    public void update(RectangleObject initialRectangleObject, RectangleObject updatedRectangleObject){
    	
    	//start with root
    	QuadTree quadTree = this;
    	int index = quadTree.getChildIndexRectangleBelongsIn(initialRectangleObject);
    	
    	//travel down the quadTree until I am where the item could potentially be
    	while(index != QuadTree.THIS_QUADTREE && quadTree.children[index] != null){
    		quadTree = quadTree.children[index];
    		index = quadTree.getChildIndexRectangleBelongsIn(initialRectangleObject);
    	}
    	
    	//loop through items on quadTree and see if we can find the one we are updating
    	//there is a bug here! What happens if there are two items in the QuadTree and we update the first one and it remains on the quadTree, I thin our update code will run twice!
        for(int i = 0; i < quadTree.getRectangleObjects().size(); i++){
            if(quadTree.getRectangleObjects().get(i).getId().equals(initialRectangleObject.getId())){
            	
            	//write updated properties on object
            	RectangleObject rectangleObjectToUpdate = quadTree.getRectangleObjects().remove(i);
            	rectangleObjectToUpdate.setX(updatedRectangleObject.getX());
            	rectangleObjectToUpdate.setY(updatedRectangleObject.getY());
            	rectangleObjectToUpdate.setW(updatedRectangleObject.getW());
            	rectangleObjectToUpdate.setH(updatedRectangleObject.getH());
            	
            	//if the updated properties still lie in this quadtree, just add the object to the list
            	if(GeometryUtil.rectangleObjectIsInside(new SearchRectangleObject(Double.valueOf(quadTree.getX()), Double.valueOf(quadTree.getY()), Double.valueOf(quadTree.getW()), Double.valueOf(quadTree.getH())),updatedRectangleObject)){
            		quadTree.insert(rectangleObjectToUpdate);
            	}else{ //start looking back up the quadtree starting with this ones parent
            		quadTree = quadTree.getParent();
            		while(quadTree.getParent() != null && !GeometryUtil.rectangleObjectIsInside(new SearchRectangleObject(Double.valueOf(quadTree.getX()), Double.valueOf(quadTree.getY()), Double.valueOf(quadTree.getW()), Double.valueOf(quadTree.getH())), rectangleObjectToUpdate)){
            			quadTree = quadTree.getParent();
            		}
            		//only insert if it actually fits in the quadtree I am on
            		if(GeometryUtil.rectangleObjectIsInside(new SearchRectangleObject(Double.valueOf(quadTree.getX()), Double.valueOf(quadTree.getY()), Double.valueOf(quadTree.getW()), Double.valueOf(quadTree.getH())), rectangleObjectToUpdate)){
                		quadTree.insert(rectangleObjectToUpdate);
            		}
            		
            	}
            	//will never be updating more than one, so top going through the list once we find a match
            	break;
            }
        }
    }

    public List<RectangleObject> search(SearchRectangleObject searchRectangleObject){

        List<RectangleObject> returnList = new ArrayList<>();
        //here I will need to filter through these and only return the objects that are in the search area (even if they are partially in the search area)
        ListIterator<RectangleObject> iterator = search(new ArrayList<RectangleObject>(), searchRectangleObject).listIterator();
        while(iterator.hasNext()){
            RectangleObject rectangleObject = iterator.next();
            if(GeometryUtil.rectangleObjectsOverlap(rectangleObject, searchRectangleObject)){
                returnList.add(rectangleObject);
            }
        }
        return returnList;
    }

    public Integer getDepth(){
        return 1 + Math.max(
                Math.max(this.children[QuadTree.NE_CHILD] == null ? 0 : this.children[QuadTree.NE_CHILD].getDepth(), this.children[QuadTree.NW_CHILD] == null ? 0 : this.children[QuadTree.NW_CHILD].getDepth()),
                Math.max(this.children[QuadTree.SW_CHILD] == null ? 0 : this.children[QuadTree.SW_CHILD].getDepth(), this.children[QuadTree.SE_CHILD] == null ? 0 : this.children[QuadTree.SE_CHILD].getDepth())
        );
    }

    public Integer getTotalObjects(){
        return this.rectangleObjects.size() +
                (this.children[QuadTree.NE_CHILD] == null ? 0 : this.children[QuadTree.NE_CHILD].getTotalObjects()) +
                (this.children[QuadTree.NW_CHILD] == null ? 0 : this.children[QuadTree.NW_CHILD].getTotalObjects()) +
                (this.children[QuadTree.SW_CHILD] == null ? 0 : this.children[QuadTree.SW_CHILD].getTotalObjects()) +
                (this.children[QuadTree.SE_CHILD] == null ? 0 : this.children[QuadTree.SE_CHILD].getTotalObjects());
    }

    //recursively remove this quadtree's children
    public void clear(){
        this.rectangleObjects.clear();
        for(int i = 0; i < children.length; i++){
            if(children[i] != null){
                children[i].clear();
                children[i] = null;
            }
        }
    }

    private List<RectangleObject> search(List<RectangleObject> rectangleObjects, SearchRectangleObject searchRectangleObject){

        rectangleObjects.addAll(this.rectangleObjects);

        int index = getChildIndexRectangleBelongsIn(searchRectangleObject);
        //if the search area does not fit into any of the children perfectly
        if(index == QuadTree.THIS_QUADTREE || this.children[0] == null){
            //add anything that is on this QuadTree, may need to recurse down and add more
            if(this.children[0] != null){
                //for each of the children, if the search area overlaps with the child area, search the child
                for(int i = 0; i < this.children.length; i++){
                    if(GeometryUtil.rectangleObjectsOverlap(new SearchRectangleObject(Double.valueOf(this.children[i].getX()), Double.valueOf(this.children[i].getY()),Double.valueOf(this.children[i].getW()), Double.valueOf(this.children[i].getH())), searchRectangleObject)){
                        this.children[i].search(rectangleObjects, searchRectangleObject);
                    }
                }
            }
        }else if(this.children[index] != null){
            //search area is in one of the children totally, but we still can't exclude the objects on this node, because that search area could include one
            this.children[index].search(rectangleObjects, searchRectangleObject);
        }
        return rectangleObjects;
    }



    //instantiate the four children
    private void split(){
        int childWidth = this.w / 2;
        int childHeight = this.h / 2;

        children[QuadTree.NE_CHILD] = new QuadTree(this.maxObjects, this.maxLevels,level + 1, this.x + childWidth, this.y, childWidth, childHeight, this);
        children[QuadTree.NW_CHILD] = new QuadTree(this.maxObjects, this.maxLevels,this.level + 1, this.x, this.y, childWidth, childHeight, this);
        children[QuadTree.SW_CHILD] = new QuadTree(this.maxObjects, this.maxLevels,this.level + 1, this.x, this.y + childHeight, childWidth, childHeight, this);
        children[QuadTree.SE_CHILD] = new QuadTree(this.maxObjects, this.maxLevels,this.level + 1, this.x + childWidth, this.y + childHeight, childWidth, childHeight, this);
    }
    
    protected int getChildIndexRectangleBelongsIn(RectangleObject rectangleObject){
        //-1 means the object does not fit in any of the children, keep it on the parent
        int index = -1;
        double verticalDividingLine = getX() + getW() / 2;
        double horizontalDividingLine = getY() + getH() / 2;

        //its funny, here you might be tempted to think about what happens if it goes over the bounds, but if it did...we wouldn't have gotten this far, it would be on a parent QuadTree!
        boolean fitsCompletelyInNorthHalf = rectangleObject.getY() < horizontalDividingLine && (rectangleObject.getH() + rectangleObject.getY() < horizontalDividingLine);
        boolean fitsCompletelyInSouthHalf = rectangleObject.getY() > horizontalDividingLine;
        boolean fitsCompletelyInWestHalf = rectangleObject.getX() < verticalDividingLine && (rectangleObject.getX() + rectangleObject.getW() < verticalDividingLine);
        boolean fitsCompletelyInEastHalf = rectangleObject.getX() > verticalDividingLine;

        if(fitsCompletelyInEastHalf){
            if(fitsCompletelyInNorthHalf){
                index = QuadTree.NE_CHILD;
            }else if(fitsCompletelyInSouthHalf){
                index = QuadTree.SE_CHILD;
            }
        }else if(fitsCompletelyInWestHalf){
            if(fitsCompletelyInNorthHalf){
                index = QuadTree.NW_CHILD;
            }else if(fitsCompletelyInSouthHalf){
                index = QuadTree.SW_CHILD;
            }
        }
        return index;
    }

    public int getMaxObjects() {
        return maxObjects;
    }

    public void setMaxObjects(int maxObjects) {
        this.maxObjects = maxObjects;
    }

    public int getMaxLevels() {
        return maxLevels;
    }

    public void setMaxLevels(int maxLevels) {
        this.maxLevels = maxLevels;
    }

    public int getDefaultWidth() {
        return defaultWidth;
    }

    public void setDefaultWidth(int defaultWidth) {
        this.defaultWidth = defaultWidth;
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }

    public void setDefaultHeight(int defaultHeight) {
        this.defaultHeight = defaultHeight;
    }

    public List<RectangleObject> getRectangleObjects() {
        return rectangleObjects;
    }

    public void setRectangleObjects(List<RectangleObject> rectangleObjects) {
        this.rectangleObjects = rectangleObjects;
    }
    
    public void addRectangleObject(RectangleObject rectangleObject) {
    	if(this.rectangleObjects == null){
    		this.rectangleObjects = new ArrayList<>();
    	}
    	this.rectangleObjects.add(rectangleObject);
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public QuadTree getParent(){
    	return this.parent;
    }
    
    public void setParent(QuadTree parent){
    	this.parent = parent;
    }

    public QuadTree[] getChildren() {
        return children;
    }

    public void setChildren(QuadTree[] children) {
        this.children = children;
    }
}
