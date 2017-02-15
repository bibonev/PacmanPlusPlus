package teamproject.gamelogic.event.listener;

import teamproject.constants.GameType;
import teamproject.event.arguments.container.NewGameRequestedEventArguments;
import teamproject.gamelogic.core.GameCommandService;

public class NewGameRequestedEventListener implements teamproject.event.listener.NewGameRequestedEventListener {

	private GameCommandService gameCommandService;

	public NewGameRequestedEventListener() {
		gameCommandService = new GameCommandService();
	}

	@Override
	public void onNewGameRequested(final NewGameRequestedEventArguments args) {
		if (args.getType().equals(GameType.SINGLEPLAYER)) {
			gameCommandService.startNewSingleplayerGame(args.getUserName(), args.getSettings(), args.getStage());
		} else {
			gameCommandService.requestNewMultiplayerGame(args.getUserName(), args.getSettings());
		}
	}

}
