import java.io.*;
import java.net.*;

public class Server {

    public static void main(String args[]) {

	Socket s = null;
	ServerSocket ss2 = null;
	System.out.println("ConnectFour server started");
	try {
	    ss2 = new ServerSocket(9999);

	} catch (IOException e) {
	    e.printStackTrace();
	    System.out.println("Server error");

	}

	while (true) {
	    try {
		s = ss2.accept();
		System.out.println("Connection established from " + s.getLocalAddress());
		ServerThread st = new ServerThread(s);
		st.start();

	    }

	    catch (Exception e) {
		e.printStackTrace();
		System.out.println("Connection Error");

	    }
	}

    }
}

class ServerThread extends Thread {

    String line = null;
    BufferedReader inputStream = null;
    PrintWriter outputStream = null;
    Socket s = null;

    public ServerThread(Socket s) {
	this.s = s;
    }

    public void run() {
	try {
	    inputStream = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    outputStream = new PrintWriter(s.getOutputStream());

	} catch (IOException e) {
	    System.out.println("IO error in server thread");
	}

	try {
	    
	    line = inputStream.readLine();
	    while (!line.equalsIgnoreCase("logout")) {
		outputStream.println(line);
		outputStream.flush();
		System.out.println("Response to Client  :  " + line);
		line = inputStream.readLine();
	    }
	} catch (IOException e) {

	    line = this.getName(); // reused String line for getting thread name
	    System.out.println("IO Error/ Client " + line + " terminated abruptly");
	} catch (NullPointerException e) {
	    line = this.getName(); // reused String line for getting thread name
	    System.out.println("Client " + line + " Closed");
	}

	finally {
	    try {
		System.out.println("Connection Closing..");
		if (inputStream != null) {
		    inputStream.close();
		    System.out.println(" Socket Input Stream Closed");
		}

		if (outputStream != null) {
		    outputStream.close();
		    System.out.println("Socket Out Closed");
		}
		if (s != null) {
		    s.close();
		    System.out.println("Socket Closed");
		}

	    } catch (IOException ie) {
		System.out.println("Socket Close Error");
	    }
	} // end finally
    }
}