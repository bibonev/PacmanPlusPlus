package teamproject.gamelogic.domain;

public class Ghost {

	private String name;
	private Behaviour behaviour;

	public Ghost(final Behaviour behaviour, final String name) {
		this.name = name;
		this.behaviour = behaviour;
	}

	public String getName() {
		return name;
	}

	public Behaviour getBehaviour() {
		return behaviour;
	}

}
