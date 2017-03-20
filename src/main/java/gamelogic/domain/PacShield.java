package main.java.gamelogic.domain;

/**
 * PacShield which protects the pacman when first tried to be killed
 *
 * Created by Simeon Kostadinov on 10/03/2017.
 */
public class PacShield extends Ability {

	public PacShield() {
		super("PacShield");
	}

	/**
	 * Activate the shield by changing the shield variable to true
	 */
	@Override
	public void activate() {
		final boolean shield = owner.getShield();
		if (!shield) {
			owner.setShield(true);
		}
	}

	/**
	 * Deactivate the shield by changing the shield variable to false
	 */
	public void deactivate() {
		final boolean shield = owner.getShield();
		if (shield) {
			owner.setShield(false);
		}
	}
}
