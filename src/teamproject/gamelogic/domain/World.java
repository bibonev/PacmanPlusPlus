package teamproject.gamelogic.domain;

import java.util.Collection;

public class World {

	private Collection<Player> players;
	private Collection<Ghost> ghosts;
	private Map map;

	public World(final Collection<Player> players, final Collection<Ghost> ghosts, final Map map) {
		this.players = players;
		this.ghosts = ghosts;
		this.map = map;
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public Collection<Ghost> getGhosts() {
		return ghosts;
	}

	public Map getMap() {
		return map;
	}

	public void setPlayers(final Collection<Player> players) {
		this.players = players;
	}

	public void setGhosts(final Collection<Ghost> ghosts) {
		this.ghosts = ghosts;
	}

	public void setMap(final Map map) {
		this.map = map;
	}

}
