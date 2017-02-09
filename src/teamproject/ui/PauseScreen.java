package teamproject.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

/**
 * Overlay screen for pausing the game
 * 
 * @author Rose Kirtley
 *
 */
public class PauseScreen extends Screen {
	
	Button cont;
	Button quit;
	Label label;
	private GameScreen gameScreen;

	public PauseScreen(GameUI game, GameScreen gameScreen){
		super(game);
		this.gameScreen = gameScreen;
		
        cont = new Button("Continue");
        setUpButton(cont);
        cont.setOnAction(e-> gameScreen.play());
        quit = new Button("Quit game");
        setUpButton(quit);
        quit.setOnAction(e-> switchToMain());
        label = new Label("Game Paused");
        label.setStyle(pauseLabelStyle);;
        label.setTextFill(Color.WHITE);
		pane.setStyle(pausePaneStyle);
        
        pane.setAlignment(Pos.CENTER);
	    pane.getChildren().addAll(label, cont, quit);
	}
		
	private void switchToMain(){
		game.switchToMenu();
		gameScreen.quit();
	}
}
