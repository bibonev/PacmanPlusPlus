package main.java;

public class Pair<T, U> {
	private T left;
	private U right;

	public Pair(final T left, final U right) {
		this.left = left;
		this.right = right;
	}

	public T getLeft() {
		return left;
	}

	public U getRight() {
		return right;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", left, right);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Pair<?, ?>) {
			final Pair<?, ?> pair = (Pair<?, ?>) obj;

			final boolean leftEqual = pair.left == null ? this.left == null : pair.left.equals(this.left);
			final boolean rightEqual = pair.right == null ? this.right == null : pair.right.equals(this.right);

			return leftEqual && rightEqual;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int leftHash = left.hashCode();
		final int rightHash = right.hashCode();
		return leftHash ^ rightHash >> 16 & 0x0000ffff ^ rightHash << 16 & 0xffff0000;
	}
}
