import java.math.BigInteger;

public class Player {
	private BigInteger board;
	private Piece[] pieces = new Piece[16];
	private String colour;
	
	public Player() {}
	
	public Player(String colour) {
		this.colour = colour;
		if(colour.equals("black")) {
			this.board = new BigInteger("18446462598732840960");
			pieces[0] = new Pawn("36028797018963968");
			pieces[1] = new Pawn("18014398509481984");
			pieces[2] = new Pawn("9007199254740992");
			pieces[3] = new Pawn("4503599627370496");
			pieces[4] = new Pawn("2251799813685248");
			pieces[5] = new Pawn("1125899906842624");
			pieces[6] = new Pawn("562949953421312");
			pieces[7] = new Pawn("281474976710656");
			pieces[8] = new Rook("9223372036854775808");
			pieces[9] = new Knight("4611686018427387904");
			pieces[10] = new Bishop("2305843009213693952");
			pieces[11] = new Queen("1152921504606846976");
			pieces[12] = new King("576460752303423488");
			pieces[13] = new Bishop("288230376151711744");
			pieces[14] = new Knight("144115188075855872");
			pieces[15] = new Rook("72057594037927936");
			
		}
		else {
			this.board = new BigInteger("65535");
			pieces[0] = new Pawn("32768");
			pieces[1] = new Pawn("16384");
			pieces[2] = new Pawn("8192");
			pieces[3] = new Pawn("4096");
			pieces[4] = new Pawn("2048");
			pieces[5] = new Pawn("1024");
			pieces[6] = new Pawn("512");
			pieces[7] = new Pawn("256");
			pieces[8] = new Rook("128");
			pieces[9] = new Knight("64");
			pieces[10] = new Bishop("32");
			pieces[11] = new Queen("16");
			pieces[12] = new King("8");
			pieces[13] = new Bishop("4");
			pieces[14] = new Knight("2");
			pieces[15] = new Rook("1");
		}
	}
	
	public Piece[] getPieces() {
		return pieces;
	}

	public void setPieces(Piece[] pieces) {
		this.pieces = pieces;
	}

	public BigInteger getBoard() {
		return board;
	}

	public void setBoard(BigInteger board) {
		this.board = board;
	}
	
	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}


	String convert(BigInteger pos) {
		String posEquivalent = null;
		int i;
		for(i = 0; i < 64; i++) {
			BigInteger s = BigInteger.valueOf((long)(Math.pow(2, i)));
			if(s.equals(pos))
				break;
		}
		switch(i % 8) {
		case 7:
			posEquivalent = "a";
			break;
		case 6:
			posEquivalent = "b";
			break;
		case 5:
			posEquivalent = "c";
			break;
		case 4:
			posEquivalent = "d";
			break;
		case 3:
			posEquivalent = "e";
			break;
		case 2:
			posEquivalent = "f";
			break;
		case 1:
			posEquivalent = "g";
			break;
		case 0:
			posEquivalent = "h";
			break;
		}
		posEquivalent += i/8 + 1;
		return posEquivalent;
	}
	
	BigInteger convertToBigInteger(String cmd) {
		BigInteger pos = new BigInteger("1");
		switch(cmd.charAt(0)) {
		case 'a':
			pos = pos.shiftLeft(7);
			break;
		case 'b':
			pos = pos.shiftLeft(6);
			break;
		case 'c':
			pos = pos.shiftLeft(5);
			break;
		case 'd':
			pos = pos.shiftLeft(4);
			break;
		case 'e':
			pos = pos.shiftLeft(3);
			break;
		case 'f':
			pos = pos.shiftLeft(2);
			break;
		case 'g':
			pos = pos.shiftLeft(1);
			break;
		}
		pos = pos.shiftLeft(8 * (Character.getNumericValue(cmd.charAt(1)) - 1));
		return pos;
	}
	
	public void eliminatePiece(BigInteger pos) {
		int i;
		for(i = 0; i < 16; i++) {
			BigInteger aux = pieces[i].getPosition();
			if(aux.equals(pos))
				break;
		}
		if(i != 16)
			pieces[i].setKilled(true);
	}
	
	public void modifyBoard(String command, Player opponent) {
		//make the old square free
		BigInteger oldPosition = convertToBigInteger(command.substring(0,2));
		board = board.subtract(oldPosition);
		
		// occupy the new square
		BigInteger newPosition = convertToBigInteger(command.substring(2,4));
		for(int i = 0; i < pieces.length; i++) {
			if(pieces[i].getPosition().equals(oldPosition)) {
				pieces[i].setPosition(newPosition);
				break;
			}
		}
		board = board.add(newPosition);
		
		//eliminate piece if necessary
		if(board.and(opponent.getBoard()).equals(new BigInteger("0")) != true) {
			opponent.eliminatePiece(newPosition);
			opponent.setBoard(opponent.getBoard().subtract(newPosition));
		}
	}
	
	public String tryMoves(Player opponent) {
		Piece piece = pieces[0];
		
		// check if the pawn was captured
		if(piece.isKilled() == true)
			System.out.println("resign");
		
		// check if the pawn is on the first or the last line
		if(convert(piece.getPosition()).charAt(1) >= '8' || 
			convert(piece.getPosition()).charAt(1) <= '1')
			return "resign";
		
		
		BigInteger opponentBoard = opponent.getBoard();
		BigInteger oldPosition = piece.getPosition();
		String res = "move " + convert(piece.getPosition());
		board = board.subtract(piece.getPosition());
		
		// try to move to the square directly in front
		((Pawn)piece).move(this.colour);
		
		// if the square is occupied, try to attack another piece
		if(opponentBoard.and(piece.getPosition()).equals(new BigInteger("0")) != true
			|| this.board.and(piece.getPosition()).equals(new BigInteger("0")) != true) {
			piece.setPosition(oldPosition);
			
			// try to attack the left square
			BigInteger attackPos = ((Pawn)piece).attackLeft(this.colour);
			
			// check if the left square is clear and the piece can move to left
			if(opponentBoard.and(attackPos).equals(new BigInteger("0")) != true &&
				this.board.and(attackPos).equals(new BigInteger("0")) != true &&
				convert(attackPos).charAt(0) != 'a') {
					piece.setPosition(attackPos);
					opponent.setBoard(opponentBoard.subtract(attackPos));
			}
			else {
				// try to attack the right square
				attackPos = ((Pawn)piece).attackRight(this.colour);
				
				//check if the right square is clear and the piece can move to right
				if(opponentBoard.and(attackPos).equals(new BigInteger("0")) != true &&
					convert(attackPos).charAt(0) != 'h') {
						piece.setPosition(attackPos);
						opponent.setBoard(opponentBoard.subtract(attackPos));
				}
				else return "resign";

		}
			
		}
		res += convert(piece.getPosition());
		board = board.add(piece.getPosition());
		return res;
	}
}
