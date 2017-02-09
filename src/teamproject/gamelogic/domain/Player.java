package teamproject.gamelogic.domain;

public class Player {

	private long id;
	private String name;

	public Player(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

}
