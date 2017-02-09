package teamproject.graphics;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by boyanbonev on 05/02/2017.
 */
public class PositionTest {

    @Test(expected = ExceptionInInitializerError.class)
    public void testInitialInitialization(){
        Position.initScreenDimensions();
    }

    @Test
    public void testConstructor(){
        Position test = new Position(5, 5);

        assertEquals(test.row, 5);
        assertEquals(test.column, 5);
        assertEquals(test.height, 0, 000.1);
        assertEquals(test.height, 0, 000.1);
        assertEquals(test.x, 0, 000.1);
        assertEquals(test.y, 0, 000.1);
    }
}
