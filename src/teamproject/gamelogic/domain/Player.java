package teamproject.gamelogic.domain;

import java.util.Optional;

public class Player {

	private Optional<Long> id;
	private String name;

	public Player(final Optional<Long> id, final String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Optional<Long> getId() {
		return id;
	}

}
