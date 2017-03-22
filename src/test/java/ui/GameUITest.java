package test.java.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.java.event.Event;
import main.java.event.arguments.LobbyChangedEventArgs;
import main.java.event.listener.LobbyStateChangedListener;
import main.java.gamelogic.core.LobbyPlayerInfo;
import main.java.ui.GameUI;
import main.java.ui.HelpScreen;
import main.java.ui.LogInScreen;
import main.java.ui.MenuScreen;
import main.java.ui.MultiPlayerJoinScreen;
import main.java.ui.MultiPlayerLobbyScreen;
import main.java.ui.MultiPlayerOptionScreen;
import main.java.ui.PlayersList;
import main.java.ui.Screen;
import main.java.ui.SettingsScreen;
import main.java.ui.SinglePlayerLobbyScreen;

public class GameUITest {
	@InjectMocks private GameUI game;
	
	@Mock private LogInScreen logInScreen = new LogInScreen(game);
	@Mock private Screen currentScreen = logInScreen;
	@Spy private MultiPlayerLobbyScreen multiPlayerLobbyScreen = new MultiPlayerLobbyScreen(game);
	@Mock private StackPane centerPane = new StackPane();
	@Spy private Button settings = new Button();
	@Spy private BorderPane banner = new BorderPane();
	
	private LobbyPlayerInfo player = new LobbyPlayerInfo(1,"Name");
	private ObservableList<Node> nodeList = FXCollections.observableArrayList();
	private LobbyChangedEventArgs addArgs = new LobbyChangedEventArgs.LobbyPlayerJoinedEventArgs(1, player);
	private LobbyChangedEventArgs removeArgs = new LobbyChangedEventArgs.LobbyPlayerLeftEventArgs(1);

	@Spy private Stage stage;
	
	public static class UIMockJavaFx extends Application {
		@Override
		public void start(final Stage primaryStage) throws Exception {

		}
	}

	@BeforeClass
	public static void initJFX() throws InterruptedException {
		final Thread t = new Thread("JavaFX Init Thread") {
			@Override
			public void run() {
				Application.launch(UIMockJavaFx.class, new String[0]);
			}
		};
		t.setDaemon(true);
		t.start();
		Thread.sleep(500);
	}

	@Before
	public void setUp() throws Exception {
		game = mock(GameUI.class);
		stage = mock(Stage.class);
		game.start(stage);
				
		MockitoAnnotations.initMocks(this);
		Mockito.doCallRealMethod().when(game).switchToMultiPlayerLobby();
		Mockito.doCallRealMethod().when(game).switchToLogIn();
		Mockito.doCallRealMethod().when(game).onLobbyStateChanged(addArgs);
		Mockito.doCallRealMethod().when(game).onLobbyStateChanged(removeArgs);		
		Mockito.when(centerPane.getChildren()).thenReturn(nodeList);
	}

	@Test
	public void testSwitchMulti(){
		game.switchToMultiPlayerLobby();
		assertFalse(game.currentScreen == null);
		assertFalse(game.multiPlayerLobbyScreen == null);
		assertTrue(game.currentScreen == game.multiPlayerLobbyScreen);
	}
	
	@Test
	public void testSwitchLogIn(){
		game.switchToLogIn();
		assertFalse(game.currentScreen == null);
		assertFalse(game.logInScreen == null);
		assertTrue(game.currentScreen == game.logInScreen);
	}
	
	@Test
	public void testLobby(){		
		Event<LobbyStateChangedListener, LobbyChangedEventArgs> event = new Event<>((l, a) -> l.onLobbyStateChanged(a));
		event.addListener(game);
		
		event.fire(addArgs);
		System.out.println(game.multiPlayerLobbyScreen);
		assertTrue(game.multiPlayerLobbyScreen.list.contains(1));
		event.fire(removeArgs);
		assertFalse(game.multiPlayerLobbyScreen.list.contains(2));
	}
}