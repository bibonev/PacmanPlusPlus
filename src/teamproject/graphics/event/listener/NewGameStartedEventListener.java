package teamproject.graphics.event.listener;

import teamproject.event.arguments.NewGameStartedEventArgs;

/**
 * Created by boyanbonev on 13/02/2017.
 */
public class NewGameStartedEventListener implements teamproject.event.listener.NewGameStartedEventListener {
	@Override
	public void onNewGameStarted(final NewGameStartedEventArgs args) {
		/* final Render mapV = new Render();

		// Initialize Screen dimensions
		PositionVisualisation.initScreenDimensions();

		// Draw Map
		args.getStage().setScene(mapV.drawWorld(cells));
		args.getStage().show();

		// Add CLick Listener
		mapV.addClickListener();

		// Create Pacman
		GamePlay.pacman = new PacmanVisualisation(player1, mapV);

		// Create Ghost
		GamePlay.ghost1 = new GhostVisualisation(ghost, GamePlay.pacman, mapV);

		// Redraw Map
		mapV.redrawWorld();

		// Start Timeline
		mapV.startTimeline();*/
	}
}
