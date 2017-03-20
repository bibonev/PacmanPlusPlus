package main.java.graphics;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.java.constants.CellState;
import main.java.constants.Colors;
import main.java.gamelogic.domain.Cell;

/**
 * Created by Boyan Bonev on 05/02/2017.
 */
public class CellVisualisation implements Visualisation {
	private Cell cell;
	private Node node;

	public CellVisualisation(final Cell cell) {
		this.cell = cell;
		this.node = null;
	}

	/**
	 * Get the node for the particular cell depending on its state
	 *
	 * @return Node
	 */
	@Override
	public Node getNode() {
		if(node == null) {
			final PositionVisualisation positionVisualisation = new PositionVisualisation(cell.getPosition().getRow(),
					cell.getPosition().getColumn());
			if (cell.getState() == CellState.FOOD) {
				node = new Circle(positionVisualisation.getPixelX() + positionVisualisation.getWidth() / 2,
						positionVisualisation.getPixelY() + positionVisualisation.getHeight() / 2,
						positionVisualisation.getWidth() / 8);
				((Circle) node).setFill(Colors.CellFoodColor);
			} else if (cell.getState() == CellState.OBSTACLE) {
				node = new ImageView("border.jpg");
	
				((ImageView) node).setFitWidth(positionVisualisation.getWidth());
				((ImageView) node).setFitHeight(positionVisualisation.getHeight());
	
				((ImageView) node).setX(positionVisualisation.getPixelX() + positionVisualisation.getWidth() / 2
						- positionVisualisation.getWidth() / 2);
				((ImageView) node).setY(positionVisualisation.getPixelY() + positionVisualisation.getHeight() / 2
						- positionVisualisation.getHeight() / 2);
			} else if (cell.getState() == CellState.EMPTY) {
				node = new Rectangle(positionVisualisation.getPixelX(), positionVisualisation.getPixelY(),
						positionVisualisation.getWidth(), positionVisualisation.getHeight());
				((Rectangle) node).setFill(Colors.CellEmptyColor);
			}
		}

		return node;
	}

	public Cell getCell() {
		return cell;
	}
}
