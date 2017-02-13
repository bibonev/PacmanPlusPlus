package teamproject.event.listener;

import teamproject.event.arguments.container.UserLoggedInEventArguments;

public interface UserLoggedInEventListener {
	public void onUserLoggedIn(UserLoggedInEventArguments args);
}
