package com.pelletier.geometry;
import static org.junit.Assert.*;

import com.pelletier.QuadTree;
import org.junit.Test;

import java.util.List;

public class SearchRectangleObjectTest {



    @Test
    public void testSplit(){

        SearchRectangleObject searchRectangleObject = new SearchRectangleObject(5.0, 5.0, 10.0, 10.0);

        //split rectangle where rectangle remains whole
        List<SearchRectangleObject> splitRectangles = searchRectangleObject.split(0.0, 0.0);

        assertEquals(0, splitRectangles.size());

        //split rectangle along each of its corners, make sure it gives me back a single SearchRectangleObject (that isn't null)
        splitRectangles = searchRectangleObject.split(5.0, 5.0);
        assertEquals(4, splitRectangles.size());
        assertNull(splitRectangles.get(QuadTree.NE_CHILD));
        assertNull(splitRectangles.get(QuadTree.NW_CHILD));
        assertNull(splitRectangles.get(QuadTree.SW_CHILD));
        assertNotNull(splitRectangles.get(QuadTree.SE_CHILD));
        assertEquals(Double.valueOf(5.0), splitRectangles.get(QuadTree.SE_CHILD).getX());
        assertEquals(Double.valueOf(5.0), splitRectangles.get(QuadTree.SE_CHILD).getY());
        assertEquals(Double.valueOf(10.0), splitRectangles.get(QuadTree.SE_CHILD).getW());
        assertEquals(Double.valueOf(10.0), splitRectangles.get(QuadTree.SE_CHILD).getH());


        splitRectangles = searchRectangleObject.split(5.0, 15.0);
        assertEquals(4, splitRectangles.size());
        assertNotNull(splitRectangles.get(QuadTree.NE_CHILD));
        assertEquals(Double.valueOf(5.0), splitRectangles.get(QuadTree.NE_CHILD).getX());
        assertEquals(Double.valueOf(5.0), splitRectangles.get(QuadTree.NE_CHILD).getY());
        assertEquals(Double.valueOf(10.0), splitRectangles.get(QuadTree.NE_CHILD).getW());
        assertEquals(Double.valueOf(10.0), splitRectangles.get(QuadTree.NE_CHILD).getH());
        assertNull(splitRectangles.get(QuadTree.NW_CHILD));
        assertNull(splitRectangles.get(QuadTree.SW_CHILD));
        assertNull(splitRectangles.get(QuadTree.SE_CHILD));


        splitRectangles = searchRectangleObject.split(15.0, 15.0);
        assertEquals(4, splitRectangles.size());
        assertNull(splitRectangles.get(QuadTree.NE_CHILD));
        assertNotNull(splitRectangles.get(QuadTree.NW_CHILD));
        assertEquals(Double.valueOf(5.0), splitRectangles.get(QuadTree.NW_CHILD).getX());
        assertEquals(Double.valueOf(5.0), splitRectangles.get(QuadTree.NW_CHILD).getY());
        assertEquals(Double.valueOf(10.0), splitRectangles.get(QuadTree.NW_CHILD).getW());
        assertEquals(Double.valueOf(10.0), splitRectangles.get(QuadTree.NW_CHILD).getH());
        assertNull(splitRectangles.get(QuadTree.SW_CHILD));
        assertNull(splitRectangles.get(QuadTree.SE_CHILD));

        splitRectangles = searchRectangleObject.split(15.0, 5.0);
        assertEquals(4, splitRectangles.size());
        assertNull(splitRectangles.get(QuadTree.NE_CHILD));
        assertNull(splitRectangles.get(QuadTree.NW_CHILD));
        assertNotNull(splitRectangles.get(QuadTree.SW_CHILD));
        assertEquals(Double.valueOf(5.0), splitRectangles.get(QuadTree.SW_CHILD).getX());
        assertEquals(Double.valueOf(5.0), splitRectangles.get(QuadTree.SW_CHILD).getY());
        assertEquals(Double.valueOf(10.0), splitRectangles.get(QuadTree.SW_CHILD).getW());
        assertEquals(Double.valueOf(10.0), splitRectangles.get(QuadTree.SW_CHILD).getH());
        assertNull(splitRectangles.get(QuadTree.SE_CHILD));

        //split rectangle from the middle
        splitRectangles = searchRectangleObject.split(10.0, 10.0);
        assertEquals(4, splitRectangles.size());

        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.NE_CHILD).getX());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NE_CHILD).getY());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NE_CHILD).getH());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NE_CHILD).getW());

        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NW_CHILD).getX());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NW_CHILD).getY());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NW_CHILD).getH());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NW_CHILD).getW());

        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SW_CHILD).getX());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.SW_CHILD).getY());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SW_CHILD).getH());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SW_CHILD).getW());

        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.SE_CHILD).getX());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.SE_CHILD).getY());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SE_CHILD).getH());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SE_CHILD).getW());


        //split from top
        splitRectangles = searchRectangleObject.split(10.0, 0.0);
        assertEquals(4, splitRectangles.size());

        assertNull(splitRectangles.get(QuadTree.NE_CHILD));
        assertNull(splitRectangles.get(QuadTree.NW_CHILD));


        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SW_CHILD).getX());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SW_CHILD).getY());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.SW_CHILD).getH());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SW_CHILD).getW());

        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.SE_CHILD).getX());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SE_CHILD).getY());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.SE_CHILD).getH());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SE_CHILD).getW());

        //split from bottom
        splitRectangles = searchRectangleObject.split(10.0, 20.0);
        assertEquals(4, splitRectangles.size());

        assertNull(splitRectangles.get(QuadTree.SE_CHILD));
        assertNull(splitRectangles.get(QuadTree.SW_CHILD));


        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NW_CHILD).getX());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NW_CHILD).getY());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.NW_CHILD).getH());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NW_CHILD).getW());

        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.NE_CHILD).getX());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NE_CHILD).getY());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.NE_CHILD).getH());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NE_CHILD).getW());

        //split from left
        splitRectangles = searchRectangleObject.split(0.0, 10.0);
        assertEquals(4, splitRectangles.size());

        assertNull(splitRectangles.get(QuadTree.NW_CHILD));
        assertNull(splitRectangles.get(QuadTree.SW_CHILD));


        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NE_CHILD).getX());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NE_CHILD).getY());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NE_CHILD).getH());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.NE_CHILD).getW());

        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SE_CHILD).getX());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.SE_CHILD).getY());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SE_CHILD).getH());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.SE_CHILD).getW());

        //split from right
        splitRectangles = searchRectangleObject.split(20.0, 10.0);
        assertEquals(4, splitRectangles.size());

        assertNull(splitRectangles.get(QuadTree.NE_CHILD));
        assertNull(splitRectangles.get(QuadTree.SE_CHILD));


        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NW_CHILD).getX());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NW_CHILD).getY());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.NW_CHILD).getH());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.NW_CHILD).getW());

        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SW_CHILD).getX());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.SW_CHILD).getY());
        assertEquals(Double.valueOf(5), splitRectangles.get(QuadTree.SW_CHILD).getH());
        assertEquals(Double.valueOf(10), splitRectangles.get(QuadTree.SW_CHILD).getW());



    }


}
