package networking;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;


public class Server implements Runnable {
	
	public static final short PORT = 9001;
	
	public static void main(String[] args) {
		ServerSocket server;
		try {
			server = new ServerSocket(PORT);
			ArrayList<Thread> threads = new ArrayList<Thread>();
			Socket socket = server.accept();
			threads.add(new Thread(new Server(socket)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Server(Socket connection) {

		Socket socket;
		try {
			socket = new Socket(Account.Ip, PORT);
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			Scanner input = new Scanner(socket.getInputStream());
			
			String inputData = input.nextLine();
			System.out.println("> " + inputData);
			String outputData = parseCommand(inputData);
			System.out.println("< " + outputData);
			output.writeBytes(outputData);
			
			input.close();
			output.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String parseCommand(String command) {
		String data[] = command.split("||");
		switch(data[0]) {
		case "REGISTER":
			User user = new User(data[1],PasswordHash.Hash(data[2]), new Timestamp(System.currentTimeMillis()));
			User.AddUser(user);
			return "OK";
		case "LOGIN":
			user = User.GetUser(data[1]);
			if(user!=null) {
				if(PasswordHash.Compare(user.PasswordHash,data[2])) {
					return "OK";
				} else {
					return "Password Incorrect";
				}
			}
			return "Username Does Not Exist";
		}
		return "Unknown Command Error";
	}
	
	public void login(User user) {
		new Multiplayer(user);
	}
	
	@Override
	public void run() {
		
	}
	
	public static Socket SendMessage(String data) {
		try {
			Socket socket = new Socket(Account.Ip, PORT);
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.writeBytes(data);
			output.close();
			return socket;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String WaitMessage(String data) {
		Socket socket = SendMessage(data);
        try {
			Scanner input = new Scanner(socket.getInputStream());
			String back = input.nextLine();
			input.close();
			socket.close();
			return back;
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
}
