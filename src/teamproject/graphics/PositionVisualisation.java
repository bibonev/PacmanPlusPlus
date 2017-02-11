package teamproject.graphics;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import teamproject.constants.CellSize;
import teamproject.constants.ScreenSize;
import teamproject.gamelogic.domain.Position;

/**
 * Created by boyanbonev on 03/02/2017.
 */
public class PositionVisualisation extends Position {
    private double pixelX;
    private double pixelY;

    private double width;
    private double height;

    /**
     * Initialize positionVisualisation by providing row and column
     * @param i - row
     * @param j - column
     */
    public PositionVisualisation(int i, int j){
        super(i, j);

        this.pixelX = (ScreenSize.Width/ CellSize.Columns)*j;
        this.pixelY = (ScreenSize.Height/CellSize.Rows)*i;

        this.width = ScreenSize.Width/CellSize.Rows;
        this.height = ScreenSize.Height/CellSize.Rows;
    }

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

    public double getPixelX() {
        return pixelX;
    }

    public double getPixelY() {
        return pixelY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
