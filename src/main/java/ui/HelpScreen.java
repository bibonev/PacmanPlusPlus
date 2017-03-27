package main.java.ui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextAlignment;

public class HelpScreen extends Screen {

	private BorderPane bPane;
	private FlowPane leftPane;
	private FlowPane centrePane;
	private FlowPane rightPane;
	private FlowPane topPane;

	private Button returnButton;
	private Label label;
	private Label play;
	private Label multi;
	private Label sound;
	private Label joinMulti;

	private Label soundDesc1;
	private Label soundDesc2;
	private Label playDesc;
	private Label multiDesc1;
	private Label multiDesc2;
	private Label multiDesc3;
	private Label multiDesc4;
	private Label multiDesc5;
	private Label multiDesc5a;
	private Label multiDesc6;
	private Label multiDesc7;
	private Label multiDesc8;
	private Label multiDesc9;
	private String soundText1;
	private String soundText2;
	private String playText;
	private String multiText1;
	private String multiText2;
	private String multiText3;
	private String multiText4;
	private String multiText5;
	private String multiText5a;
	private String multiText6;
	private String multiText7;
	private String multiText8;
	private String multiText9;
	private Label settings;
	private Label gameSettings;
	private Label multiplayer;
	private Label multiplayer2;
	private Label start;
	private Label start2;
	private Label startNew;
	private Label join;
	private Label leave;

	public HelpScreen(final GameUI game) {
		super(game);

		bPane = new BorderPane();
		bPane.getStyleClass().add("paneStyle");

		leftPane = new FlowPane();
		leftPane.setPadding(new Insets(5, 0, 5, 0));
		leftPane.setVgap(4);
		leftPane.setHgap(4);
		leftPane.setColumnHalignment(HPos.CENTER);
		leftPane.setOrientation(Orientation.VERTICAL);
		leftPane.getStyleClass().add("paneStyle");
		leftPane.setAlignment(Pos.TOP_CENTER);

		centrePane = new FlowPane();
		centrePane.setPadding(new Insets(5, 0, 5, 0));
		centrePane.setVgap(4);
		centrePane.setHgap(4);
		centrePane.setColumnHalignment(HPos.CENTER);
		centrePane.setOrientation(Orientation.VERTICAL);
		centrePane.getStyleClass().add("paneStyle");
		centrePane.setAlignment(Pos.TOP_CENTER);

		rightPane = new FlowPane();
		rightPane.setPadding(new Insets(5, 0, 5, 0));
		rightPane.setVgap(4);
		rightPane.setHgap(4);
		rightPane.setColumnHalignment(HPos.CENTER);
		rightPane.setOrientation(Orientation.VERTICAL);
		rightPane.getStyleClass().add("paneStyle");
		rightPane.setAlignment(Pos.TOP_CENTER);

		topPane = new FlowPane();
		topPane.setPadding(new Insets(5, 0, 5, 0));
		topPane.setVgap(4);
		topPane.setHgap(4);
		topPane.setColumnHalignment(HPos.CENTER);
		topPane.setOrientation(Orientation.VERTICAL);
		topPane.getStyleClass().add("paneStyle");
		topPane.setAlignment(Pos.TOP_CENTER);

		setText();

		returnButton = new Button("Return");
		returnButton.getStyleClass().add("backButtonStyle");
		returnButton.setOnAction(e -> game.switchToMenu());
		select(returnButton);

		label = new Label("Help");
		label.getStyleClass().add("miniTitleStyle");

		sound = new Label("Sound settings");
		sound.getStyleClass().add("headingStyle");

		soundDesc1 = new Label(soundText1);
		soundDesc1.getStyleClass().add("textStyle");
		soundDesc1.setTextAlignment(TextAlignment.CENTER);
		soundDesc2 = new Label(soundText2);
		soundDesc2.getStyleClass().add("textStyle");
		soundDesc2.setTextAlignment(TextAlignment.CENTER);

		play = new Label("How to play in the game");
		play.getStyleClass().add("headingStyle");

		playDesc = new Label(playText);
		playDesc.getStyleClass().add("textStyle");
		playDesc.setTextAlignment(TextAlignment.CENTER);

		multi = new Label("How to start a multiplayer game");
		multi.getStyleClass().add("headingStyle");

		joinMulti = new Label("How to join a multiplayer game");
		joinMulti.getStyleClass().add("headingStyle");

		multiDesc1 = new Label(multiText1);
		multiDesc1.getStyleClass().add("textStyle");
		multiDesc1.setTextAlignment(TextAlignment.CENTER);
		multiDesc2 = new Label(multiText2);
		multiDesc2.getStyleClass().add("textStyle");
		multiDesc2.setTextAlignment(TextAlignment.CENTER);
		multiDesc3 = new Label(multiText3);
		multiDesc3.getStyleClass().add("textStyle");
		multiDesc3.setTextAlignment(TextAlignment.CENTER);
		multiDesc4 = new Label(multiText4);
		multiDesc4.getStyleClass().add("textStyle");
		multiDesc4.setTextAlignment(TextAlignment.CENTER);
		multiDesc5 = new Label(multiText5);
		multiDesc5.getStyleClass().add("textStyle");
		multiDesc5.setTextAlignment(TextAlignment.CENTER);
		multiDesc5a = new Label(multiText5a);
		multiDesc5a.getStyleClass().add("textStyle");
		multiDesc5a.setTextAlignment(TextAlignment.CENTER);
		multiDesc6 = new Label(multiText6);
		multiDesc6.getStyleClass().add("textStyle");
		multiDesc6.setTextAlignment(TextAlignment.CENTER);
		multiDesc7 = new Label(multiText7);
		multiDesc7.getStyleClass().add("textStyle");
		multiDesc7.setTextAlignment(TextAlignment.CENTER);
		multiDesc8 = new Label(multiText8);
		multiDesc8.getStyleClass().add("textStyle");
		multiDesc8.setTextAlignment(TextAlignment.CENTER);
		multiDesc9 = new Label(multiText9);
		multiDesc9.getStyleClass().add("textStyle");
		multiDesc9.setTextAlignment(TextAlignment.CENTER);

		settings = new Label("Settings");
		settings.getStyleClass().add("buttonImageStyle");
		settings.setTextAlignment(TextAlignment.CENTER);

		gameSettings = new Label("Change Game Settings");
		gameSettings.getStyleClass().add("buttonImageStyle");
		gameSettings.setTextAlignment(TextAlignment.CENTER);

		multiplayer = new Label("Multiplayer");
		multiplayer.getStyleClass().add("buttonImageStyle");
		multiplayer.setTextAlignment(TextAlignment.CENTER);
		multiplayer2 = new Label("Multiplayer");
		multiplayer2.getStyleClass().add("buttonImageStyle");
		multiplayer2.setTextAlignment(TextAlignment.CENTER);

		join = new Label("Join game");
		join.getStyleClass().add("buttonImageStyle");
		join.setTextAlignment(TextAlignment.CENTER);

		start = new Label("Start Game");
		start.getStyleClass().add("buttonImageStyle");
		start.setTextAlignment(TextAlignment.CENTER);
		start2 = new Label("Start Game");
		start2.getStyleClass().add("buttonImageStyle");
		start2.setTextAlignment(TextAlignment.CENTER);

		startNew = new Label("Start new game");
		startNew.getStyleClass().add("buttonImageStyle");
		startNew.setTextAlignment(TextAlignment.CENTER);

		leave = new Label("Leave Game");
		leave.getStyleClass().add("buttonImageStyle");
		leave.setTextAlignment(TextAlignment.CENTER);

		final Separator separator = new Separator();
		separator.getStyleClass().add("separator");
		topPane.getChildren().addAll(label);
		leftPane.getChildren().addAll(sound, soundDesc1, settings, soundDesc2, play, playDesc);
		centrePane.getChildren().addAll(multi, multiDesc1, multiplayer, multiDesc2, startNew, multiDesc3, gameSettings,
				multiDesc4, start, multiDesc5);
		rightPane.getChildren().addAll(joinMulti, multiDesc5a, multiplayer2, multiDesc6, join, multiDesc7, leave,
				multiDesc8, start2, multiDesc9);

		bPane.setTop(label);
		bPane.setLeft(leftPane);
		bPane.setCenter(centrePane);
		bPane.setRight(rightPane);
		bPane.setBottom(returnButton);

		bPane.setAlignment(label, Pos.CENTER);
		bPane.setAlignment(returnButton, Pos.CENTER);
		bPane.setMargin(centrePane, new Insets(0, 5, 0, 5));

		pane.getChildren().add(bPane);
	}

