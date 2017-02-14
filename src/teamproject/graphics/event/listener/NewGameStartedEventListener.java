package teamproject.graphics.event.listener;

import teamproject.event.arguments.container.NewGameStartedEventArguments;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.graphics.BasicBehaviour;
import teamproject.graphics.GamePlay;
import teamproject.graphics.GhostVisualisation;
import teamproject.graphics.MapVisualisation;
import teamproject.graphics.PacmanVisualisation;
import teamproject.graphics.PositionVisualisation;
import teamproject.graphics.Render;

/**
 * Created by boyanbonev on 13/02/2017.
 */
public class NewGameStartedEventListener implements teamproject.event.listener.NewGameStartedEventListener {
	@Override
	public void onNewGameStarted(final NewGameStartedEventArguments args) {
		final Behaviour sampleBehavior = new BasicBehaviour(Behaviour.Type.DEFAULT);

		final MapVisualisation grid = new MapVisualisation();
		final Render mapV = new Render(grid);

		// Initialize Screen dimensions
		PositionVisualisation.initScreenDimensions();

		// Generate Map
		// args.stage.setScene(mapV.drawMap());
		// args.stage.show();

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
}
