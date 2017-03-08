package teamproject.graphics;

import javafx.scene.image.ImageView;
import teamproject.gamelogic.domain.Position;

/**
 * Created by Boyan Bonev on 11/02/2017.
 */
public class GhostVisualisation {

    private PositionVisualisation position;
    private ImageView node;
    /**
     * Initialize new visualization for the ghost
     * @param position
     */
    public GhostVisualisation(Position position) {
        this.position = new PositionVisualisation(position.getRow(), position.getColumn());
        this.node = new ImageView("ghost.png");
    }

    /**
     * Get the node that represents the ghost
     * @return Node
     */
    public ImageView getNode(){
		double min = position.getHeight();
		if (position.getWidth() < position.getHeight()) {
			min = position.getWidth();
		}

		node.setFitWidth(min);
        node.setFitHeight(min);

        node.setTranslateX(position.getPixelX()+ position.getWidth() / 2 - min / 2);
        node.setTranslateY(position.getPixelY()+ position.getHeight() / 2 - min / 2);


		return node;
    }
}
