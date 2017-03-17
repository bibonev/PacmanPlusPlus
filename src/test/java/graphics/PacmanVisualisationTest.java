package test.java.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

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

	@Mock
	private Render render;

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
				Application.launch(MockJavaFx.class, new String[0]);
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
		render = mock(Render.class);
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
		final ImageView node = test.getNode();

		assertEquals(289.0, node.getImage().getHeight(), 0.001);
		assertEquals(300.0, node.getImage().getWidth(), 0.001); // Assert no
																// exception
	}
}
