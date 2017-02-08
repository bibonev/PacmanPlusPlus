package teamproject.ui;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import teamproject.gamelogic.domain.Game;

/**
 * UI to be run, contains all screens
 * 
 * @author Rose Kirtley
 *
 */
public class GameUI extends Application {
	
	private Game game;

	private Stage thisStage;
	private BorderPane pane;
	private BorderPane banner;
	private Button settings;
	private StackPane centerPane;
	
	private boolean isPlaying;
	
	public LogInScreen logInScreen;
	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	private SettingsScreen settingsScreen;
	private SinglePlayerLobbyScreen singlePlayerLobbyScreen;
	private MultiPlayerLobbyScreen multiPlayerLobbyScreen;
	private MultiPlayerOptionScreen multiPlayerOptionScreen;
	private MultiPlayerJoinScreen multiPlayerJoinScreen;

	@Override
	public void start(Stage primaryStage) throws Exception {
		setup();
		
		thisStage = primaryStage;
		primaryStage.setTitle("PacMac");
		
		pane = new BorderPane();
        pane.setStyle("-fx-background-color: DAE6F3;");
        
        centerPane = new StackPane();
        pane.setCenter(centerPane);
        Scene scene = new Scene(pane, 500, 500);
        scene.setOnKeyPressed(e-> sendMoveEvent(e.getCode().toString()));
        
        setUpSettingsButton();
		
        primaryStage.setScene(scene);
		switchToLogIn();
		primaryStage.show();
	}
	
	private void setup(){
		logInScreen = new LogInScreen(this);
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);
		settingsScreen = new SettingsScreen(this);
		singlePlayerLobbyScreen = new SinglePlayerLobbyScreen(this);
		multiPlayerLobbyScreen = new MultiPlayerLobbyScreen(this);
		multiPlayerOptionScreen = new MultiPlayerOptionScreen(this);
		multiPlayerJoinScreen = new MultiPlayerJoinScreen(this);
	}
	
	private void setUpSettingsButton(){
		
		settings = new Button("Settings");
        settings.setOnAction(e-> switchToSettings());
		settings.styleProperty().bind(
			      Bindings
			        .when(settings.hoverProperty())
			          .then(
			            new SimpleStringProperty(AbstractScreen.hoverbuttonStyle)
			          )
			          .otherwise(
			            new SimpleStringProperty(AbstractScreen.buttonStyle)
			          )
			    );
		banner = new BorderPane();
		
		banner.setRight(settings);
		
		pane.setTop(banner);
	}
	
	public static void main(String[] args){
		launch(args);	
	}
	
	private void setScreen(Pane screen){
		centerPane.getChildren().remove(0, centerPane.getChildren().size());
		centerPane.getChildren().add(screen);
	}
	
	
	public void switchToMenu(){
		setScreen(menuScreen.getPane());
		Label label = new Label("PacMan " + logInScreen.getName());
        banner.setLeft(label);
	}
	
	public void switchToLogIn(){	
		Label label = new Label("PacMan");
		banner.setLeft(label);
		setScreen(logInScreen.getPane());
	}	
	public void switchToGame(){
		isPlaying = true;
		setScreen(gameScreen.getPane());
	}
	
	public void switchToSettings(){		
		settings.setDisable(true);
		centerPane.getChildren().add(settingsScreen.getPane());
	}	
	
	public void returnBack(){
		settings.setDisable(false);
		centerPane.getChildren().remove(settingsScreen.getPane());
	}
	
	public void switchToSinglePlayerLobby(){
		setScreen(singlePlayerLobbyScreen.getPane());
	}
	
	public void switchToMultiPlayerLobby(){		
		setScreen(multiPlayerLobbyScreen.getPane());

	}	
	public void switchToMultiPlayerOption(){
		setScreen(multiPlayerOptionScreen.getPane());
	}
	
	public void switchToMultiPlayerJoin(){
		setScreen(multiPlayerJoinScreen.getPane());
	}
	
	public void close(){
		thisStage.close();
	}
	
	public void setIsPlaying(boolean bool){
		isPlaying = bool;
	}
	
	public Game getGame(){
		return game;
	}
	
	public void startNewGame(){
		//start new game
		multiPlayerLobbyScreen.addNames();
	}
	
	public boolean checkGame(String ip){
		//check game
		return true;
	}
	
	public void joinGame(String ip){
		//start new game with ip
		multiPlayerLobbyScreen.addNames();
	}
	
	private void sendMoveEvent(String move){
		if(isPlaying){
			if(move=="UP"){
				System.out.println("move up");
				//send up move event
			}
			if(move=="DOWN"){
				//send down move event
				System.out.println("move down");
			}
			if(move=="LEFT"){
				//send left move event
				System.out.println("move left");
			}
			if(move=="RIGHT"){
				//send right move event
				System.out.println("move right");
			}
			if(move=="S"){
				//send change selection event
				System.out.println("swap selection");
			}
			if(move=="ENTER"){
				//send use power up event
				System.out.println("use power up");
			}
			if(move=="D"){
				//send drop power up event
				System.out.println("drop power up");
			}
		}
	}
}
