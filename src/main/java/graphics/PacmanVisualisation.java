package main.java.graphics;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import main.java.constants.Colors;
import main.java.gamelogic.domain.Player;

/**
 * Created by Boyan Bonev on 11/02/2017.
 */
public class PacmanVisualisation implements Visualisation {

	private ImageView pacMan;
	private Player player;
	private PositionVisualisation pv;

	/**
	 * Initialize new visualisation for the PacMan player
	 *
	 * @param player, the player that need to be visualised
	 */
	public PacmanVisualisation(final Player player) {
		this.player = player;
		this.pacMan = new ImageView("images/pacman-animated.gif");
		this.pv = new PositionVisualisation(player.getPosition().getRow(), player.getPosition().getColumn());
	}

	/**
	 * Get the node that represents the PacMan player
	 *
	 * @return Node
	 */
	@Override
	public Node getNode() {
		pacMan.setFitWidth(getMin());
		pacMan.setFitHeight(getMin());
		pacMan.toFront();

		pacMan.setTranslateX(pv.getPixelX() + pv.getWidth() / 2 - getMin() / 2);
		pacMan.setTranslateY(pv.getPixelY() + pv.getHeight() / 2 - getMin() / 2);
		pacMan.setRotate(player.getAngle());
		return pacMan;
	}

	public Node getShieldNode(){
		pacMan.setFitWidth(getMin());
		pacMan.setFitHeight(getMin());

		Circle shield = new Circle(pv.getPixelX() + pv.getWidth() / 2,
				pv.getPixelY() + pv.getHeight() / 2,
				pv.getWidth() / 4);
		shield.setFill(Colors.ShieldColor);

		StackPane pacManWithShield = new StackPane();
		pacManWithShield.setTranslateX(pv.getPixelX() + pv.getWidth() / 2 - getMin() / 2);
		pacManWithShield.setTranslateY(pv.getPixelY() + pv.getHeight() / 2 - getMin() / 2);
		pacManWithShield.setRotate(player.getAngle());
		pacManWithShield.getChildren().addAll(pacMan, shield);
		return pacManWithShield;
	}

	private double getMin(){
		double min = pv.getHeight();
		if (pv.getWidth() < pv.getHeight()) {
			min = pv.getWidth();
		}
		return min;
	}
}
