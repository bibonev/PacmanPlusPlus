package test.java.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import main.java.graphics.PositionVisualisation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.gamelogic.domain.LocalPlayer;
import main.java.gamelogic.domain.Player;
import main.java.gamelogic.domain.Position;
import main.java.graphics.PacmanVisualisation;
import main.java.graphics.Render;

/**
 * Created by boyanbonev on 13/02/2017.
 */

public class PacmanVisualisationTest {
	private Player testPlayer;

	private PacmanVisualisation test;

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
				Application.launch(MockJavaFx.class);
			}
		};
		t.setDaemon(true);
		t.start();
		Thread.sleep(500);
	}

	@Before
	public void setUp() throws NullPointerException {
		testPlayer = new LocalPlayer("TestPlayer");
		testPlayer.setPosition(new Position(1, 1));
	}

	@Test
	public void testInitialization() {
		try {
			test = new PacmanVisualisation(testPlayer);
		} catch (final NullPointerException ex) {

		}

		assertTrue(true);
	}

	@Test
	public void testNode() {
		test = new PacmanVisualisation(testPlayer);
		final Node node = test.getNode();

		assertTrue(((ImageView)(node)).getImage().impl_getUrl().contains("pacman"));
		assertEquals(289.0, ((ImageView)(node)).getImage().getHeight(), 0.001);
		assertEquals(300.0, ((ImageView)(node)).getImage().getWidth(), 0.001);
	}

	@Test
	public void testShieldNode() {
		PositionVisualisation.initScreenDimensions();

		testPlayer.setShield(10);
		testPlayer.setAngle(90);
		test = new PacmanVisualisation(testPlayer);

		final Node node = test.getNode();

		assertEquals(node.getClass().getTypeName(), "javafx.scene.layout.StackPane");
		assertEquals(((StackPane)(node)).getChildren().size(), 2);
		assertEquals(((StackPane)(node)).getChildren().get(0).getClass().getTypeName(), "javafx.scene.image.ImageView");
		assertEquals(((StackPane)(node)).getChildren().get(1).getClass().getTypeName(), "javafx.scene.shape.Circle");

		assertEquals(0,((Circle)(((StackPane)(node)).getChildren().get(1))).getTranslateX(), 1);
		assertEquals(0,((Circle)(((StackPane)(node)).getChildren().get(1))).getTranslateY(), 1);
		assertEquals(13,((Circle)(((StackPane)(node)).getChildren().get(1))).getRadius(), 1);

		assertEquals(61, ((StackPane)(node)).getTranslateX(), 1);
		assertEquals(36, ((StackPane)(node)).getTranslateY(), 1);
		assertEquals(36, ((StackPane)(node)).getRotate(), 90);
	}
}
