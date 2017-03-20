package main.java.graphics;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import main.java.constants.GameOutcome;
import main.java.constants.ScreenSize;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.Player;

public class InGameScreens {
	
	private Game game;
	
	public InGameScreens(final Game game){
		this.game = game;
	}

	public StackPane getPlayerRespawnWindow(final String deathReason, final boolean canRejoin) {
		final StackPane pane = new StackPane();
		pane.setStyle("-fx-background-color: rgba(0, 100, 100, 0.6)");
		pane.setPrefSize(ScreenSize.Width, ScreenSize.Height);
		
		final Label deathLabel = new Label("You died!");
		deathLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 50 \"serif\"; -fx-padding: 20 0 0 0; -fx-text-alignment: center");

		final Label reasonLabel = new Label(deathReason);
		reasonLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 35 \"serif\"; -fx-padding: 0 0 0 0; -fx-text-alignment: center");

		String retryString = canRejoin ?
				"Press SPACE to respawn" :
				"You have ran out of lives";
		final Label retryLabel = new Label(retryString);
		retryLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 35 \"serif\"; -fx-padding: 0 0 0 0; -fx-text-alignment: center");
		StackPane.setAlignment(deathLabel, Pos.TOP_CENTER);
		StackPane.setAlignment(reasonLabel, Pos.CENTER);
		StackPane.setAlignment(retryLabel, Pos.BOTTOM_CENTER);

		pane.getChildren().addAll(deathLabel, reasonLabel, retryLabel);
		return pane;
	}
	
	public StackPane pauseGameScreen() {
		final StackPane pane = new StackPane();
		pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");
		pane.setPrefSize(ScreenSize.Width, ScreenSize.Height);

		final Label pauseLabel = new Label("Paused");
		pauseLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 50 \"serif\"; -fx-padding: 20 0 0 0; -fx-text-alignment: center");
		
		final Label escLable = new Label("* Press ESC to resume the game");
		escLable.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 35 \"serif\"; -fx-padding: 0 0 0 0; -fx-text-alignment: center");
		
		final Label spaceLabel = new Label("* Press SPACE to go to settings");
		spaceLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 35 \"serif\"; -fx-padding: 75 15 0 0; -fx-text-alignment: center");

		final Label exitLabel = new Label("* Press Q to go back at the menu");
		exitLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 35 \"serif\"; -fx-padding: 150 -5 0 0; -fx-text-alignment: center");
		
		StackPane.setAlignment(pauseLabel, Pos.TOP_CENTER);
		StackPane.setAlignment(escLable, Pos.CENTER);
		StackPane.setAlignment(spaceLabel, Pos.CENTER);
		StackPane.setAlignment(exitLabel, Pos.CENTER);

		pane.getChildren().addAll(pauseLabel, escLable, spaceLabel, exitLabel);
		
		return pane;
	}
	
	public StackPane endGameScreen(final int localPlayerID, final GameOutcome gameOutcome) {
		final StackPane pane = new StackPane();
		pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");
		pane.setPrefSize(ScreenSize.Width, ScreenSize.Height);

		final Label outcomeLabel = new Label(getGameOutcomeText(localPlayerID, gameOutcome));
		outcomeLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 50 \"serif\"; -fx-padding: 20 0 0 0; -fx-text-alignment: center");

		final Label escLable = new Label("* Press ESC to go back at the menu");
		escLable.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 35 \"serif\"; -fx-padding: 0 0 0 0; -fx-text-alignment: center");

		final Label spaceLabel = new Label("* Press SPACE to replay");
		spaceLabel.setStyle(
				"-fx-text-fill: #fafad2; -fx-font: bold 35 \"serif\"; -fx-padding: 75 160 0 0; -fx-text-alignment: center");
		StackPane.setAlignment(outcomeLabel, Pos.TOP_CENTER);
		StackPane.setAlignment(escLable, Pos.CENTER);
		StackPane.setAlignment(spaceLabel, Pos.CENTER);

		pane.getChildren().addAll(outcomeLabel, escLable, spaceLabel);
		return pane;
	}
	
	private String getGameOutcomeText(final int localPlayerID, final GameOutcome gameOutcome) {
		switch(gameOutcome.getOutcomeType()) {
			case GHOSTS_WON:
				return "Damn! The ghosts won this time...";
			case PLAYER_WON:
				if(gameOutcome.getWinner().getID() == localPlayerID) {
					return "Wohoo, you won!";
				} else {
					return "Damn, " + gameOutcome.getWinner().getName() + " won this time.";
				}
			case TIE:
				return "No one won. Stop being bad at the game.";
			default:
				return "A " + gameOutcome.getOutcomeType().name() + " happened.";
		}
	}
}
