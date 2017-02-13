package teamproject.gamelogic.domain;

public abstract class Ghost {

	private String name;
	private Behaviour behavior;

	public Ghost(final Behaviour behavior, final String name) {
		this.name = name;
		this.behavior = behavior;
	}

	public String getName() {
		return name;
	}

	public Behaviour getBehaviour() {
		return behavior;
	}

}
