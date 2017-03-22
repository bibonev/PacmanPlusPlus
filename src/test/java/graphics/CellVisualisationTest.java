package test.java.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.gamelogic.domain.Position;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.java.constants.CellState;
import main.java.constants.Colors;
import main.java.gamelogic.domain.Cell;
import main.java.graphics.CellVisualisation;
import main.java.graphics.PositionVisualisation;
import test.java.gamelogic.random.Randoms;

/**
 * Created by boyanbonev on 05/02/2017.
 */
public class CellVisualisationTest {
	private Position testPosition;
	private Cell testCell;
	private CellVisualisation test;

    public static class MockJavaFx extends Application {
        @Override
        public void start(final Stage primaryStage) throws Exception {

        }
    }

    @BeforeClass
    public static void initJFX() throws InterruptedException {
        final Thread t = new Thread("JavaFX Init Thread") {
            @Override
            public void run() {
                Application.launch(PacmanVisualisationTest.MockJavaFx.class);
            }
        };
        t.setDaemon(true);
        t.start();
        Thread.sleep(500);
    }

	@Before
	public void initializeFields() {
        PositionVisualisation.initScreenDimensions();

		testPosition = new Position(5,5);
		testCell = new Cell(CellState.EMPTY, testPosition);
		test = new CellVisualisation(testCell);
	}

	@Test
	public void testCell() {
		assertEquals(test.getCell(), testCell);
	}

	@Test
	public void testEmptyNode() {
		assertEquals(test.getNode().toString().contains("Rectangle"), true);

		final Rectangle testRectangle = (Rectangle) test.getNode();

		assertEquals( 266, testRectangle.getX(), 1);
		assertEquals( 183, testRectangle.getY(), 1);
		assertEquals( 36.6,testRectangle.getHeight(), 1);
		assertEquals(53.3, testRectangle.getWidth(), 1);
		assertEquals( Colors.CellEmptyColor, testRectangle.getFill());
	}

	@Test
	public void testFoodNode() {
		test = new CellVisualisation(new Cell(CellState.FOOD, testPosition));
		assertEquals(test.getNode().toString().contains("Circle"), true);

		final Circle testCircle = (Circle) test.getNode();

		assertEquals( Colors.CellFoodColor, testCircle.getFill());
		assertEquals( 293.3, testCircle.getCenterX(),1);
		assertEquals( 201.6, testCircle.getCenterY(),1);
		assertEquals( 6.60, testCircle.getRadius(),1);
	}


    @Test
    public void testObstacleNode() {
        test = new CellVisualisation(new Cell(CellState.OBSTACLE, testPosition));
        assertEquals(test.getNode().toString().contains("ImageView"), true);

        final ImageView testImage = (ImageView) test.getNode();

        assertTrue(testImage.getImage().impl_getUrl().contains("border"));
        assertEquals(36.6, testImage.getFitHeight(), 1);
        assertEquals(36.6, testImage.getFitHeight(), 1);
        assertEquals(266.0, testImage.getX(), 1);
        assertEquals(183.0, testImage.getY(), 1);
    }

    @Test
    public void testLaserNode() {
        test = new CellVisualisation(new Cell(CellState.LASER, testPosition));
        assertEquals(test.getNode().toString().contains("Rectangle"), true);

        final Rectangle testLaser = (Rectangle) test.getNode();

        assertEquals( 0, testLaser.getX(), 1);
        assertEquals( 0, testLaser.getY(), 1);
        assertEquals( 9,testLaser.getHeight(), 1);
        assertEquals(13, testLaser.getWidth(), 1);
        assertEquals(20, testLaser.getArcWidth(), 1);
        assertEquals(20, testLaser.getArcHeight(), 1);
        assertEquals( Colors.LaserColor, testLaser.getFill());

    }
}
