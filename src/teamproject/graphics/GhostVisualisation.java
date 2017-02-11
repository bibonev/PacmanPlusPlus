package teamproject.graphics;

import javafx.scene.image.ImageView;
import teamproject.constants.CellSize;
import teamproject.constants.CellState;
import teamproject.constants.Images;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Ghost;
import javafx.scene.Node;

import java.util.Random;

/**
 * Created by boyanbonev on 11/02/2017.
 */
public class GhostVisualisation extends Ghost {

    private PositionVisualisation position;
    private PacmanVisulisation pacman;
    private GridVisualisation grid;
    private Node node;

    public GhostVisualisation(
            Behaviour behaviour,
            String name,
            GridVisualisation grid,
            PacmanVisulisation pacman) {
        super(behaviour, name);

        this.position = new PositionVisualisation(CellSize.Rows/2,CellSize.Columns/2);
        this.grid = grid;
        this.pacman = pacman;
        Images.Ghost = new ImageView("ghost.png");
    }

    public void moveGhost() {
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 0) + 1) + 0;

        if (pacman.getPosition().getRow() > this.position.getRow()) {
            if (pacman.getPosition().getColumn() > this.position.getColumn()) {
                randomNum = rand.nextInt((1 - 0) + 1) + 0;

                if (randomNum == 0)
                    moveDown();
                else
                    moveRight();
            }

            if (pacman.getPosition().getColumn() <= this.position.getColumn()) {
                randomNum = rand.nextInt((1 - 0) + 1) + 0;

                if (randomNum == 0)
                    moveDown();
                else
                    moveLeft();
            }

        } else if (pacman.getPosition().getRow() <= this.position.getRow()) {
            if (pacman.getPosition().getColumn() >= this.position.getColumn()) {
                randomNum = rand.nextInt((1 - 0) + 1) + 0;

                if (randomNum == 0)
                    moveUp();
                else
                    moveRight();
            }

            if (pacman.getPosition().getColumn() < this.position.getColumn()) {
                randomNum = rand.nextInt((1 - 0) + 1) + 0;

                if (randomNum == 0)
                    moveUp();
                else
                    moveLeft();
            }
        }

        if (position.getRow() == pacman.getPosition().getRow() && position.getColumn() == pacman.getPosition().getColumn()) {
            //MapGenerator.gameEnded();
        }else{
            //MapGenerator.redrawMap();
        }
    }

    public boolean moveUp(){
        if (grid.getCell(position.getRow() - 1, position.getColumn()).getState() == CellState.OBSTACLE)
            return false;

        position = new PositionVisualisation(position.getRow() - 1, position.getColumn());
        //MapGenerator.redrawMap();

        return true;
    }

    public boolean moveDown(){
        if (grid.getCell(position.getRow() + 1, position.getColumn()).getState() == CellState.OBSTACLE)
            return false;

        position = new PositionVisualisation(position.getRow() + 1, position.getColumn());
        //MapGenerator.redrawMap();

        return true;
    }

    public boolean moveLeft(){
        if (grid.getCell(position.getRow(), position.getColumn() - 1).getState() == CellState.OBSTACLE)
            return false;

        position = new PositionVisualisation(position.getRow(), position.getColumn() - 1);
        //MapGenerator.redrawMap();

        return true;
    }

    public boolean moveRight(){
        if (grid.getCell(position.getRow(), position.getColumn() + 1).getState() == CellState.OBSTACLE)
            return false;

        position = new PositionVisualisation(position.getRow(), position.getColumn() + 1);
        //MapGenerator.redrawMap();

        return true;
    }

    public  Node getNode(){
        double min = position.getHeight();
        if (position.getWidth() < position.getHeight())
            min = position.getWidth();

        Images.Ghost.setFitWidth(min);
        Images.Ghost.setFitHeight(min);

        Images.Ghost.setX(position.getRow()+position.getWidth()/2 - min/2);
        Images.Ghost.setY(position.getColumn()+position.getHeight()/2 - min/2);

        node = Images.Ghost;

        return node;
    }
}
