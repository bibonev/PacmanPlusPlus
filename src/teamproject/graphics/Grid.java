package teamproject.graphics;

import teamproject.graphics.constants.CellSize;

/**
 * Created by boyanbonev on 05/02/2017.
 */
public class Grid {
    private static Cell [] [] grid;

    public Grid(){
        grid = new Cell [CellSize.Rows] [CellSize.Columns];
    }

    public static void addCell(Cell cell){
        grid[cell.position.row][cell.position.column] = cell;
    }

    public static Cell getCell(int row, int column){
        return grid [row][column];
    }
}
