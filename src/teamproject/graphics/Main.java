package teamproject.graphics;

import javafx.application.Application;
import javafx.stage.Stage;
import teamproject.gamelogic.domain.Behaviour;

/**
 * Created by boyanbonev on 11/02/2017.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Behaviour sampleBehavior = new BasicBehaviour(Behaviour.Type.BASIC);

        GridVisualisation grid = new GridVisualisation();
        MapVisualisation mapV = new MapVisualisation(grid);

        //Initialose ScreenDimentions
        PositionVisualisation.initScreenDimensions();

        //Generate Map
        MapVisualisation.generateMap(stage).show();

        //Add CLick Listener
        MapVisualisation.addClickListener();

        //Create Pacman
        GamePlay.pacman = new PacmanVisualisation(sampleBehavior, "Player1", grid);

        //Create Ghost
        GamePlay.ghost1 = new GhostVisualisation(sampleBehavior, "Ghost1", grid, GamePlay.pacman);

        //Redraw Map
        MapVisualisation.redrawMap();

        //Start Timeline
        MapVisualisation.startTimeline();

        //Play background music
        //playMusic();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
