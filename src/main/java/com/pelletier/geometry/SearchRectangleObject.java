package com.pelletier.geometry;


import java.util.ArrayList;
import java.util.List;

public class SearchRectangleObject extends AbstractRectangleObject {
    public SearchRectangleObject(){
        this.setX(0.0);
        this.setY(0.0);
        this.setW(0.0);
        this.setH(0.0);
    }

    public SearchRectangleObject(Double x, Double y, Double w, Double h){
        super(x,y,w,h);
        this.setType("structure");
        this.setId("testId");
    }

    public SearchRectangleObject(Double x, Double y, Double w, Double h, String id){
        super(x,y,w,h);
        this.setId(id);
    }



    /**
     * Given x and y, split this rectangle object into either 0, 2, or 4 rectangle objects.
     * The order the returned rectangles are placed in the list is important and should correspond to the
     * QuadTree's directions
     * @param x
     * @param y
     * @return
     */
    public List<SearchRectangleObject> split(Double x, Double y) {
        boolean splitVertically = false;
        boolean splitHorizontally = false;
        List<SearchRectangleObject> returnList = new ArrayList<>();

        //rectangle is split vertically
        if(x >= this.getX() && x <= this.getX() + this.getW()){
            splitVertically = true;
        }

        if(y >= this.getY() && y <= this.getY() + this.getH()){
            splitHorizontally = true;
        }

        if(splitHorizontally && splitVertically){
            returnList.add(makeSearchRectangleObject(x, getY(), getX() + getW() - x, y - getY()));
            returnList.add(makeSearchRectangleObject(getX(), getY(),x - getX(), y - getY()));
            returnList.add(makeSearchRectangleObject(getX(), y, x - getX(), getY() + getH() - y));
            returnList.add(makeSearchRectangleObject(x, y, getX() + getW() - x, getY() + getH() - y));
        }else if(splitHorizontally){
            //if splitting from the right
            if(x <= getX()){
                returnList.add(makeSearchRectangleObject(getX(), getY(), getW(), y - getY()));
                returnList.add(null);
                returnList.add(null);
                returnList.add(makeSearchRectangleObject(getX(), y,getW(), getY() + getH() - y));
            }else{
                returnList.add(null);
                returnList.add(makeSearchRectangleObject(getX(), getY(), getW(), y - getY()));
                returnList.add(makeSearchRectangleObject(getX(), y,getW(), getY() + getH() - y));
                returnList.add(null);
            }
        }else if(splitVertically){
            //if splitting from the top
            if(y <= getY()){
                returnList.add(null);
                returnList.add(null);
                returnList.add(makeSearchRectangleObject(getX(), getY(),x - getX(), getH()));
                returnList.add(makeSearchRectangleObject(x, getY(), getX() + getW() - x, getH()));
            }else{
                returnList.add(makeSearchRectangleObject(x, getY(),getX() + getW() - x, getH()));
                returnList.add(makeSearchRectangleObject(getX(), getY(),x - getX(), getH()));
                returnList.add(null);
                returnList.add(null);
            }
        }

        return returnList;
    }

    //I don't want to make search rectangles with 0 area, this is a method to help me do that
    private SearchRectangleObject makeSearchRectangleObject(Double x, Double y, Double w, Double h){
        if(w == 0 || h == 0){
            return null;
        }
        return new SearchRectangleObject(x, y, w, h);
    }
}
