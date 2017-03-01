package teamproject.event.listener;

import teamproject.event.arguments.UserLoggedInEventArguments;

public interface UserLoggedInEventListener {
	void onUserLoggedIn(UserLoggedInEventArguments args);
}
