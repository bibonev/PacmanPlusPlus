package teamproject.gamelogic.domain;

public abstract class Behaviour {

	public enum Type {
		BASIC
	}

	private Type type;

	public Behaviour(final Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

}
