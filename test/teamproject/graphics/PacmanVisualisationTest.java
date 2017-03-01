package teamproject.graphics;

import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import teamproject.gamelogic.domain.*;

/**
 * Created by boyanbonev on 13/02/2017.
 */

public class PacmanVisualisationTest {
    private Player testPlayer;

    @Mock
    private Render render;

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
        this.testPlayer = new LocalPlayer("TestPlayer");
        this.testPlayer.setPosition(new Position(1, 1));
        this.render = mock(Render.class);
    }

    @Test
    public void testInitialization(){
        try{
            this.test = new PacmanVisualisation(testPlayer);
        } catch (NullPointerException ex){

        }

        assertTrue(true);
    }

    @Test
    public void testNode(){
        this.test = new PacmanVisualisation(testPlayer);
        ImageView node = ((javafx.scene.image.ImageView) this.test.getNode());

        assertEquals(53.0, node.getImage().getHeight(), 0.001);
        assertEquals(50.0, node.getImage().getWidth(), 0.001); //Assert no exception
    }
}
