package teamproject.event.arguments;

public class UserLoggedInEventArgs {

	private String userName;

	public UserLoggedInEventArgs(final String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

}
