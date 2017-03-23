package main.java.event.listener;

/**
 * Represents objects which receive messages when the local user leaves 
 * the multiplayer lobby that they are currently in. This is used so that
 * the socket may disconnect and the server may be made aware that the
 * client is no longer participating.
 * 
 * @author Tom Galvin
 *
 */
public interface UserLeavingLobbyListener {
	/**
	 * Called when the local player is leaving the multiplayer lobby which
	 * they are currently in.
	 */
	public void onUserLeavingLobby();
}
