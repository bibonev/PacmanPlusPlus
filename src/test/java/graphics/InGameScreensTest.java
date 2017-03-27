package test.java.graphics;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.java.constants.GameOutcome;
import main.java.constants.GameOutcomeType;
import main.java.gamelogic.domain.LocalPlayer;
import main.java.gamelogic.domain.Player;
import main.java.graphics.InGameScreens;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by boyanbonev on 21/03/2017.
 */
public class InGameScreensTest {
    private InGameScreens inGameScreens;

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
        inGameScreens = new InGameScreens();
    }

    @Test
    public void testInitialization() {
        assertTrue(true);
    }

    @Test
    public void testPlayerRespawnWindow(){
        Node node = inGameScreens.getPlayerRespawnWindow("test", true);
        Node nodeRejoinFalse = inGameScreens.getPlayerRespawnWindow("testRejoinFalse", false);

        assertEquals(node.getClass().getTypeName(), "javafx.scene.layout.StackPane");
        assertEquals(((StackPane)(node)).getChildren().size(), 3);
        assertEquals(((StackPane)(node)).getChildren().get(0).getClass().getTypeName(), "javafx.scene.control.Label");
        assertEquals(((StackPane)(node)).getChildren().get(1).getClass().getTypeName(), "javafx.scene.control.Label");
        assertEquals(((StackPane)(node)).getChildren().get(2).getClass().getTypeName(), "javafx.scene.control.Label");

        assertEquals("You died!",((javafx.scene.control.Label)(((StackPane)(node)).getChildren().get(0))).getText());
        assertEquals("test",((javafx.scene.control.Label)(((StackPane)(node)).getChildren().get(1))).getText());
        assertEquals("Press SPACE to respawn",((javafx.scene.control.Label)(((StackPane)(node)).getChildren().get(2))).getText());
        assertEquals("You have ran out of lives",((javafx.scene.control.Label)(((StackPane)(nodeRejoinFalse)).getChildren().get(2))).getText());

        assertEquals("-fx-background-color: rgba(0, 0, 0, 0.7)", (node).getStyle());
    }

    @Test
    public void testPauseGameScreen(){
        Node node = inGameScreens.pauseGameScreen();

        assertEquals(node.getClass().getTypeName(), "javafx.scene.layout.StackPane");
        assertEquals(((StackPane)(node)).getChildren().size(), 4);
        assertEquals(((StackPane)(node)).getChildren().get(0).getClass().getTypeName(), "javafx.scene.control.Label");
        assertEquals(((StackPane)(node)).getChildren().get(1).getClass().getTypeName(), "javafx.scene.control.Label");
        assertEquals(((StackPane)(node)).getChildren().get(2).getClass().getTypeName(), "javafx.scene.control.Label");
        assertEquals(((StackPane)(node)).getChildren().get(3).getClass().getTypeName(), "javafx.scene.control.Label");

        assertEquals("Paused",((javafx.scene.control.Label)(((StackPane)(node)).getChildren().get(0))).getText());
        assertEquals("* Press ESC to resume the game",((javafx.scene.control.Label)(((StackPane)(node)).getChildren().get(1))).getText());
        assertEquals("* Press SPACE to go to settings",((javafx.scene.control.Label)(((StackPane)(node)).getChildren().get(2))).getText());
        assertEquals("* Press Q to go back at the menu",((javafx.scene.control.Label)(((StackPane)(node)).getChildren().get(3))).getText());

        assertEquals("-fx-background-color: rgba(0, 0, 0, 0.7)", (node).getStyle());
    }


    @Test
    public void testEndGameScreen(){
        Player winner2 = new LocalPlayer("Other player");
        winner2.setID(0);
        Player winner = new LocalPlayer("My player");
        winner.setID(0);
        GameOutcome outcomeOtherPlayerWon = new GameOutcome(GameOutcomeType.PLAYER_WON, winner2);
        GameOutcome outcomeMyPlayerWon = new GameOutcome(GameOutcomeType.PLAYER_WON, winner);

        Node nodeLoose = inGameScreens.endGameScreen(0, new GameOutcome(GameOutcomeType.GHOSTS_WON));
        Node nodeWin = inGameScreens.endGameScreen(0, outcomeMyPlayerWon);
        Node nodeWin2 = inGameScreens.endGameScreen(1, outcomeOtherPlayerWon);
        Node nodeTie = inGameScreens.endGameScreen(0, new GameOutcome(GameOutcomeType.TIE));

        assertEquals(nodeWin.getClass().getTypeName(), "javafx.scene.layout.StackPane");
        assertEquals(((StackPane)(nodeWin)).getChildren().size(), 3);
        assertEquals(((StackPane)(nodeWin)).getChildren().get(0).getClass().getTypeName(), "javafx.scene.control.Label");
        assertEquals(((StackPane)(nodeWin)).getChildren().get(1).getClass().getTypeName(), "javafx.scene.control.Label");
        assertEquals(((StackPane)(nodeWin)).getChildren().get(2).getClass().getTypeName(), "javafx.scene.control.Label");

        assertEquals("* Press ESC to go back at the menu",((javafx.scene.control.Label)(((StackPane)(nodeWin)).getChildren().get(1))).getText());
        assertEquals("* Press SPACE to replay",((javafx.scene.control.Label)(((StackPane)(nodeWin)).getChildren().get(2))).getText());
        assertEquals("Wohoo, you won!",((javafx.scene.control.Label)(((StackPane)(nodeWin)).getChildren().get(0))).getText());
        assertEquals("Damn, Other player won this time.",((javafx.scene.control.Label)(((StackPane)(nodeWin2)).getChildren().get(0))).getText());
        assertEquals("Damn! The ghosts won this time...",((javafx.scene.control.Label)(((StackPane)(nodeLoose)).getChildren().get(0))).getText());
        assertEquals("It's a tie! Nicely done.",((javafx.scene.control.Label)(((StackPane)(nodeTie)).getChildren().get(0))).getText());

        assertEquals("-fx-background-color: rgba(0, 0, 0, 0.7)", (nodeWin).getStyle());
    }
}
