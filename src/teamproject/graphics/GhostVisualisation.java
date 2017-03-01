package teamproject.graphics;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import teamproject.constants.Images;
import teamproject.gamelogic.domain.Position;

/**
 * Created by Boyan Bonev on 11/02/2017.
 */
public class GhostVisualisation {

    private PositionVisualisation position;
    private Node node;
    /**
     * Initialize new visualization for the ghost
     * @param position
     */
    public GhostVisualisation(Position position) {
        this.position = new PositionVisualisation(position.getRow(), position.getColumn());
        Images.Ghost = new ImageView("ghost.png");
    }

    /**
     * Get the node that represents the ghost
     * @return Node
     */
    public Node getNode(){
		double min = position.getHeight();
		if (position.getWidth() < position.getHeight()) {
			min = position.getWidth();
		}

		Images.Ghost.setFitWidth(min);
		Images.Ghost.setFitHeight(min);

		Images.Ghost.setX(position.getPixelX()+ position.getWidth() / 2 - min / 2);
		Images.Ghost.setY(position.getPixelY()+ position.getHeight() / 2 - min / 2);

		node = Images.Ghost;

		return node;
    }
}