	private void setText() {
		soundText1 = "To turn off the sound effects or music, select";
		soundText2 = "in the top right hand corner,\n" + "and select the checkboxes as desired\n" + "-\n";

		playText = "To move, about use the arrows on the key board. \n" + "-\n" + "Once the game is over, you can\n"
				+ "play again by pressing space\n" + "or return to the menu screen\n by pressing esc.\n" + "-\n"
				+ "Press Q to shoot.\n" + "-\n" + "Press W to activate your shield.\n" + "-\n"
				+ "To pause when playing the game press esc.\n-\n";

		multiText1 = "To start a multiplayer game, select";
		multiText2 = "in the main menu, then";
		multiText3 = "Players will appear as they join. " + "\n-\n" + "Select";
		multiText4 = "to edit the game settings. " + "\n-\n" + "Select";
		multiText5 = "to start the game and a countdown will begin.\n-\n";
		multiText5a = "To join a multiplayer game, select";
		multiText6 = "in the main menu, then";
		multiText7 = "-\n" + "Input the IP address of the host of the game\n"
				+ "and you will be taken to the game lobby.\n" + "You can leave the game by pressing";
		multiText8 = "When the host of the game has selected";
		multiText9 = "a countdown will begin.\n" + "-\n";
	}

	@Override
	public void changeSelection(final boolean up) {
		// only one selection

	}

	@Override
	public void makeSelection() {
		game.switchToMenu();
	}

	@Override
	public void unselectAll() {
		// only one selection

	}

}
