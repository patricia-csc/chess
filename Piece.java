import java.math.BigInteger;

public class Piece {
	private BigInteger position;
	private boolean killed;

	public Piece() {}
	
	public Piece(String value) {
		this.position = new BigInteger(value);
		this.killed = false;
	}

	public BigInteger getPosition() {
		return position;
	}

	public void setPosition(BigInteger position) {
		this.position = position;
	}
	
	public boolean isKilled() {
		return killed;
	}

	public void setKilled(boolean killed) {
		this.killed = killed;
	}
	
	void move() {};
}
