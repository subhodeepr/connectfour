import java.util.Scanner;

public class GameRoom {
	
	GameLogic gl = new GameLogic();
	String boardDrawing = "Connect Four!\n _ _ _ _ _ _ _\n";
	String[][] board = new String[6][7];
	int user_ascii;
	char base_value = 'A';
	int ascii_value = (int) base_value;
	private String gameRoomName = null;
	private Player player1;
	private Player player2;

	
	public GameRoom(Player p1, String grn){
		
		gameRoomName = grn; 
		player1 = p1;
	}
	
	public GameRoom(Player p1, Player p2, String grn){
		
		gameRoomName = grn; 
		player1 = p1;
		player2 = p2;
	}
	
	// set the initial board values
	public GameRoom(){
		for (int i = 0; i < 6; i++){
			for (int j = 0; j < 7; j++){
				board[i][j] = "_";
			}
		}
	}
	
	// this will draw the board
	public void drawBoard(){
	//hard coded in the size of the connect four board
		for(int i = 0; i < 6; i++){
			for (int j = 0; j < 7; j++){
				boardDrawing = boardDrawing + "|" + board[i][j];
			}
			boardDrawing = boardDrawing + "|\n";

			if ( i == 5 ){
				boardDrawing = boardDrawing + " A B C D E F G\n";
			}
		}
		System.out.println(boardDrawing);
		boardDrawing = "Connect Four!\n _ _ _ _ _ _ _\n";
	}
	
	
	public void updateBoard(String user_move){
		//testing to see if the user can input into the board correctly
		//this user input should be on the client class, just testing this for now
		user_ascii = (int) user_move.charAt(0); // grab the ascii value of input
		if (gl.validateMove(user_move, board)){   //CHANGE TO USER MOVE
			for(int i = 5; i < board.length; i--){
				if(board[i][user_ascii - ascii_value] == "_"){
					if (gl.nextPlayer() % 2 == 0){		// need to double check which player goes first
						board[i][user_ascii - ascii_value] = "X"; //need to get from player token
						break;
					}
					else{
						board[i][user_ascii - ascii_value] = "O"; //need to get from player token
						break;
					}
				}
			}
			gl.checkHorizontal(board);
			gl.checkVertical(board);
			gl.checkDiagonal(board);
		}
		else{
			System.out.println("Incorrect input\n");
		}
		drawBoard();
	}

	public void reconnectPlayer(){
		
	}
	
	public void disconnectPlayer(){
		
	}
	
	public String getGameRoomName(){
		
		return gameRoomName;
	}
	
	public void setPlayer1(Player p){
		player1 = p;
	}
	public void setPlayer2(Player p){
		player2 = p;
	}
	
	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}
}
