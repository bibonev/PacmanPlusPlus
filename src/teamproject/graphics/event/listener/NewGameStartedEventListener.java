package teamproject.graphics.event.listener;

import teamproject.event.arguments.container.NewGameStartedEventArguments;
import teamproject.gamelogic.domain.GLGhost;
import teamproject.gamelogic.domain.Ghost;
import teamproject.gamelogic.domain.Player;
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
		Player player1 = (Player) args.getGame().getWorld().getPlayers().toArray()[0];
		Ghost ghost = (GLGhost) args.getGame().getWorld().getPlayers().toArray()[1];

		final MapVisualisation grid = new MapVisualisation();
		final Render mapV = new Render(grid);

		// Initialize Screen dimensions
		PositionVisualisation.initScreenDimensions();

		// Generate Map
		args.getStage().setScene(mapV.drawMap(args.getGame().getWorld().getMap().getCells()));
		args.getStage().show();

		// Add CLick Listener
		mapV.addClickListener();

		// Create Pacman
		GamePlay.pacman = new PacmanVisualisation(player1, grid, mapV);

		// Create Ghost
		GamePlay.ghost1 = new GhostVisualisation(ghost, grid, GamePlay.pacman, mapV);

		// Redraw Map
		mapV.redrawMap();

		// Start Timeline
		mapV.startTimeline();
	}
}
