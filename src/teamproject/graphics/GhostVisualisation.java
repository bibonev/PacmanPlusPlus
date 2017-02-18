package teamproject.graphics;

import javafx.scene.image.ImageView;
import javafx.scene.Node;
import java.util.Random;

import teamproject.constants.CellSize;
import teamproject.constants.CellState;
import teamproject.constants.Images;
import teamproject.constants.ScreenSize;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.RuleEnforcer;

/**
 * Created by Boyan Bonev on 11/02/2017.
 */
public class GhostVisualisation {

    private PositionVisualisation position;
    private Player target;
    private Node node;
    private Ghost ghost;
    /**
     * Initialize new visualization for the ghost
     * @param ghost
     * @param target
     */
    public GhostVisualisation(
            Ghost ghost,
            Player target) {

        this.ghost = ghost;
        this.position = new PositionVisualisation(CellSize.Rows/2, CellSize.Columns/2);
        this.target = target;
        Images.Ghost = new ImageView("ghost.png");
    }

    /**
     * Get the node that represents the ghost
     * @return Node
     */
    public Node getNode(){
    	PositionVisualisation pv = new PositionVisualisation(ghost.getPosition().getRow(), ghost.getPosition().getColumn());
        
		double min = pv.getHeight();
		if (pv.getWidth() < pv.getHeight()) {
			min = pv.getWidth();
		}

		Images.Ghost.setFitWidth(min);
		Images.Ghost.setFitHeight(min);

		Images.Ghost.setX(pv.getPixelX()+ pv.getWidth() / 2 - min / 2);
		Images.Ghost.setY(pv.getPixelY()+ pv.getHeight() / 2 - min / 2);

		node = Images.Ghost;

		return node;
    }
}
