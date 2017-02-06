package teamproject.graphics;

import static org.junit.Assert.*;
import org.junit.Test;
import teamproject.graphics.constants.BoardState;

/**
 * Created by boyanbonev on 06/02/2017.
 */
public class GridTest {

    @Test
    public void testGettingAddingCell(){
        Grid test = new Grid();
        Cell testCell = new Cell(new Position(0,0), BoardState.EMPTY);
        test.addCell(testCell);

        assertEquals(Grid.getCell(0,0).position.x,0,0.0001);
        assertEquals(Grid.getCell(0,0).position.y,0,0.0001);
        assertEquals(Grid.getCell(0,0).position.height,0,0.0001);
        assertEquals(Grid.getCell(0,0).position.width,0,0.0001);
        assertEquals(Grid.getCell(0,0).position.row,0);
        assertEquals(Grid.getCell(0,0).position.column, 0);
    }
}
