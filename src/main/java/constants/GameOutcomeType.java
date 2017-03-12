package teamproject.constants;

/*
 * Single player or multiplayer game outcomes
 */
public enum GameOutcomeType {
	/**
	 * Used when a player wins. If this is the outcome type, the corresponding
	 * {@link GameOutcome} object must also refer to which player won.
	 */
	PLAYER_WON,
	
	/**
	 * Used when the ghosts won - ie. all of the players were eliminated by
	 * ghosts.
	 */
	GHOSTS_WON,
	
	/**
	 * Used when the game outcome is a tie (this might never be used, we'll
	 * have to see how much time we have..)
	 */
	TIE
}
