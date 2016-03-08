
public class GameLogic {
	private Boolean win_state = false;
	private int numberOfTurns = 1;
	private Boolean pause_game;

	// return if move is valid or not
	public Boolean validateMove(String move){
		int user_input = (int) move.charAt(0);
		char base_value = 'A';
		int ascii_value = (int) base_value;
		
		if ((user_input - ascii_value) <= 6){
			numberOfTurns++;
			return true;
		}
		else
			return false;
		

	}
	// if number is even player 1 goes, if number is odd player 2 
	public int nextPlayer(){
		return numberOfTurns;
	}
	
	public Boolean checkForWinner(){
		return win_state;
	}
	
	public void checkHorizontal(String[][] board){ // add char player_token
		for(int i = 0; i < 6; i++){
			int count_tokens = 0;
			for (int j = 0; j < 7; j++){
				if (board[i][j].equals("X")){	// player_token goes here to check for who wins
					count_tokens = count_tokens + 1;
					if (count_tokens == 4){
						win_state = true;
					}
				}
				else{
					count_tokens = 0;
				}
			}
		}
	}
	
	public void checkVertical(String[][] board){
		for(int i = 0; i < 7; i++){
			int count_tokens = 0;
			for (int j = 0; j < 6; j++){
				if (board[j][i].equals("X")){	// player_token goes here to check for who wins
					count_tokens = count_tokens + 1;
					if (count_tokens == 4){
						win_state = true;
					}
				}
				else{
					count_tokens = 0;
				}
			}
		}
	}
	
	public void checkDiagonal(String[][] board){
		
	}
	
}
