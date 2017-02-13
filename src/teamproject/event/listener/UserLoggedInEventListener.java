package teamproject.event.listener;

import teamproject.event.arguments.container.UserLoggedInEventArguments;

public interface UserLoggedInEventListener {
	void onUserLoggedIn(UserLoggedInEventArguments args);
}
