package test.java.gamelogic.domain.stubs;

import main.java.gamelogic.domain.Ability;
import test.java.gamelogic.random.Randoms;

public class AbilityStub extends Ability {

	public AbilityStub(final String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean incrementCooldown() {
		return Randoms.randomBoolean();
	}

	@Override
	public void reduceShieldValue() {

	}

}
