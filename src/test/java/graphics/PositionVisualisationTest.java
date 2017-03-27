package test.java.graphics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.java.graphics.PositionVisualisation;

/**
 * Created by boyanbonev on 05/02/2017.
 */
public class PositionVisualisationTest {

	@Test(expected = ExceptionInInitializerError.class)
	public void testInitialInitialization() {
		PositionVisualisation.initScreenDimensions();
	}

	@Test
	public void testConstructor() {
		final PositionVisualisation test = new PositionVisualisation(5, 5);

		assertEquals(test.getRow(), 5);
		assertEquals(test.getColumn(), 5);
		assertEquals(test.getWidth(), 0, 1);
		assertEquals(test.getHeight(), 0, 1);
		assertEquals(test.getPixelX(), 0, 1);
		assertEquals(test.getPixelY(), 0, 1);
	}
}
