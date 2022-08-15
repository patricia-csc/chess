import java.io.*;
import java.math.BigInteger;

public class Main {
	
	static boolean isAMove(String cmd) {
		char c1, c2;
		if (cmd.length() > 2) {
			c1 = cmd.charAt(0);
			c2 = cmd.charAt(2); 
		} else return false;
			 
		if(c1 < 'a' || c1 > 'h' || c2 < 'a' || c2 > 'h' )
			return false;
		c1 = cmd.charAt(1);
		c2 = cmd.charAt(3);
		if(c1 < '1' || c1 > '8' || c2 < '1' || c2 > '8' )
			return false;
		return true;
	}
	
    public static void main(String[] args) {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Player engine = new Player("black");
		Player opponent = new Player("white");
		Boolean force_mode = false;
		int turn = 1; // 1 for white, -1 for black

		try {
			for(;;) {
				// handle command
				String command = input.readLine();
				if (command == null) {
					return;
				} else if (command.contains("protover")) {
					System.out.println("feature sigint=0");
				} else if (command.equals("xboard")) {
					continue;
				} else if (command.equals("force")) {
					force_mode = true;
				} else if (command.equals("go")) {
					force_mode = false;
					
					// make engine play with the desired colour
					if((turn == -1 && engine.getColour().equals("white")) ||
						(turn == 1 && engine.getColour().equals("black"))) {
						Player aux = engine;
						engine = opponent;
						opponent = aux;
					}
					System.out.println(engine.tryMoves(opponent));
					turn *= -1;
				}  else if (command.equals("black")) {
					if(engine.getColour().equals("black")) {
						Player aux = engine;
						engine = opponent;
						opponent = aux;
					}
					turn = -1;
				} else if (command.equals("white")) {
					if(engine.getColour().equals("white")) {
						Player aux = engine;
						engine = opponent;
						opponent = aux;
					}
					turn = 1;
				} else if (command.equals("new")) {
					engine = new Player("black");
					opponent = new Player("white");
					force_mode = false;
					turn = 1;
				} else if (command.equals("quit")) {
					return;
				} else if (force_mode == false) {
					// modify the opponent's board, make a move and eliminate a piece if necessary
					if (isAMove(command)) {
						opponent.modifyBoard(command,engine);
						BigInteger oldBoard = opponent.getBoard();
						System.out.println(engine.tryMoves(opponent));
                		if(oldBoard.equals(opponent.getBoard()) != true) {
                			opponent.eliminatePiece(oldBoard.subtract(opponent.getBoard()));
                		}
					}
					turn *= -1;
				}
				else if(isAMove(command)) {
					// execute command without making a move on its own
					if(turn == 1)
						if(engine.getColour().equals("white"))
							engine.modifyBoard(command, opponent);
						else opponent.modifyBoard(command, engine);
					else if(engine.getColour().equals("white"))
							opponent.modifyBoard(command, engine);
						else engine.modifyBoard(command, opponent);
					turn *= -1;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}