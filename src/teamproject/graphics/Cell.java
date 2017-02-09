package teamproject.graphics;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import teamproject.constants.Colors;
import teamproject.constants.Images;
import teamproject.constants.CellState;

/**
 * Created by Boyan Bonev on 05/02/2017.
 */
public class Cell {
    public Position position;

    private CellState state;
    private Node node;

    /**
     * Initialization of the Cell by providing its position and its state
     * @param position
     * @param state
     */
    public Cell(Position position, CellState state){
        this.position = position;
        this.state = state;
    }

    /**
     * Get the node for the particular cell depending on its state
     * @return Node
     */
    public Node getNode(){
        if (this.state == CellState.FOOD){
            this.node = new Circle(position.x+position.width/2,position.y+position.height/2,position.width/8);
            ((Circle)node).setFill(Colors.CellFoodColor);
        } else if (this.state == CellState.OBSTACLE){
            Images.Border =  new ImageView("border.jpg");

            Images.Border.setFitWidth(position.width);
            Images.Border.setFitHeight(position.height);

            Images.Border.setX(position.x+position.width/2 - position.width/2);
            Images.Border.setY(position.y+position.height/2 - position.height/2);

            this.node= Images.Border;
        }
        else if (this.state == CellState.EMPTY){
            this.node = new Rectangle(position.x,position.y,position.width,position.height);
            ((Rectangle)node).setFill(Colors.CellEmptyColor);
        }

        return node;
    }

    /**
     * Get the state of the cell
     * @return BoardState
     */
    public CellState getState() {
        return this.state;
    }

    /**
     * Set different state
     * @param state
     */
    public void setState(CellState state){
        this.state = state;
    }
}
