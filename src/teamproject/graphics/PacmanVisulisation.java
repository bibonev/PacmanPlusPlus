package teamproject.graphics;

import javafx.scene.image.ImageView;
import teamproject.constants.CellState;
import teamproject.constants.Images;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Pacman;

import javafx.scene.Node;

/**
 * Created by boyanbonev on 11/02/2017.
 */
public class PacmanVisulisation extends Pacman {

    private PositionVisualisation position;
    private GridVisualisation grid;
    private Node node;

    public PacmanVisulisation(Behaviour behaviour, String name, GridVisualisation grid) {
        super(behaviour, name);

        this.position = new PositionVisualisation(1,1);
        this.grid = grid;
        Images.PacMan = new ImageView("pacman.png");
    }

    public boolean moveUp(){

        if (grid.getCell(position.getRow()-1, position.getColumn()).getState() == CellState.OBSTACLE)
            return false;

        Images.PacMan.setRotate(90+180);

        position = new PositionVisualisation(position.getRow()-1, position.getColumn());
        if (grid.getCell(position.getRow(), position.getColumn()).getState() == CellState.FOOD){
            grid.getCell(position.getRow(), position.getColumn()).setState(CellState.EMPTY);
        }

        //MapGenerator.redrawMap();

        return true;
    }

    public boolean moveDown(){

        if (grid.getCell(position.getRow()+1, position.getColumn()).getState() == CellState.OBSTACLE)
            return false;

        Images.PacMan.setRotate(90);

        position = new PositionVisualisation(position.getRow()+1, position.getColumn());
        if (grid.getCell(position.getRow(), position.getColumn()).getState() == CellState.FOOD){
            grid.getCell(position.getRow(), position.getColumn()).setState(CellState.EMPTY);
        }

        //MapGenerator.redrawMap();

        return true;
    }

    public boolean moveLeft(){

        if (grid.getCell(position.getRow(), position.getColumn()-1).getState() == CellState.OBSTACLE)
            return false;

        Images.PacMan.setRotate(-180);

        position = new PositionVisualisation(position.getRow(), position.getColumn()-1);
        if (grid.getCell(position.getRow(), position.getColumn()).getState() == CellState.FOOD){
            grid.getCell(position.getRow(), position.getColumn()).setState(CellState.EMPTY);
        }

        //MapGenerator.redrawMap();

        return true;
    }

    public boolean moveRight(){

        if (grid.getCell(position.getRow(), position.getColumn()+1).getState() == CellState.OBSTACLE)
            return false;

        Images.PacMan.setRotate(0);

        position = new PositionVisualisation(position.getRow(), position.getColumn()+1);
        if (grid.getCell(position.getRow(), position.getColumn()).getState() == CellState.FOOD){
            grid.getCell(position.getRow(), position.getColumn()).setState(CellState.EMPTY);
        }

        //MapGenerator.redrawMap();

        return true;
    }

    public Node getNode(){
        double min = position.getHeight();
        if (position.getWidth() < position.getHeight())
            min = position.getWidth();

        Images.PacMan.setFitWidth(min);
        Images.PacMan.setFitHeight(min);

        Images.PacMan.setX(position.getRow()+position.getWidth()/2 - min/2);
        Images.PacMan.setY(position.getColumn()+position.getHeight()/2 - min/2);

        node = Images.PacMan;

        return node;
    }

    public PositionVisualisation getPosition() {
        return position;
    }
}
