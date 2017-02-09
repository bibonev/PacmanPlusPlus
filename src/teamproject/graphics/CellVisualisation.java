package teamproject.graphics;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import teamproject.constants.CellType;
import teamproject.constants.Colors;
import teamproject.constants.Images;
import teamproject.constants.CellState;
import teamproject.gamelogic.domain.Cell;

/**
 * Created by Boyan Bonev on 05/02/2017.
 */
public class CellVisualisation extends Cell{
    public PositionVisualisation positionVisualisation;

    private CellType type;
    private CellState state;
    private Node node;

    /**
     * Initialization of the CellVisualisation by providing its positionVisualisation and its state
     * @param positionVisualisation
     * @param state
     * @param type
     */
    public CellVisualisation(CellType type, CellState state, PositionVisualisation positionVisualisation){
        super(type, state, positionVisualisation);
        this.positionVisualisation = positionVisualisation;
        this.state = state;
        this.type = type;
    }

    /**
     * Get the node for the particular cell depending on its state
     * @return Node
     */
    public Node getNode(){
        if (this.state == CellState.FOOD){
            this.node = new Circle(positionVisualisation.x+ positionVisualisation.width/2, positionVisualisation.y+ positionVisualisation.height/2, positionVisualisation.width/8);
            ((Circle)node).setFill(Colors.CellFoodColor);
        } else if (this.state == CellState.OBSTACLE){
            Images.Border =  new ImageView("border.jpg");

            Images.Border.setFitWidth(positionVisualisation.width);
            Images.Border.setFitHeight(positionVisualisation.height);

            Images.Border.setX(positionVisualisation.x+ positionVisualisation.width/2 - positionVisualisation.width/2);
            Images.Border.setY(positionVisualisation.y+ positionVisualisation.height/2 - positionVisualisation.height/2);

            this.node= Images.Border;
        }
        else if (this.state == CellState.EMPTY){
            this.node = new Rectangle(positionVisualisation.x, positionVisualisation.y, positionVisualisation.width, positionVisualisation.height);
            ((Rectangle)node).setFill(Colors.CellEmptyColor);
        }

        return node;
    }
}
