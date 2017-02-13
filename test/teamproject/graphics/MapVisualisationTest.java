package teamproject.graphics;

import static org.junit.Assert.*;
import org.junit.Test;
import teamproject.constants.CellState;
import teamproject.constants.CellType;


/**
 * Created by boyanbonev on 06/02/2017.
 */
public class MapVisualisationTest {

    @Test
    public void testGettingAddingCell(){
        MapVisualisation test = new MapVisualisation();
        CellVisualisation testCell = new CellVisualisation(CellType.NORMAL, CellState.EMPTY, new PositionVisualisation(0,0));
        test.addVisualCell(testCell);

        assertEquals(test.getCell(0,0).positionVisualisation.getPixelX(),0,0.0001);
        assertEquals(test.getCell(0,0).positionVisualisation.getPixelY(),0,0.0001);
        assertEquals(test.getCell(0,0).positionVisualisation.getHeight(),0,0.0001);
        assertEquals(test.getCell(0,0).positionVisualisation.getWidth(),0,0.0001);
        assertEquals(test.getCell(0,0).positionVisualisation.getRow(),0);
        assertEquals(test.getCell(0,0).positionVisualisation.getColumn(), 0);
    }
}
