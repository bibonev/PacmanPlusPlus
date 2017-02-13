package teamproject.graphics.event.listener;

import teamproject.event.arguments.container.NewGameStartedEventArguments;
import teamproject.event.listener.NewGameStartedEventListener;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.graphics.*;

/**
 * Created by boyanbonev on 13/02/2017.
 */
public class NewGameListener implements NewGameStartedEventListener {
    @Override
    public void onNewGameStarted(NewGameStartedEventArguments args) {
        final Behaviour sampleBehavior = new BasicBehaviour(Behaviour.Type.DEFAULT);

        final GridVisualisation grid = new GridVisualisation();
        final MapVisualisation mapV = new MapVisualisation(grid);

        // Initialize Screen dimensions
        PositionVisualisation.initScreenDimensions();

        // Generate Map
        //args.stage.setScene(mapV.generateMap());
        //args.stage.show();

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
