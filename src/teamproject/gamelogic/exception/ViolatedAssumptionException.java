package teamproject.gamelogic.exception;

public class ViolatedAssumptionException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ViolatedAssumptionException(final String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		return "Assumption was violated: " + super.getMessage();
	}

}
