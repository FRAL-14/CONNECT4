package game;

public class Coin {
	private final Player owner;

	public Coin(Player owner) {
		this.owner = owner;
	}

	public char getSign() {
		return owner.getSign();
	}

	@Override
	public String toString() {
		return String.format("%c", getSign());
	}
}
