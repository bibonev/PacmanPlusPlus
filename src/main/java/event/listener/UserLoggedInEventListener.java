package main.java.event.listener;

import main.java.event.arguments.UserLoggedInEventArgs;

public interface UserLoggedInEventListener {
	void onUserLoggedIn(UserLoggedInEventArgs args);
}
