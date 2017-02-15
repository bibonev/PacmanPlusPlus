package teamproject.graphics;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import teamproject.constants.CellState;
import teamproject.constants.CellType;
import teamproject.constants.Colors;
import teamproject.constants.Images;
import teamproject.gamelogic.domain.Cell;

/**
 * Created by Boyan Bonev on 05/02/2017.
 */
public class CellVisualisation extends Cell {
	PositionVisualisation positionVisualisation;

	private CellType type;
	private CellState state;
	private Node node;

	/**
	 * Initialization of the CellVisualisation by providing its
	 * positionVisualisation and its state
	 * 
	 * @param positionVisualisation
	 * @param state
	 * @param type
	 */
	public CellVisualisation(final CellType type, final CellState state,
			final PositionVisualisation positionVisualisation) {
		super(type, state, positionVisualisation);
		this.positionVisualisation = positionVisualisation;
		this.state = state;
		this.type = type;
	}

	/**
	 * Get the node for the particular cell depending on its state
	 * 
	 * @return Node
	 */
	Node getNode() {
		if (state == CellState.FOOD) {
			node = new Circle(positionVisualisation.getPixelX() + positionVisualisation.getWidth() / 2,
					positionVisualisation.getPixelY() + positionVisualisation.getHeight() / 2,
					positionVisualisation.getWidth() / 8);
			((Circle) node).setFill(Colors.CellFoodColor);
		} else if (state == CellState.OBSTACLE) {
			Images.Border = new ImageView("border.jpg");

			Images.Border.setFitWidth(positionVisualisation.getWidth());
			Images.Border.setFitHeight(positionVisualisation.getHeight());

			Images.Border.setX(positionVisualisation.getPixelX() + positionVisualisation.getWidth() / 2
					- positionVisualisation.getWidth() / 2);
			Images.Border.setY(positionVisualisation.getPixelY() + positionVisualisation.getHeight() / 2
					- positionVisualisation.getHeight() / 2);

			node = Images.Border;
		} else if (state == CellState.EMPTY) {
			node = new Rectangle(positionVisualisation.getPixelX(), positionVisualisation.getPixelY(),
					positionVisualisation.getWidth(), positionVisualisation.getHeight());
			((Rectangle) node).setFill(Colors.CellEmptyColor);
		}

		return node;
	}

	/**
	 * Override the setState method from the Cell class
	 * 
	 * @param state
	 */
	@Override
	public void setState(final CellState state) {
		this.state = state;
	}
}
