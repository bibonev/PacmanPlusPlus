package test.java.graphics;

import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.gamelogic.domain.Position;
import main.java.graphics.GhostVisualisation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by boyanbonev on 21/03/2017.
 */
public class GhostVisualisationTest {
    private GhostVisualisation testGhost;

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
    public void setUp() throws NullPointerException {
        Position testPosition = new Position(5, 5);
        testGhost = new GhostVisualisation(testPosition);
    }

    @Test
    public void testInitialization() {
        assertTrue(true);
    }

    @Test
    public void testNode() {
        ImageView node = testGhost.getNode();


        assertTrue(((ImageView)(node)).getImage().impl_getUrl().contains("ghost"));
        assertEquals(50, node.getImage().getHeight(), 0.001);
        assertEquals(50.0, node.getImage().getWidth(), 0.001);
    }
}
