package teamproject.gamelogic.event.listener;

import teamproject.constants.GameType;
import teamproject.event.arguments.NewGameRequestedEventArguments;
import teamproject.event.listener.NewGameRequestedEventListener;
import teamproject.gamelogic.core.GameCommandService;

/**
 * Created by Simeon Kostadinov on 24/02/2017.
 */

public class NewGameRequestedListener implements NewGameRequestedEventListener {

    private GameCommandService gameCommandService;

    public NewGameRequestedListener() {
        gameCommandService = new GameCommandService();
    }

    @Override
 	public void onNewGameRequested(final NewGameRequestedEventArguments args) {
        if (args.getType().equals(GameType.SINGLEPLAYER)) {
                gameCommandService.startNewSingleplayerGame(args.getUserName(), args.getSettings(), args.getStage());
            } else {
                gameCommandService.requestNewMultiplayerGame(args.getUserName(), args.getSettings(), args.getStage());
            }
    }

}