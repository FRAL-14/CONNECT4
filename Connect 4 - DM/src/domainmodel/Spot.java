package domainmodel;

public class Spot {
	private int row;
	private int col;
	private Coin coin;

	public Spot(int row, int col, Coin coin) {
		this.row = row;
		this.col = col;
		this.coin = coin;
	}

	public void setCoin(Coin coin) {
		this.coin = coin;
	}

	public Coin getCoin() {
		return coin;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}
}
