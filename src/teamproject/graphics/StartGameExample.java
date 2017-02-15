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
        stage.setResizable(false);

		// Initialize Screen dimensions
		PositionVisualisation.initScreenDimensions();

        // Generate Map
		final Map newMap = new MapVisualisation();
		newMap.generateMap();

		//Draw Map
		final Render mapV = new Render();
		stage.setScene(mapV.drawMap(newMap.getCells()));
		stage.show();

		// Add CLick Listener
		mapV.addClickListener();

		//Create players, ghosts and other items
		Inventory stash = new Inventory(new HashMap<>(2));
		Behaviour bh = new DefaultBehaviour(newMap, new PositionVisualisation(0,0), 2, stash, Type.GHOST);

		Player pl = new Player(Optional.empty(),"Player1");
		Ghost gh = new GLGhost(bh, "Ghost1");
		
		GamePlay.pacman = new PacmanVisualisation(pl, mapV);

		GamePlay.ghost1 = new GhostVisualisation(gh, GamePlay.pacman, mapV);

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
