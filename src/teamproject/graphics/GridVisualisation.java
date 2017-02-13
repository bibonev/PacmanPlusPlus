package teamproject.graphics;

import teamproject.constants.CellSize;
import teamproject.gamelogic.domain.Cell;
import teamproject.gamelogic.domain.Map;

/**
 * Created by Boyan Bonev on 05/02/2017.
 */
public class GridVisualisation extends Map {
    private static CellVisualisation[] [] grid;

    /**
     * Initialize the grid
     */
    public GridVisualisation(){
        super();
        grid = new CellVisualisation[CellSize.Rows] [CellSize.Columns];
    }

    /**
     * Add cell on the grid
     * @param cell
     */
    public void addVisualCell(CellVisualisation cell){
        grid[cell.positionVisualisation.getRow()][cell.positionVisualisation.getColumn()] = cell;
    }

    /**
     * Get particular cell by searching it on row and column
     * @param row
     * @param column
     * @return CellVisualisation
     */
    public CellVisualisation getCell(int row, int column){
        return grid [row][column];
    }
}
