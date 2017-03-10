package teamproject.gamelogic.domain;

/**
 * Represent a remote ghost (used for multiplayer games)
 * 
 * @author aml
 *
 */
public class RemoteGhost extends Ghost {
	public RemoteGhost(final int id) {
		super();
		setID(id);
	}
}
