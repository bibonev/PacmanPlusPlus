package teamproject.graphics;

import java.util.Optional;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

import teamproject.ai.AIPlayer;
import teamproject.constants.CellSize;
import teamproject.constants.CellState;
import teamproject.constants.Images;
import teamproject.constants.ScreenSize;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.RuleEnforcer;

/**
 * Created by Boyan Bonev on 11/02/2017.
 */
public class PacmanVisualisation {

	private Node node;
	private Player player;

	/**
	 * Initialize new visualisation for the PacMan player
	 * 
	 * @param player
	 * @param map
	 */
	public PacmanVisualisation(final Player player) {
		this.player = player;
		Images.PacMan = new ImageView("pacman.png");
	}

	/**
	 * Get the node that represents the PacMan player
	 * 
	 * @return Node
	 */
	Node getNode() {
		PositionVisualisation pv = new PositionVisualisation(player.getPosition().getRow(), player.getPosition().getColumn());
        
		double min = pv.getHeight();
		if (pv.getWidth() < pv.getHeight()) {
			min = pv.getWidth();
		}

		Images.PacMan.setFitWidth(min);
		Images.PacMan.setFitHeight(min);

		Images.PacMan.setX(pv.getPixelX()+ pv.getWidth() / 2 - min / 2);
		Images.PacMan.setY(pv.getPixelY()+ pv.getHeight() / 2 - min / 2);
		
		Images.PacMan.setRotate(player.getAngle());

		node = Images.PacMan;

		return node;
	}
}
