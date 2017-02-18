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
public class CellVisualisation {
	private Cell cell;
	private Node node;

	public CellVisualisation(Cell cell) {
		this.cell = cell;
	}

	/**
	 * Get the node for the particular cell depending on its state
	 * 
	 * @return Node
	 */
	public Node getNode() {
		PositionVisualisation positionVisualisation = new PositionVisualisation(cell.getPosition().getRow(), cell.getPosition().getColumn());
		if (cell.getState() == CellState.FOOD) {
			node = new Circle(positionVisualisation.getPixelX() + positionVisualisation.getWidth() / 2,
					positionVisualisation.getPixelY() + positionVisualisation.getHeight() / 2,
					positionVisualisation.getWidth() / 8);
			((Circle) node).setFill(Colors.CellFoodColor);
		} else if (cell.getState() == CellState.OBSTACLE) {
			Images.Border = new ImageView("border.jpg");

			Images.Border.setFitWidth(positionVisualisation.getWidth());
			Images.Border.setFitHeight(positionVisualisation.getHeight());

			Images.Border.setX(positionVisualisation.getPixelX() + positionVisualisation.getWidth() / 2
					- positionVisualisation.getWidth() / 2);
			Images.Border.setY(positionVisualisation.getPixelY() + positionVisualisation.getHeight() / 2
					- positionVisualisation.getHeight() / 2);

			node = Images.Border;
		} else if (cell.getState() == CellState.EMPTY) {
			node = new Rectangle(positionVisualisation.getPixelX(), positionVisualisation.getPixelY(),
					positionVisualisation.getWidth(), positionVisualisation.getHeight());
			((Rectangle) node).setFill(Colors.CellEmptyColor);
		}

		return node;
	}

	public Cell getCell() {
		return cell;
	}
}
