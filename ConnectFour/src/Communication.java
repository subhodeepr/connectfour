
public class Communication {
	private String newMessage;

	
	//two parameter message
	public void message(char command, String username, String message){
		newMessage = message;
		
	}

	//three parameter message
	public void message(char command, String message){
		newMessage = message;
	}
}

/**

class Communication{
	timestamp // order messages based on a timestamp
	user // keep track of who says what
	message_text 
}

class Chat { //store,send,receive messages

	Message arrayList; //stores messages as an arraylist
	User userList; //stores the users

	Chat Constructor (User userList)
	{
	Message arrayList = new ArrayList(); //instantiate this
	User list  = userList; //users are passed into constructor from elsewhere (server)
	}

	public recevieMessage(Message)
	{
		if message.user is in User list and message.messagetext isn't blank
			arrayList.add(message) //IF THE MESSAGE VERIFIES VALIDATION, ADD IT TO THE LIST OF MESSAGES

		sendMessage(message);
	}

	public sendMessage(message) 
	{
		loop through the users and send them the message, or something.
	}

	public sendWholeChat() //can send people entire chat history? I guess
	{
		loop through the users and send them teh message array.
	}

}



*/
