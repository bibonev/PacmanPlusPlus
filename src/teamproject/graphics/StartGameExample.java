package teamproject.graphics;

import javafx.application.Application;
import javafx.stage.Stage;

import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Behaviour.Type;
import teamproject.gamelogic.domain.Map;

/**
 * Created by Boyan Bonev on 11/02/2017.
 */
public class StartGameExample extends Application {

	/**
	 * Start the graphics
	 * @param stage
	 */
	@Override
	public void start(final Stage stage) {
		final Behaviour sampleBehavior = new BasicBehaviour(Type.DEFAULT);

		final MapVisualisation grid = new MapVisualisation();
		final Render mapV = new Render(grid);

        stage.setResizable(false);
		// Initialize Screen dimensions
		PositionVisualisation.initScreenDimensions();

		Map newMap = new MapVisualisation();
		newMap.generateMap();

		// Generate Map
		stage.setScene(mapV.drawMap(newMap.getCells()));
		stage.show();

		// Add CLick Listener
		mapV.addClickListener();

		// Create Pacman
		GamePlay.pacman = new PacmanVisualisation(sampleBehavior, "Player1", grid, mapV);

		// Create Ghost
		GamePlay.ghost1 = new GhostVisualisation(sampleBehavior, "Ghost1", grid, GamePlay.pacman, mapV);

		// Redraw Map
		mapV.redrawMap();

		// Start Timeline
		mapV.startTimeline();
	}

	/**
	 * Run the graphics
	 * @param args
	 */
	public static void main(final String[] args) {
		launch(args);
	}
}
