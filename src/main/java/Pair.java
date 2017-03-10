package teamproject;

public class Pair<T, U> {
	private T left;
	private U right;

	public Pair(T left, U right) {
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
	public boolean equals(Object obj) {
		if(obj instanceof Pair<?, ?>) {
			Pair<?, ?> pair = (Pair<?, ?>)obj;

			boolean leftEqual = pair.left == null ? this.left == null : pair.left.equals(this.left);
			boolean rightEqual = pair.right == null ? this.right == null : pair.right.equals(this.right);

			return leftEqual && rightEqual;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int leftHash = left.hashCode();
		int rightHash = right.hashCode();
		return leftHash ^ ((rightHash >> 16) & 0x0000ffff) ^ ((rightHash << 16) & 0xffff0000);
	}
}
