import java.math.BigInteger;

public class Pawn extends Piece{
	
	public Pawn() {}
	public Pawn(String value) {
		super(value);
	}
	
	void move(String colour) {
		if(colour.equals("black"))
			setPosition(getPosition().shiftRight(8));
		else
			setPosition(getPosition().shiftLeft(8));
	}
	
	BigInteger attackLeft(String colour) {
		if(colour.equals("black"))
			return getPosition().shiftRight(7);
		else
			return getPosition().shiftLeft(9);
		
	}
	
	BigInteger attackRight(String colour) {
		if(colour.equals("black"))
			return getPosition().shiftRight(9);
		else
			return getPosition().shiftLeft(7);
	}
}
