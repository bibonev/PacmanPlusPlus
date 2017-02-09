package teamproject.graphics;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by boyanbonev on 05/02/2017.
 */
public class PositionVisualisationTest {

    @Test(expected = ExceptionInInitializerError.class)
    public void testInitialInitialization(){
        PositionVisualisation.initScreenDimensions();
    }

    @Test
    public void testConstructor(){
        PositionVisualisation test = new PositionVisualisation(5, 5);

        assertEquals(test.row, 5);
        assertEquals(test.column, 5);
        assertEquals(test.height, 0, 000.1);
        assertEquals(test.height, 0, 000.1);
        assertEquals(test.x, 0, 000.1);
        assertEquals(test.y, 0, 000.1);
    }
}
