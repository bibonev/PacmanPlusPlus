package teamproject.graphics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import teamproject.constants.*;
import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Map;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by boyanbonev on 09/02/2017.
 */
public class MapVisualisation {
    protected static Pane root;

    static Timeline timeLine;
    static Scene scene;
    static Stage stage;
    static GridVisualisation grid;

    public MapVisualisation(GridVisualisation grid){
        this.grid = grid;
    }

    public static Stage generateMap(Stage stage) {
        MapVisualisation.stage = stage;

        Images.Border = new ImageView("border.jpg");
        grid = new GridVisualisation();
        root = new Pane();
        root.setStyle("-fx-background-color: black");

        scene = new Scene(root, ScreenSize.Width, ScreenSize.Height);

        //Create obstacles
        initObstacles();
        CellState state = CellState.EMPTY;

        for (int i = 0; i < CellSize.Rows; i++) {
            for (int j = 0; j < CellSize.Columns; j++) {
                PositionVisualisation position = new PositionVisualisation(i, j);

                //Check if not boundary
                if (i != CellSize.Columns && j != CellSize.Rows) {
                    if (i == 1 && j == 1)
                        state = CellState.EMPTY;
                    else if (isObstacle(position))
                        state = CellState.OBSTACLE;
                    else
                        state = CellState.FOOD;
                }

                CellVisualisation cell = new CellVisualisation(CellType.NORMAL, state, position);
                grid.addCell(cell);

                root.getChildren().add(cell.getNode());
            }

        }

        stage.setScene(scene);
        return stage;
    }

    public static void redrawMap() {
        PositionVisualisation.initScreenDimensions();

        root.getChildren().clear();
        System.out.println(grid.getCells().length);

        for (int i = 0; i < CellSize.Rows; i++) {
            for (int j = 0; j < CellSize.Columns; j++) {
                System.out.println("I, j : " + i + ", " + j);
                root.getChildren().add(grid.getCell(i, j).getNode());
                //System.out.println("I, j : " + i + ", " + j);
            }
        }

        root.getChildren().add(GamePlay.pacman.getNode());

        root.getChildren().add(GamePlay.ghost1.getNode());

        root.requestFocus();
    }

    private static boolean isObstacle(PositionVisualisation position){

        for (int i =0;i< obstacles.size();i++){
            PositionVisualisation tmpPosition = obstacles.get(i);
            if (position.getRow() == tmpPosition.getRow() && position.getColumn() == tmpPosition.getColumn())
                return true;
        }

        return false;

    }

    private static ArrayList<PositionVisualisation> obstacles = new ArrayList<>();

    private static void initObstacles(){

        //Generate Left Obstacles
        obstacles.add(new PositionVisualisation(2, 2));
        obstacles.add(new PositionVisualisation(1, 4));
        obstacles.add(new PositionVisualisation(2, 4));
        obstacles.add(new PositionVisualisation(3, 4));
        obstacles.add(new PositionVisualisation(4, 4));

        obstacles.add(new PositionVisualisation(4, 2));
        obstacles.add(new PositionVisualisation(5, 2));
        obstacles.add(new PositionVisualisation(6, 2));

        obstacles.add(new PositionVisualisation(6, 3));

        obstacles.add(new PositionVisualisation(13, 4));
        obstacles.add(new PositionVisualisation(12, 4));
        obstacles.add(new PositionVisualisation(11, 4));
        obstacles.add(new PositionVisualisation(10, 4));

        obstacles.add(new PositionVisualisation(12, 2));
        obstacles.add(new PositionVisualisation(8, 2));
        obstacles.add(new PositionVisualisation(9, 2));
        obstacles.add(new PositionVisualisation(11, 2));

        obstacles.add(new PositionVisualisation(3, 6));


        //Generate Reflection
        int loopSize = obstacles.size();
        for (int i =0;i< loopSize;i++){

            PositionVisualisation tmpPosition = obstacles.get(i);
            PositionVisualisation newPosition = new PositionVisualisation(tmpPosition.getRow(), CellSize.Columns-1-tmpPosition.getColumn());
            obstacles.add(newPosition);

        }

        //Generate Center Obstacles
        obstacles.add(new PositionVisualisation(6, 6));
        obstacles.add(new PositionVisualisation(7, 6));
        obstacles.add(new PositionVisualisation(8, 6));
        obstacles.add(new PositionVisualisation(8, 7));
        obstacles.add(new PositionVisualisation(8, 8));

        obstacles.add(new PositionVisualisation(7, 8));
        obstacles.add(new PositionVisualisation(6, 8));

        obstacles.add(new PositionVisualisation(10, 7));
        obstacles.add(new PositionVisualisation(11, 7));
        obstacles.add(new PositionVisualisation(12, 7));

        obstacles.add(new PositionVisualisation(2, 7));
        obstacles.add(new PositionVisualisation(3, 7));
        obstacles.add(new PositionVisualisation(4, 7));
    }

    public static void addClickListener(){
        MapVisualisation.root.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.UP)
                GamePlay.pacman.moveUp();
            else if (event.getCode() == KeyCode.DOWN)
                GamePlay.pacman.moveDown();
            else if (event.getCode() == KeyCode.LEFT)
                GamePlay.pacman.moveLeft();
            else if (event.getCode() == KeyCode.RIGHT)
                GamePlay.pacman.moveRight();

        });
    }


    public static void invalidateClickListener(){

        MapVisualisation.root.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.SPACE)
            {
                replay();
            }

        });

    }

    public static void replay(){
        Behaviour sampleBehavior = new BasicBehaviour(Behaviour.Type.BASIC);

        //Generate Map
        MapVisualisation.generateMap(stage);

        //Add CLick istener
        MapVisualisation.addClickListener();

        //Create Pacman
        GamePlay.pacman = new PacmanVisualisation(sampleBehavior, "Player1", grid);

        //Create Ghost
        GamePlay.ghost1 = new GhostVisualisation(sampleBehavior, "Ghost1", grid, GamePlay.pacman);

        //Redraw Map
        MapVisualisation.redrawMap();

        //Start Timeline
        MapVisualisation.startTimeline();
    }

    public static void gameEnded(){
        invalidateClickListener();
        timeLine.stop();
    }

    public static void startTimeline(){
        timeLine = new Timeline(new KeyFrame(Duration.millis(250), event -> {
            GamePlay.ghost1.moveGhost();
        }));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

}
