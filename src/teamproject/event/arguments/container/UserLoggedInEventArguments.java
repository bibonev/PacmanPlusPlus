package teamproject.event.arguments.container;

public class UserLoggedInEventArguments {

	private String userName;

	public UserLoggedInEventArguments(final String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

}