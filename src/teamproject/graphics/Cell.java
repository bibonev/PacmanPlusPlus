package teamproject.graphics;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import teamproject.graphics.constants.BoardState;
import teamproject.graphics.constants.Colors;
import teamproject.graphics.constants.Images;

/**
 * Created by boyanbonev on 05/02/2017.
 */
public class Cell {
    public Position position;

    private BoardState state;
    private Node node;

    public Cell(Position position, BoardState state){
        this.position = position;
        this.state = state;
    }

    public Node getNode(){
        if (this.state == BoardState.FOOD){
            this.node = new Circle(position.x+position.width/2,position.y+position.height/2,position.width/8);
            ((Circle)node).setFill(Colors.CellFoodColor);
        } else if (this.state == BoardState.OBSTACLE){
            Images.Border =  new ImageView("border.jpg");

            Images.Border.setFitWidth(position.width);
            Images.Border.setFitHeight(position.height);

            Images.Border.setX(position.x+position.width/2 - position.width/2);
            Images.Border.setY(position.y+position.height/2 - position.height/2);

            this.node= Images.Border;
        }
        else if (this.state == BoardState.EMPTY){
            this.node = new Rectangle(position.x,position.y,position.width,position.height);
            ((Rectangle)node).setFill(Colors.CellEmptyColor);
        }

        return node;
    }

    public BoardState getType() {
        return this.state;
    }

    public void setType(BoardState state){
        this.state = state;
    }
}
