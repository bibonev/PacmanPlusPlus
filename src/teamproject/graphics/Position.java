package teamproject.graphics;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import teamproject.graphics.constants.CellSize;
import teamproject.graphics.constants.ScreenSize;

/**
 * Created by boyanbonev on 03/02/2017.
 */
public class Position {
    double x;
    double y;

    double width;
    double height;

    int row;
    int column;

    /**
     * Initialize initial screen dimension
     */
    public static void initScreenDimensions(){
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        ScreenSize.Width = visualBounds.getWidth();
        ScreenSize.Height = visualBounds.getHeight();

        // Test screen size
        // TODO: Remove the test screen size, or add it as a default one
        ScreenSize.Width = 600;
        ScreenSize.Height = 350;
    }

    /**
     * Initialize position by providing row and column
     * @param i - row
     * @param j - column
     */
    public Position(int i, int j){
        this.row = i;
        this.column = j;

        this.x = (ScreenSize.Width/ CellSize.Columns)*j;
        this.y = (ScreenSize.Height/CellSize.Rows)*i;

        this.width = ScreenSize.Width/CellSize.Rows;
        this.height = ScreenSize.Height/CellSize.Rows;
    }
}
