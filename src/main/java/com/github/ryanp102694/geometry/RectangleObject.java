package com.github.ryanp102694.geometry;

/**
 *  Objects inserted into the QuadTree must implement this interface
 */
public interface RectangleObject {

    public String getId();

    public void setId(String id);

    public String getType();

    public void setType(String type);

    public Double getX();

    public void setX(Double x);

    public Double getY();

    public void setY(Double y);

    public Double getH();

    public void setH(Double h);

    public Double getW();

    public void setW(Double w);

}
