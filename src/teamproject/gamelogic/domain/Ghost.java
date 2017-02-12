package teamproject.gamelogic.domain;

public abstract class Ghost {

	private String name;
	private Behavior behavior;

	public Ghost(final Behavior behavior, final String name) {
		this.name = name;
		this.behavior = behavior;
	}

	public String getName() {
		return name;
	}

	public Behavior getBehavior() {
		return behavior;
	}

}
