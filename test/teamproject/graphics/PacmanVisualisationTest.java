package teamproject.graphics;

import javafx.application.Application;
<<<<<<< Updated upstream
=======
import javafx.scene.Node;
>>>>>>> Stashed changes
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import teamproject.constants.CellState;
import teamproject.constants.CellType;
import teamproject.gamelogic.domain.Behaviour;

/**
 * Created by boyanbonev on 13/02/2017.
 */

public class PacmanVisualisationTest {
    private Behaviour testBehavior;
    private String testName;
<<<<<<< Updated upstream
    private MapVisualisation testGrid;

    @Mock
    private Render testMapV;
=======
    private GridVisualisation testGrid;

    @Mock
    private MapVisualisation testMapV;
>>>>>>> Stashed changes

    private PacmanVisualisation test;

    public static class MockJavaFx extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {

        }
    }

    @BeforeClass
    public static void initJFX() throws InterruptedException {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(MockJavaFx.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
        Thread.sleep(500);
    }

    @Before
    public void setUp() throws NullPointerException {
        MockitoAnnotations.initMocks(this);
        this.testBehavior = new BasicBehaviour(Behaviour.Type.DEFAULT);
        this.testName = "Test Pacman";
<<<<<<< Updated upstream
        this.testGrid = new MapVisualisation();
        this.testMapV = mock(Render.class);
=======
        this.testGrid = new GridVisualisation();
        this.testMapV = mock(MapVisualisation.class);
>>>>>>> Stashed changes
    }

    @Test
    public void testInitialization(){
        try{
            this.test = new PacmanVisualisation(testBehavior, testName, testGrid, testMapV);
        } catch (NullPointerException ex){

        }

        assertTrue(true);
    }

    @Test
    public void testPosition(){
        this.test = new PacmanVisualisation(testBehavior, testName, testGrid, testMapV);

        assertEquals(1, this.test.getPosition().getRow());
        assertEquals(1, this.test.getPosition().getColumn());
    }

    @Test
    public void testMovement(){
        doNothing().when(this.testMapV).redrawMap();

        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(i == 0 && j == 0){
                    testGrid.addVisualCell(new CellVisualisation(CellType.NORMAL, CellState.OBSTACLE,
                            new PositionVisualisation(i, j)));
                } else{
                    testGrid.addVisualCell(new CellVisualisation(CellType.NORMAL, CellState.FOOD,
                            new PositionVisualisation(i, j)));
                }
            }
        }

        this.test = new PacmanVisualisation(testBehavior, testName, testGrid, testMapV);

        assertEquals(true, this.test.moveUp());
        assertEquals(false, this.test.moveLeft());
        assertEquals(true, this.test.moveRight());
        assertEquals(true, this.test.moveDown());
    }

    @Test
    public void testNode(){
        this.test = new PacmanVisualisation(testBehavior, testName, testGrid, testMapV);
        ImageView node = ((javafx.scene.image.ImageView) this.test.getNode());

        assertEquals(53.0, node.getImage().getHeight(), 0.001);
        assertEquals(50.0, node.getImage().getWidth(), 0.001); //Assert no exception
    }
}
