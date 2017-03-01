package teamproject.constants;

/**
 * Represents the type of the game.
 * 
 * @author Tom Galvin
 */
public enum GameType {
	/**
	 * Represents a singleplayer game.
	 */
	SINGLEPLAYER,
	
	/**
	 * Represents a multiplayer game which is running on the server.
	 */
	MULTIPLAYER_SERVER,
	
	/**
	 * Represents a multiplayer "game" which is running client-side -
	 * ie. one where no logic is ran on the client, and instead the
	 * logic is ran on the server and then mirrored to the local
	 * machine.
	 */
	MULTIPLAYER_CLIENT
}
