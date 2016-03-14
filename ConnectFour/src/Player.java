
public class Player {
	private String username;
	private int wins;
	private int losses;
	private Boolean gameStatus;
	private String banList[];
	private String unbanList[];
	private Boolean connected;
	private char token;
	
	public Player(String name){
		username = name; 	

	}
	
	public String getUserName(){
		return username;
	}
	public int getLoses(){
		return losses;
	}
	public int getWins(){
		return wins;
	}
	public Boolean getStatus(){
		return gameStatus;
	}
	public Boolean acceptChallenge(){
		return true;
	}
	
	public void setPlayerToken(char player_token){
		token = player_token;
	}
	public char returnPlayerToken(){
		return token;
	}
}
