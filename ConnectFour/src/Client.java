import java.io.*;
import java.net.*;

public class Client {

    public static void main(String args[]) throws Exception {
	if (args.length != 1)
        {
            System.out.println("Usage: Client <Server IP>");
            System.exit(1);
        }
	String address = args[0];
	Socket socket = null;
	String line = null;
	BufferedReader bufferedReader = null;
	BufferedReader inputStream = null;
	PrintWriter outputStream = null;

	try {
	    socket = new Socket(address, 9999); 
	    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	    inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    outputStream = new PrintWriter(socket.getOutputStream());
	    
	    Authentication authentication = new Authentication(inputStream, outputStream, bufferedReader, socket);
	    authentication.authenticate();

	} catch (IOException e) {
	    e.printStackTrace();
	    System.err.print("IO Exception");
	}

	finally {
	    inputStream.close();
	    outputStream.close();
	    bufferedReader.close();
	    socket.close();
	    System.out.println("Logged out from server");

	}

    }

}
