package teamproject.graphics;

import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import teamproject.ai.DefaultBehaviour;
import teamproject.constants.CellState;
import teamproject.constants.CellType;
import teamproject.gamelogic.domain.*;

import java.util.HashMap;
import java.util.Optional;

/**
 * Created by boyanbonev on 13/02/2017.
 */

public class PacmanVisualisationTest {
    private Player testPlayer;
    private String testName;
    private MapVisualisation testGrid;

    @Mock
    private Render testMapV;

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
        this.testPlayer = new Player(Optional.empty(), "TestPlayer");
        this.testName = "Test Pacman";
        this.testGrid = new MapVisualisation();
        this.testMapV = mock(Render.class);
    }

    @Test
    public void testInitialization(){
        try{
            this.test = new PacmanVisualisation(testPlayer, testGrid, testMapV);
        } catch (NullPointerException ex){

        }

        assertTrue(true);
    }

    @Test
    public void testPosition(){
        this.test = new PacmanVisualisation(testPlayer, testGrid, testMapV);

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

        this.test = new PacmanVisualisation(testPlayer, testGrid, testMapV);

        assertEquals(true, this.test.moveUp());
        assertEquals(false, this.test.moveLeft());
        assertEquals(true, this.test.moveRight());
        assertEquals(true, this.test.moveDown());
    }

    @Test
    public void testNode(){
        this.test = new PacmanVisualisation(testPlayer, testGrid, testMapV);
        ImageView node = ((javafx.scene.image.ImageView) this.test.getNode());

        assertEquals(53.0, node.getImage().getHeight(), 0.001);
        assertEquals(50.0, node.getImage().getWidth(), 0.001); //Assert no exception
    }
}
