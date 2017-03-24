package test.java.graphics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import main.java.gamelogic.core.GameLogic;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.World;
import main.java.graphics.Render;
import main.java.ui.GameUI;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by boyanbonev on 21/03/2017.
 */
public class RenderTest {
    @InjectMocks
    private Render render;

    @Mock
    private GameLogic mockedGameLogic;
    @Mock
    private GameUI mockedGameUI;
    @Mock
    private Game mockedGame;
    @Mock
    private World mockedWorld;
    @Mock
    private Map mockedMap;

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
        mockedGame = mock(Game.class);
        mockedGameLogic = mock(GameLogic.class);
        mockedGameUI = mock(GameUI.class);
        mockedWorld = mock(World.class);
        mockedMap = mock(Map.class);

        render = mock(Render.class);

        MockitoAnnotations.initMocks(this);

        doCallRealMethod().when(render).setupWorld();
        when(mockedGame.getWorld()).thenReturn(mockedWorld);
        when(mockedWorld.getMap()).thenReturn(mockedMap);
        when(mockedMap.getMapSize()).thenReturn(15);
        doNothing().when(mockedGameLogic).readyToStart();
    }

    @Test
    public void testInitialization() {
        assertTrue(true);
    }

    @Test
    public void testSetupOfWorld() {
        Scene scene = render.setupWorld();

        assertEquals(scene.getRoot().getChildrenUnmodifiable().size(), 2);
        assertEquals(((BorderPane) scene.getRoot()).getChildren().get(0).getClass().getSimpleName(), "Pane");
        assertEquals(((BorderPane) scene.getRoot()).getChildren().get(1).getClass().getSimpleName(), "StackPane");
    }
}
