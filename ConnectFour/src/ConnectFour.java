import java.util.Scanner;

public class ConnectFour {
	private int gameState;

	public static void main(String[] args){
		GameRoom gr = new GameRoom();
		gr.drawBoard();
		String user_move = "";
		// must change move to what server supplies
		
		while (!(gr.gl.checkForWinner())){         //checkForWinner should be parameter for the while loop
			Scanner user_input = new Scanner(System.in);
			System.out.println("Enter your move: ");
			user_move = user_input.nextLine();
			gr.updateBoard(user_move);
		}
		
		// kill thread
	}
}
