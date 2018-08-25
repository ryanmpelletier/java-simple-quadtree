package io.github.ryanp102694.geometry;

//TODO write GeometryUtil tests
public class GeometryUtil {

    public static Double distanceBetweenPoints(Double x1,Double y1,Double x2,Double y2){
        return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public static Boolean rectangleObjectsOverlap(RectangleObject r1, RectangleObject r2){
        if(r1.getX() > r2.getX() + r2.getW() || r2.getX() > r1.getX() + r1.getW()){
            return false;
        }
        if(r1.getY() > r2.getY() + r2.getH() || r2.getY() > r1.getY() + r1.getH()){
           return false;
        }

        return true;
    }
    
    public static Boolean rectangleObjectIsInside(RectangleObject containingRectangle, RectangleObject testRectangle){

    	return (testRectangle.getX() >= containingRectangle.getX()) 
    			&& (testRectangle.getX() + testRectangle.getW() <= containingRectangle.getX() + containingRectangle.getW())
    			&& (testRectangle.getY() >= containingRectangle.getY())
    			&& (testRectangle.getY() + testRectangle.getH() <= containingRectangle.getY() + containingRectangle.getH());
    }
}
