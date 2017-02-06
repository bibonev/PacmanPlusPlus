package teamproject.graphics;

import teamproject.graphics.constants.CellSize;

/**
 * Created by Boyan Bonev on 05/02/2017.
 */
public class Grid {
    private static Cell [] [] grid;

    /**
     * Initialize the grid
     */
    public Grid(){
        grid = new Cell [CellSize.Rows] [CellSize.Columns];
    }

    /**
     * Add cell on the grid
     * @param cell
     */
    public static void addCell(Cell cell){
        grid[cell.position.row][cell.position.column] = cell;
    }

    /**
     * Get particular cell by searching it on row and column
     * @param row
     * @param column
     * @return Cell
     */
    public static Cell getCell(int row, int column){
        return grid [row][column];
    }
}
