package domainmodel;

public class Coin {
	private final Player owner;

	public Coin(Player owner) {
		this.owner = owner;
	}

	public char getSign() {
		return owner.getSign();
	}
}
