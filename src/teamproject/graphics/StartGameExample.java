package teamproject.graphics;

import javafx.application.Application;
import javafx.stage.Stage;

import teamproject.ai.DefaultBehaviour;
import teamproject.gamelogic.domain.*;
import teamproject.gamelogic.domain.Behaviour.Type;

import java.util.HashMap;
import java.util.Optional;

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

		Inventory stash = new Inventory(new HashMap<>(2));
		Behaviour bh = new DefaultBehaviour(grid, new PositionVisualisation(0,0), 2, stash, Type.DEFAULT);

		Player pl = new Player(Optional.empty(),"Player1");
		Ghost gh = new GLGhost(bh, "Ghost1");

		// Create Pacman
		GamePlay.pacman = new PacmanVisualisation(pl, grid, mapV);

		// Create Ghost
		GamePlay.ghost1 = new GhostVisualisation(gh, grid, GamePlay.pacman, mapV);

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
