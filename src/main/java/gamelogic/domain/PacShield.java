package main.java.gamelogic.domain;

/**
 * PacShield which protects the pacman when first tried to be killed
 *
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
 *
 */
public class PacShield extends Ability {
    public static final int MAX_SHIELD = 4;
    public static final int SHIELD_COOLDOWN = 40;

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
            setShieldValue(MAX_SHIELD);
            setCD(0);
        }
    }
	@Override
	public boolean incrementCooldown() {
        int cooldown = getCD();
		if(cooldown < SHIELD_COOLDOWN && getOwner().getSkillSet().getW().getShieldValue() == 0) {
			setCD(cooldown + 1);
			return true;
		} else {
			return false;
		}
	}

    @Override
    public void reduceShieldValue() {
        int shieldValue = getShieldValue();
        if(shieldValue > 0){
            setShieldValue(shieldValue - 1);
        }
    }
}
