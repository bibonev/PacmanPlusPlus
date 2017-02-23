package teamproject.graphics.event.listener;

import teamproject.ai.AIGhost;
import teamproject.event.arguments.NewGameStartedEventArguments;
import teamproject.gamelogic.domain.Cell;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.Player;
import teamproject.graphics.GhostVisualisation;
import teamproject.graphics.PacmanVisualisation;
import teamproject.graphics.PositionVisualisation;
import teamproject.graphics.Render;

/**
 * Created by boyanbonev on 13/02/2017.
 */
public class NewGameStartedEventListener implements teamproject.event.listener.NewGameStartedEventListener {
	@Override
	public void onNewGameStarted(final NewGameStartedEventArguments args) {
		/* final Render mapV = new Render();

		// Initialize Screen dimensions
		PositionVisualisation.initScreenDimensions();

		// Draw Map
		args.getStage().setScene(mapV.drawMap(cells));
		args.getStage().show();

		// Add CLick Listener
		mapV.addClickListener();

		// Create Pacman
		GamePlay.pacman = new PacmanVisualisation(player1, mapV);

		// Create Ghost
		GamePlay.ghost1 = new GhostVisualisation(ghost, GamePlay.pacman, mapV);

		// Redraw Map
		mapV.redrawMap();

		// Start Timeline
		mapV.startTimeline();*/
	}
}
