package main.java.graphics;

import javafx.scene.image.ImageView;
import main.java.gamelogic.domain.Player;

/**
 * Created by Boyan Bonev on 11/02/2017.
 */
public class PacmanVisualisation implements Visualisation {

	private ImageView pacMan;
	private Player player;

	/**
	 * Initialize new visualisation for the PacMan player
	 *
	 * @param player
	 */
	public PacmanVisualisation(final Player player) {
		this.player = player;
		pacMan = new ImageView("pacman-animated.gif");
	}

	/**
	 * Get the node that represents the PacMan player
	 *
	 * @return Node
	 */
	@Override
	public ImageView getNode() {
		final PositionVisualisation pv = new PositionVisualisation(player.getPosition().getRow(),
				player.getPosition().getColumn());

		double min = pv.getHeight();
		if (pv.getWidth() < pv.getHeight()) {
			min = pv.getWidth();
		}

		pacMan.setFitWidth(min);
		pacMan.setFitHeight(min);

		pacMan.setTranslateX(pv.getPixelX() + pv.getWidth() / 2 - min / 2);
		pacMan.setTranslateY(pv.getPixelY() + pv.getHeight() / 2 - min / 2);

		pacMan.setRotate(player.getAngle());
		pacMan.toFront();

		return pacMan;
	}
}
