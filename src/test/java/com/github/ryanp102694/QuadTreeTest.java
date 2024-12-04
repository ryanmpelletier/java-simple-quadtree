package com.github.ryanp102694;

import com.github.ryanp102694.geometry.RectangleObject;

import static org.junit.Assert.*;

import com.github.ryanp102694.geometry.SearchRectangleObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class QuadTreeTest {

    private QuadTree quadTree;

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testDefaultConstructor()
    {
        quadTree = new QuadTree();
        assertEquals(Integer.valueOf(0), quadTree.getLevel());
        assertEquals(Integer.valueOf(0), quadTree.getX());
        assertEquals(Integer.valueOf(0), quadTree.getY());
        assertEquals(Integer.valueOf(100), quadTree.getW());
        assertEquals(Integer.valueOf(100), quadTree.getH());
        assertArrayEquals(new QuadTree[4], quadTree.getChildren());
        assertEquals(0, quadTree.getRectangleObjects().size());
    }

    @Test
    public void testConstructorWithParams(){
        quadTree = new QuadTree(2,4,1, 2, 3, 4, 5, null);
        assertEquals(2, quadTree.getMaxObjects());
        assertEquals(4, quadTree.getMaxLevels());
        assertEquals(Integer.valueOf(1), quadTree.getLevel());
        assertEquals(Integer.valueOf(2), quadTree.getX());
        assertEquals(Integer.valueOf(3), quadTree.getY());
        assertEquals(Integer.valueOf(4), quadTree.getW());
        assertEquals(Integer.valueOf(5), quadTree.getH());
        assertArrayEquals(new QuadTree[4], quadTree.getChildren());
        assertEquals(0, quadTree.getRectangleObjects().size());
    }

    @Test
    public void testInsertAnItem(){
        quadTree = new QuadTree();
        quadTree.insert(new SearchRectangleObject());
        assertEquals(Integer.valueOf(1), Integer.valueOf(quadTree.getRectangleObjects().size()));

        //children are still null
        assertNull(quadTree.getChildren()[QuadTree.NE_CHILD]);
        assertNull(quadTree.getChildren()[QuadTree.NW_CHILD]);
        assertNull(quadTree.getChildren()[QuadTree.SW_CHILD]);
        assertNull(quadTree.getChildren()[QuadTree.SE_CHILD]);
    }

    @Test
    public void testInsertMultipleItems(){
        quadTree = new QuadTree();

        quadTree.insert(new SearchRectangleObject());
        quadTree.insert(new SearchRectangleObject());
        quadTree.insert(new SearchRectangleObject());
        quadTree.insert(new SearchRectangleObject());
        quadTree.insert(new SearchRectangleObject());

        //5 items in tree
        assertEquals(Integer.valueOf(5), Integer.valueOf(quadTree.getRectangleObjects().size()));

        //children are still null
        assertNull(quadTree.getChildren()[QuadTree.NE_CHILD]);
        assertNull(quadTree.getChildren()[QuadTree.NW_CHILD]);
        assertNull(quadTree.getChildren()[QuadTree.SW_CHILD]);
        assertNull(quadTree.getChildren()[QuadTree.SE_CHILD]);
    }

    @Test
    public void testInsertWithSplitIntoEachChild(){
        quadTree = new QuadTree();

        //only store 2 objects per node
        quadTree.setMaxObjects(2);

        quadTree.insert(new SearchRectangleObject(1.0,1.0,1.0,1.0));
        quadTree.insert(new SearchRectangleObject(51.0,51.0,1.0,1.0));

        //no split has happened yet
        assertEquals(Integer.valueOf(2), Integer.valueOf(quadTree.getRectangleObjects().size()));

        quadTree.insert(new SearchRectangleObject(51.0,1.0,1.0,1.0));

        //split happened, root should not hold any objects
        assertEquals(Integer.valueOf(0), Integer.valueOf(quadTree.getRectangleObjects().size()));

        //NW quadrant has 1 object, SE quadrant also has one object, NE has one object, SW still has 0
        assertEquals(Integer.valueOf(1), Integer.valueOf(quadTree.getChildren()[QuadTree.NW_CHILD].getRectangleObjects().size()));
        assertEquals(Integer.valueOf(1), Integer.valueOf(quadTree.getChildren()[QuadTree.SE_CHILD].getRectangleObjects().size()));
        assertEquals(Integer.valueOf(1), Integer.valueOf(quadTree.getChildren()[QuadTree.NE_CHILD].getRectangleObjects().size()));
        assertEquals(Integer.valueOf(0), Integer.valueOf(quadTree.getChildren()[QuadTree.SW_CHILD].getRectangleObjects().size()));

        //insert one that should go to SW
        quadTree.insert(new SearchRectangleObject(1.0,51.0,1.0,1.0));
        assertEquals(Integer.valueOf(1), Integer.valueOf(quadTree.getChildren()[QuadTree.SW_CHILD].getRectangleObjects().size()));
    }

    //TODO: bad practice to use anything random in unit tests (or is it good practice?)
    public void testCountTotalObjects(){

        quadTree = new QuadTree();
        //insert 10 objects
        for(int i = 0; i < 10; i++){
            quadTree.insert(new SearchRectangleObject(Math.random() * 100, Math.random() * 100, 1.0, 1.0));
        }
        assertEquals(Integer.valueOf(10), quadTree.getTotalObjects());

        for(int i = 0; i < 5; i++){
            quadTree.insert(new SearchRectangleObject(Math.random() * 100, Math.random() * 100, 1.0, 1.0));
        }

        assertEquals(Integer.valueOf(15), quadTree.getTotalObjects());

    }


    @Test
    public void testGetDepth(){
        quadTree = new QuadTree();
        quadTree.setMaxObjects(2);

        //insert one object into NW
        quadTree.insert(new SearchRectangleObject(0.0,0.0,1.0,1.0));

        assertEquals(Integer.valueOf(1), quadTree.getDepth());

        //insert another object SE
        quadTree.insert(new SearchRectangleObject(51.0,51.0,1.0,1.0));
        assertEquals(Integer.valueOf(1), quadTree.getDepth());

        //insert a 3rd object into NE
        quadTree.insert(new SearchRectangleObject(51.0,0.0,1.0,1.0));
        assertEquals(Integer.valueOf(2), quadTree.getDepth());

        //now lets add a few more into the NW
        quadTree.insert(new SearchRectangleObject(26.0,26.0,1.0,1.0));
        assertEquals(Integer.valueOf(2), quadTree.getDepth());

        //another split occurs on this insert
        quadTree.insert(new SearchRectangleObject(26.0,0.0,1.0,1.0));
        assertEquals(Integer.valueOf(3), quadTree.getDepth());
    }

    @Test
    public void testClearQuadTree(){
        quadTree = new QuadTree();
        //insert 10 objects
        for(int i = 0; i < 10; i++){
            quadTree.insert(new SearchRectangleObject(Math.random() * 100, Math.random() * 100, 1.0, 1.0));
        }
        assertEquals(Integer.valueOf(10), quadTree.getTotalObjects());

        quadTree.clear();

        assertEquals(Integer.valueOf(0), quadTree.getTotalObjects());
        assertNull(quadTree.getChildren()[QuadTree.NE_CHILD]);
        assertNull(quadTree.getChildren()[QuadTree.NW_CHILD]);
        assertNull(quadTree.getChildren()[QuadTree.SW_CHILD]);
        assertNull(quadTree.getChildren()[QuadTree.SE_CHILD]);

    }

    @Test
    public void testSearch(){
        quadTree = new QuadTree();
        quadTree.setMaxObjects(2);

        List<RectangleObject> results;
        //search entire empty quadtree
        results = quadTree.search(new SearchRectangleObject(0.0, 0.0, 100.0, 100.0));

        assertNotNull(results);
        assertEquals(0, results.size());


        //insert one object into the quadtree
        quadTree.insert(new SearchRectangleObject(0.0, 0.0, 1.0, 1.0));

        //search the entire quadtree
        results = quadTree.search(new SearchRectangleObject(0.0, 0.0, 100.0, 100.0));
        assertEquals(1, results.size());

        //do a search that misses the item
        results = quadTree.search(new SearchRectangleObject(5.0, 5.0, 95.0, 95.0));
        assertEquals(0, results.size());


        //do an exact bounds search
        results = quadTree.search(new SearchRectangleObject(0.0, 0.0, 1.0, 1.0));
        assertEquals(1, results.size());

        //add a few more items in the quadtree
        quadTree.insert(new SearchRectangleObject(51.0, 51.0, 1.0, 1.0));
        quadTree.insert(new SearchRectangleObject(51.0, 0.0, 1.0, 1.0));

        quadTree.insert(new SearchRectangleObject(26.0, 26.0, 1.0, 1.0));
        quadTree.insert(new SearchRectangleObject(26.0, 0.0, 1.0, 1.0));
        quadTree.insert(new SearchRectangleObject(28.0, 28.0, 1.0, 1.0));

        quadTree.insert(new SearchRectangleObject(24.0, 24.0, 4.0, 4.0));

        //search entire quadtree again, make sure everything is found
        results = quadTree.search(new SearchRectangleObject(0.0, 0.0, 100.0, 100.0));
        assertEquals(7, results.size());

        //search in exact quadtree boundary
        results = quadTree.search(new SearchRectangleObject(50.0, 50.0, 50.0, 50.0));
        assertEquals(1, results.size());

        //search overlapping boundaries
        results = quadTree.search(new SearchRectangleObject(50.0, 0.0, 50.0, 100.0));
        assertEquals(2, results.size());

        //search overlapping an object
        results = quadTree.search(new SearchRectangleObject(25.0, 25.0, 25.0, 25.0));
        assertEquals(3, results.size());

        //search with no result fully in search area
        results = quadTree.search(new SearchRectangleObject(0.0, 25.0, 25.0, 25.0));
        assertEquals(1, results.size());

        //search spanning multiple areas and different levels of the quadtree
        results = quadTree.search(new SearchRectangleObject(0.0, 0.0, 50.0, 50.0));
        assertEquals(5, results.size());


        //test search area fits perfectly in child of quadtree that has the item it should hit
        results = quadTree.search(new SearchRectangleObject(23.0, 23.0, 1.5, 1.5));
        assertEquals(1, results.size());

        //test where search area is along the border of an object in the quadtree
        results = quadTree.search(new SearchRectangleObject(27.0, 26.0 , 1.0, 1.0));
        assertEquals(2, results.size());

        //test where search area is along the border of 2 objects in different levels of the quadtree
        results = quadTree.search(new SearchRectangleObject(1.0, 0.0, 23.0, 30.0));
        assertEquals(2, results.size());

        //test where search area is along the border of 2 objects in the same level of the quadtree
        results = quadTree.search(new SearchRectangleObject(27.0, 27.0, 1.0, 1.0));
        assertEquals(3, results.size());
    }


    @Test
    public void testRemove(){
        quadTree = new QuadTree();
        quadTree.setMaxObjects(2);

        quadTree.insert(new SearchRectangleObject(0.0, 0.0, 1.0, 1.0, "1"));
        //add a few more items in the quadtree
        quadTree.insert(new SearchRectangleObject(51.0, 51.0, 1.0, 1.0, "2"));
        quadTree.insert(new SearchRectangleObject(51.0, 0.0, 1.0, 1.0, "3"));

        quadTree.insert(new SearchRectangleObject(26.0, 26.0, 1.0, 1.0, "4"));
        quadTree.insert(new SearchRectangleObject(26.0, 0.0, 1.0, 1.0, "5"));
        quadTree.insert(new SearchRectangleObject(28.0, 28.0, 1.0, 1.0, "6"));

        quadTree.insert(new SearchRectangleObject(24.0, 24.0, 4.0, 4.0, "7"));

        assertEquals(Integer.valueOf(3), quadTree.getDepth());
        assertEquals(Integer.valueOf(7), quadTree.getTotalObjects());


        //remove an object that is in the quadtree
        RectangleObject rectangleObject = quadTree.remove(new SearchRectangleObject(0.0, 0.0, 1.0, 1.0, "1"));
        assertEquals(Double.valueOf(0.0), rectangleObject.getX());
        assertEquals(Double.valueOf(0.0), rectangleObject.getY());
        assertEquals(Double.valueOf(1.0), rectangleObject.getW());
        assertEquals(Double.valueOf(1.0), rectangleObject.getH());
        assertEquals("1", rectangleObject.getId());

        assertEquals(Integer.valueOf(6), quadTree.getTotalObjects());

        //remove an object that is not in the quadtree
        rectangleObject = quadTree.remove(new SearchRectangleObject(0.0, 0.0, 1.0, 1.0, "1"));
        assertNull(rectangleObject);

        //remove an object that is over a boundary
        rectangleObject = quadTree.remove(new SearchRectangleObject(24.0, 24.0, 4.0, 4.0, "7"));
        assertEquals(Double.valueOf(24.0), rectangleObject.getX());
        assertEquals(Double.valueOf(24.0), rectangleObject.getY());
        assertEquals(Double.valueOf(4.0), rectangleObject.getW());
        assertEquals(Double.valueOf(4.0), rectangleObject.getH());
        assertEquals("7", rectangleObject.getId());

        //remove the rest of the objects

        quadTree.remove(new SearchRectangleObject(51.0, 51.0, 1.0, 1.0, "2"));
        quadTree.remove(new SearchRectangleObject(51.0, 0.0, 1.0, 1.0, "3"));
        quadTree.remove(new SearchRectangleObject(26.0, 26.0, 1.0, 1.0, "4"));
        quadTree.remove(new SearchRectangleObject(26.0, 0.0, 1.0, 1.0, "5"));
        quadTree.remove(new SearchRectangleObject(28.0, 28.0, 1.0, 1.0, "6"));

        //verify that the quadtree is empty
        assertEquals(Integer.valueOf(0), quadTree.getTotalObjects());

        //verify that the quadtree still has the original depth
        assertEquals(Integer.valueOf(3), quadTree.getDepth());

    }
    
    @Test
    public void testUpdate(){
    	//going to set up the same quadtree I have been using
    	quadTree = new QuadTree();
        quadTree.setMaxObjects(2);

        quadTree.insert(new SearchRectangleObject(0.0, 0.0, 1.0, 1.0, "1"));
        quadTree.insert(new SearchRectangleObject(51.0, 51.0, 1.0, 1.0, "2"));
        quadTree.insert(new SearchRectangleObject(51.0, 0.0, 1.0, 1.0, "3"));
        quadTree.insert(new SearchRectangleObject(26.0, 26.0, 1.0, 1.0, "4"));
        quadTree.insert(new SearchRectangleObject(26.0, 0.0, 1.0, 1.0, "5"));
        quadTree.insert(new SearchRectangleObject(28.0, 28.0, 1.0, 1.0, "6"));
        quadTree.insert(new SearchRectangleObject(24.0, 24.0, 4.0, 4.0, "7"));
        
        //test updating non-existing item in quadTree, make sure no items were added
        quadTree.update(new SearchRectangleObject(5.0, 5.0, 5.0, 5.0, "test"), new SearchRectangleObject(25.0, 35.0, 5.0, 5.0));
        assertEquals(Integer.valueOf(7), quadTree.getTotalObjects());
        assertEquals(0,quadTree.search(new SearchRectangleObject(5.0, 5.0, 5.0, 5.0, "test")).size());
        assertEquals(0,quadTree.search(new SearchRectangleObject(25.0, 35.0, 5.0, 5.0, "test")).size());
        
        
        //test moving item out of quadtree
        quadTree.update(new SearchRectangleObject(28.0, 28.0, 1.0, 1.0, "6"), new SearchRectangleObject(100.0, 100.0, 1.0, 1.0, "6"));
        assertEquals(Integer.valueOf(6), quadTree.getTotalObjects());
        
        //test moving item to outside border of quadtree
        quadTree.update(new SearchRectangleObject(26.0, 26.0, 1.0, 1.0, "4"), new SearchRectangleObject(-.5, 0.5, 1.0, 1.0, "4"));
        assertEquals(Integer.valueOf(5), quadTree.getTotalObjects());

        
        //test move object without crossing QuadTree boundaries
        quadTree.update(new SearchRectangleObject(0.0, 0.0, 1.0, 1.0, "1"), new SearchRectangleObject(1.0, 1.0, 1.0, 1.0, "1"));
        
        List<RectangleObject> results = quadTree.getChildren()[QuadTree.NW_CHILD].getChildren()[QuadTree.NW_CHILD].getRectangleObjects();
        assertEquals(1, results.size());
        RectangleObject rectangleObject = results.get(0);
        assertEquals(Double.valueOf(1.0), rectangleObject.getX());
        assertEquals(Double.valueOf(1.0), rectangleObject.getY());
        assertEquals(Double.valueOf(1.0), rectangleObject.getW());
        assertEquals(Double.valueOf(1.0), rectangleObject.getH());
        assertEquals("1", rectangleObject.getId());
        
        //test move object across boundary back to root quadTree
        quadTree.update(new SearchRectangleObject(26.0, 0.0, 1.0, 1.0, "5"), new SearchRectangleObject(51.0, 5.0, 1.0, 1.0, "5"));
        results = quadTree.getChildren()[QuadTree.NE_CHILD].getRectangleObjects();
        assertEquals(2, results.size());
        
        //test moving object where it should go to a subtree
        quadTree.update(new SearchRectangleObject(24.0, 24.0, 4.0, 4.0, "7"), new SearchRectangleObject(2.0, 30.0, 4.0, 4.0, "7"));
        results = quadTree.getChildren()[QuadTree.NW_CHILD].getChildren()[QuadTree.SW_CHILD].getRectangleObjects();
        assertEquals(1, results.size());
        rectangleObject = results.get(0);
        assertEquals(Double.valueOf(2.0), rectangleObject.getX());
        assertEquals(Double.valueOf(30.0), rectangleObject.getY());
        assertEquals(Double.valueOf(4.0), rectangleObject.getW());
        assertEquals(Double.valueOf(4.0), rectangleObject.getH());
        
        //test moving object onto border of QuadTree
        results = quadTree.getRectangleObjects();
        assertEquals(0, results.size());
        quadTree.update(new SearchRectangleObject(2.0, 30.0, 4.0, 4.0, "7"), new SearchRectangleObject(2.0, 48.0, 4.0, 4.0, "7"));
        results = quadTree.getRectangleObjects();
        assertEquals(1, results.size());
        rectangleObject = results.get(0);
        assertEquals(Double.valueOf(2.0), rectangleObject.getX());
        assertEquals(Double.valueOf(48.0), rectangleObject.getY());
        assertEquals(Double.valueOf(4.0), rectangleObject.getW());
        assertEquals(Double.valueOf(4.0), rectangleObject.getH());
    }
    
    //TODO should probably just make an inner class to this called MockRectangleObject that will have helpful constructors


}
