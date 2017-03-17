package main.java.graphics;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import main.java.constants.GameOutcome;
import main.java.constants.ScreenSize;
import main.java.gamelogic.domain.Game;

public class InGameScreens {
	
	private Game game;
	
	public InGameScreens(final Game game){
		this.game = game;
	}
	
	public StackPane pauseGameScreen() {
		final StackPane pane = new StackPane();
		pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");
		pane.setPrefSize(ScreenSize.Width, ScreenSize.Height);

		final Label pauseLabel = new Label("Paused");
		pauseLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 30 \"serif\"; -fx-padding: 20 0 0 0; -fx-text-alignment: center");

		StackPane.setAlignment(pauseLabel, Pos.CENTER);
		
		pane.getChildren().addAll(pauseLabel);
		return pane;
	}
	
	public StackPane endGameScreen(final GameOutcome gameOutcome) {
		final StackPane pane = new StackPane();
		pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");
		pane.setPrefSize(ScreenSize.Width, ScreenSize.Height);

		final Label outcomneLabel = new Label(getGameOutcomeText(gameOutcome));
		outcomneLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 30 \"serif\"; -fx-padding: 20 0 0 0; -fx-text-alignment: center");

		final Label escLable = new Label("* Press ESC to go back at the menu");
		escLable.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 20 \"serif\"; -fx-padding: 0 0 0 0; -fx-text-alignment: center");

		final Label spaceLabel = new Label("* Press SPACE to reply");
		spaceLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 20 \"serif\"; -fx-padding: 50 103 0 0; -fx-text-alignment: center");
		StackPane.setAlignment(outcomneLabel, Pos.TOP_CENTER);
		StackPane.setAlignment(escLable, Pos.CENTER);
		StackPane.setAlignment(spaceLabel, Pos.CENTER);

		pane.getChildren().addAll(outcomneLabel, escLable, spaceLabel);
		return pane;
	}
	
	private String getGameOutcomeText(final GameOutcome gameOutcome) {
		switch(gameOutcome.getOutcomeType()) {
			case GHOSTS_WON:
				return "Damn! The ghosts won this time...";
			case PLAYER_WON:
				if(gameOutcome.getWinner().getID() == game.getPlayer().getID()) {
					return "Wohoo, you won!";
				} else {
					return "Damn, " + gameOutcome.getWinner().getName() + " won this time!";
				}
			case TIE:
				return "No one won. Stop being bad at the game";
			default:
				return "A " + gameOutcome.getOutcomeType().name() + " happened";
		}
	}
}
