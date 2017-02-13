package teamproject.graphics;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.junit.Test;
import static org.junit.Assert.*;

import teamproject.constants.CellType;
import teamproject.constants.Colors;
import teamproject.constants.CellState;

/**
 * Created by boyanbonev on 05/02/2017.
 */
public class CellVisualisationTest {

    @Test
    public void testConstructor(){
        PositionVisualisation testPositionVisualisation = new PositionVisualisation(5, 5);
        CellState testBoardState = CellState.EMPTY;
        CellType testTye = CellType.NORMAL;
        CellVisualisation test = new CellVisualisation(testTye, testBoardState, testPositionVisualisation);

        assertEquals(test.positionVisualisation, testPositionVisualisation);
    }

    @Test
    public void testBoardState(){
        PositionVisualisation testPositionVisualisation = new PositionVisualisation(5, 5);
        CellState testBoardState = CellState.EMPTY;
        CellType testTye = CellType.NORMAL;
        CellVisualisation test = new CellVisualisation(testTye, testBoardState, testPositionVisualisation);

        assertEquals(test.getState(), CellState.EMPTY);
    }

    @Test
    public void testRectangleNode(){
        PositionVisualisation testPositionVisualisation = new PositionVisualisation(5, 5);
        CellState testBoardState = CellState.EMPTY;
        CellType testTye = CellType.NORMAL;
        CellVisualisation test = new CellVisualisation(testTye, testBoardState, testPositionVisualisation);

        assertEquals(test.getNode().toString().contains("Rectangle"), true);

        Rectangle testRectangle = (Rectangle) test.getNode();

        assertEquals(testRectangle.getX(), 0, 0.0001);
        assertEquals(testRectangle.getY(), 0, 0.0001);
        assertEquals(testRectangle.getHeight(), 0, 0.0001);
        assertEquals(testRectangle.getWidth(), 0, 0.0001);
        assertEquals(testRectangle.getFill(), Colors.CellEmptyColor);
    }

    @Test
    public void testCircleNode(){
        PositionVisualisation testPositionVisualisation = new PositionVisualisation(5, 5);
        CellState testBoardState = CellState.FOOD;
        CellType testTye = CellType.NORMAL;
        CellVisualisation test = new CellVisualisation(testTye, testBoardState, testPositionVisualisation);

        assertEquals(test.getNode().toString().contains("Circle"), true);

        Circle testCircle = (Circle) test.getNode();

        assertEquals(testCircle.getFill(), Colors.CellFoodColor);
        assertEquals(testCircle.getCenterX(), 0, 0.0001);
        assertEquals(testCircle.getCenterY(), 0, 0.0001);
        assertEquals(testCircle.getRadius(),  0, 0.0001);
    }
}
