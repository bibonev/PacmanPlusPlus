package teamproject.gamelogic.domain;

/**
 * Created by boyanbonev on 11/02/2017.
 */
public abstract class Pacman {
    private String name;
    private Behaviour behaviour;

    public Pacman(final Behaviour behaviour, final String name) {
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
