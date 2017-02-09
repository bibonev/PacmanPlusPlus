package teamproject.ui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Screen for the actual game
 * 
 * @author Rose Kirtley
 *
 */
public class GameScreen extends Screen {
	
	private Button pause;
	private Rectangle gameGraphics;
	private StackPane mainPane;
	private FlowPane gamePane;
	private Screen pauseScreen;

	public GameScreen(GameUI game){
		super(game);

        pause = new Button("pause");
        setUpButton(pause);
        pause.setOnAction(e-> paused());
                
        gamePane = new FlowPane();
        gamePane.setPadding(new Insets(5, 0, 5, 0));
        gamePane.setVgap(4);
        gamePane.setHgap(4);
        gamePane.setOrientation(Orientation.VERTICAL);
        gamePane.setAlignment(Pos.TOP_CENTER);
        
        gameGraphics = new Rectangle(300,300);
        gameGraphics.getFill();
		
        gamePane.getChildren().addAll(pause, gameGraphics);
		pauseScreen = new PauseScreen(game, this);
        
        mainPane = new StackPane();
		mainPane.getChildren().add(gamePane);
		
        pane.getChildren().addAll(mainPane);
	}
	
	private void paused(){
		pause.setDisable(true);
		mainPane.getChildren().add(pauseScreen.getPane());
		game.setIsPlaying(false);
	}
	
	public void play(){
		pause.setDisable(false);
		mainPane.getChildren().removeAll(pauseScreen.getPane());
		game.setIsPlaying(true);
	}
	
	public void quit(){
		pause.setDisable(false);
		mainPane.getChildren().removeAll(pauseScreen.getPane());
	}

}
