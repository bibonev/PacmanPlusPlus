package teamproject.graphics;

import static org.junit.Assert.*;
import org.junit.Test;
import teamproject.constants.CellState;
import teamproject.constants.CellType;


/**
 * Created by boyanbonev on 06/02/2017.
 */
public class GridTest {

    @Test
    public void testGettingAddingCell(){
        Grid test = new Grid();
        CellVisualisation testCell = new CellVisualisation(CellType.NORMAL, CellState.EMPTY, new PositionVisualisation(0,0));
        test.addVisualCell(testCell);

        assertEquals(test.getCell(0,0).positionVisualisation.x,0,0.0001);
        assertEquals(test.getCell(0,0).positionVisualisation.y,0,0.0001);
        assertEquals(test.getCell(0,0).positionVisualisation.height,0,0.0001);
        assertEquals(test.getCell(0,0).positionVisualisation.width,0,0.0001);
        assertEquals(test.getCell(0,0).positionVisualisation.row,0);
        assertEquals(test.getCell(0,0).positionVisualisation.column, 0);
    }
}
