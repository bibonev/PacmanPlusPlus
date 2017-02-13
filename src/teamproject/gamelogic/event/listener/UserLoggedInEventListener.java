package teamproject.gamelogic.event.listener;

import java.util.Optional;

import teamproject.event.arguments.container.UserLoggedInEventArguments;
import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.repository.Repository;

public class UserLoggedInEventListener implements teamproject.event.listener.UserLoggedInEventListener {

	@Override
	public void onUserLoggedIn(final UserLoggedInEventArguments args) {
		final String userName = args.getUserName();

		// Store user
		Repository.addHumanPlayer(new Player(Optional.of(Repository.nextHumanPlayerId()), userName));
	}

}
