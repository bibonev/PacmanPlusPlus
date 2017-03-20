package main.java.gamelogic.domain;

/**
 * PacShield which protects the pacman when first tried to be killed
 *
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
 *
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
        // Set the shield power to 10
        if(getCD() == 40){
            owner.setShield(10);
            setCD(0);
        }
    }
}
