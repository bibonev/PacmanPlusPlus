package teamproject.graphics;

import javafx.application.Application;
import javafx.stage.Stage;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Behaviour.Type;

/**
 * Created by boyanbonev on 11/02/2017.
 */
public class Main extends Application {

	@Override
	public void start(final Stage stage) {
		final Behaviour sampleBehavior = new BasicBehaviour(Type.DEFAULT);

		final GridVisualisation grid = new GridVisualisation();
		final MapVisualisation mapV = new MapVisualisation(grid);

		// Initialisze Screen Dimentions
		PositionVisualisation.initScreenDimensions();

		// Generate Map
		mapV.generateMap(stage).show();

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

		// Play background music
		// playMusic();
	}

	public static void main(final String[] args) {
		launch(args);
	}
}
