package teamproject.event.listener;

import teamproject.event.arguments.UserLoggedInEventArgs;

public interface UserLoggedInEventListener {
	void onUserLoggedIn(UserLoggedInEventArgs args);
}
