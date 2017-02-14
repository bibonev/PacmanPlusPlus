package teamproject.graphics;

import javafx.scene.image.ImageView;
import javafx.scene.Node;
import java.util.Random;

import teamproject.constants.CellSize;
import teamproject.constants.CellState;
import teamproject.constants.Images;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Ghost;

/**
 * Created by Boyan Bonev on 11/02/2017.
 */
public class GhostVisualisation extends Ghost {

    private PositionVisualisation position;
    private PacmanVisualisation pacman;
    private MapVisualisation grid;
    private Node node;
    private Render map;
    /**
     * Initialize new visualization for the ghost
     * @param behaviour
     * @param name
     * @param grid
     * @param pacman
     * @param map
     */
    public GhostVisualisation(
            Behaviour behaviour,
            String name,
            MapVisualisation grid,
            PacmanVisualisation pacman,
            Render map) {
        super(behaviour, name);

        this.position = new PositionVisualisation(CellSize.Rows/2, CellSize.Columns/2);
        this.grid = grid;
        this.pacman = pacman;
        this.map = map;
        Images.Ghost = new ImageView("ghost.png");
    }

    /**
     * Move Ghost randomly
     */
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
            map.gameEnded();
        }else{
            map.redrawMap();
        }
    }

    private boolean moveUp(){
        if (grid.getCell(position.getRow() - 1, position.getColumn()).getState() == CellState.OBSTACLE)
            return false;

        position = new PositionVisualisation(position.getRow() - 1, position.getColumn());
        map.redrawMap();

        return true;
    }

    private boolean moveDown(){
        if (grid.getCell(position.getRow() + 1, position.getColumn()).getState() == CellState.OBSTACLE)
            return false;

        position = new PositionVisualisation(position.getRow() + 1, position.getColumn());
        map.redrawMap();

        return true;
    }

    private boolean moveLeft(){
        if (grid.getCell(position.getRow(), position.getColumn() - 1).getState() == CellState.OBSTACLE)
            return false;

        position = new PositionVisualisation(position.getRow(), position.getColumn() - 1);
        map.redrawMap();

        return true;
    }

    private boolean moveRight(){
        if (grid.getCell(position.getRow(), position.getColumn() + 1).getState() == CellState.OBSTACLE)
            return false;

        position = new PositionVisualisation(position.getRow(), position.getColumn() + 1);
        map.redrawMap();

        return true;
    }

    /**
     * Get the node that represents the ghost
     * @return Node
     */
    public  Node getNode(){
        double min = position.getHeight();
        if (position.getWidth() < position.getHeight())
            min = position.getWidth();

        Images.Ghost.setFitWidth(min);
        Images.Ghost.setFitHeight(min);

        Images.Ghost.setX(position.getPixelX()+position.getWidth()/2 - min/2);
        Images.Ghost.setY(position.getPixelY()+position.getHeight()/2 - min/2);

        node = Images.Ghost;

        return node;
    }
}
