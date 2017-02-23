package teamproject.ai;
import teamproject.gamelogic.domain.*;

/**
 * The AI Ghost.
 * @author Lyubomir Pashev
 */
public class AIGhost extends LocalGhost {
	private Behaviour behaviour;

	/**
	 * Instantiates a new ghost.
	 *
	 * @param id entity ID
	 */
	public AIGhost() {
		super();
	}

	public Behaviour getBehaviour() {
		return behaviour;
	}
	
	public void setBehaviour(Behaviour behaviour) {
		this.behaviour = behaviour;
	}

	public void run() {
		behaviour.run();
		
	}
}
