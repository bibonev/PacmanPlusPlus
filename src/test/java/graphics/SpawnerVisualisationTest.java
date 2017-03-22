package test.java.graphics;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.constants.Colors;
import main.java.gamelogic.domain.LocalPlayer;
import main.java.gamelogic.domain.Player;
import main.java.gamelogic.domain.Position;
import main.java.gamelogic.domain.Spawner;
import main.java.graphics.SpawnerVisualisation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by boyanbonev on 21/03/2017.
 */
public class SpawnerVisualisationTest {
    private SpawnerVisualisation spawnerVisualisation;

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
        Player testPlayer = new LocalPlayer("test player");
        testPlayer.setPosition(new Position(5, 5));
        Spawner spawner = new Spawner(3, testPlayer, Spawner.SpawnerColor.CYAN);
        spawner.setPosition(testPlayer.getPosition());
        spawnerVisualisation = new SpawnerVisualisation(spawner);
    }

    @Test
    public void testInitialization(){
        assertTrue(true);
    }

    @Test
    public void testNode(){
        ImageView node = this.spawnerVisualisation.getNode();

        assertTrue(((ImageView)(node)).getImage().impl_getUrl().contains("num3"));
        assertEquals(53.0, ((ImageView)(node)).getImage().getHeight(), 0.001);
        assertEquals(50.0, ((ImageView)(node)).getImage().getWidth(), 0.001);
    }
}
