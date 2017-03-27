package main.java.graphics;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import main.java.gamelogic.domain.Spawner;
import main.java.gamelogic.domain.Spawner.SpawnerColor;

public class SpawnerVisualisation implements Visualisation {

	private PositionVisualisation position;
	private ImageView node;
	private Spawner spawner;
	
	private Effect createTint(Color color, int width, int height) {
		ColorAdjust desaturate = new ColorAdjust(0, -1, 0, 0);
		// return desaturate;
        return new Blend(
                BlendMode.MULTIPLY,
                desaturate,
                new ColorInput(0, 0, width, height, color));
	}
	
	private Color getColor(SpawnerColor color) {
		switch (color) {
			case CYAN: return Color.CYAN;
			case RED: return Color.RED;
			case GREEN: return Color.LIMEGREEN;
			case YELLOW: return Color.YELLOW;
			default: return Color.WHITE;
		}
	}

	/**
	 * Initialize new visualization for the spawner countdown
	 *
	 * @param spawner
	 */
	public SpawnerVisualisation(final Spawner spawner) {
		this.spawner = spawner;
		this.position = new PositionVisualisation(spawner.getPosition().getRow(), spawner.getPosition().getColumn());
		node = new ImageView("/images/num1.png");
		setNumber(spawner.getTimeRemaining());
	}

	/**
	 * Set particular number for getting the right picture depending on the timer
	 * @param number
	 */
	public void setNumber(int number) {
		node.setImage(new Image(String.format("/images/num%d.png", number)));
	}

	/**
	 * Get the node that represents the ghost
	 *
	 * @return Node
	 */
	@Override
	public ImageView getNode() {
		double min = position.getHeight();
		if (position.getWidth() < position.getHeight()) {
			min = position.getWidth();
		}

		node.setFitWidth(min);
		node.setFitHeight(min);

		node.setTranslateX(position.getPixelX() + position.getWidth() / 2 - min / 2);
		node.setTranslateY(position.getPixelY() + position.getHeight() / 2 - min / 2);

		node.setEffect(createTint(getColor(spawner.getColor()), (int)min, (int)min));

		return node;
	}
}
