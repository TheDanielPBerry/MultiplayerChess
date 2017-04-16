package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.sql.Timestamp;
import java.util.ArrayList;


public class Server implements Runnable {
	
	public static final int PORT = 9001;
	/**A collection of all the players online*/
	private static ArrayList<User> users = new ArrayList<User>();
	
	private static final byte threadPoolSize = 1;
	
	public static void main(String[] args) {
        ServerSocket socket;
		try {
			socket = new ServerSocket(80);
			for(byte i=0; i<threadPoolSize; i++) {
				Thread t = new Thread(new Server(socket));
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Server(ServerSocket s) {
	}
	
	public void run() {
		while(true) {
			try {
				DatagramSocket socket = new DatagramSocket(PORT);
	            byte[] inBuffer = new byte[512];
	            DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
	            socket.receive(inPacket);
	            inBuffer = inPacket.getData();
	            
	            InetAddress returnAddress = inPacket.getAddress();
	            String inputData = Server.bufferToString(inBuffer);
	            System.out.println("> " + inputData);
	            String outputData = Server.ParseCommand(inputData, inPacket);
	            System.out.println("< " + outputData);
	            
	            byte[] outBuffer = outputData.getBytes();
	            DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, returnAddress, inPacket.getPort());
	            socket.send(outPacket);
	            socket.close();
	            
	            	
//	                Socket connSocket = serverSocket.accept();
//	                Scanner input = new Scanner(connSocket.getInputStream());
//	                DataOutputStream output = new DataOutputStream(connSocket.getOutputStream());
//
//	                // get the message from client
//	                
//	                output.writeBytes(Server.ParseCommand(input.nextLine(), 
//	                		new DatagramPacket(null, 0, connSocket.getInetAddress(), connSocket.getPort())));
//	                
//	                
//	                // close stream and socket
//	                input.close();
//	                output.close();
//	                connSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	public static String ParseCommand(String command, DatagramPacket packet) {
		String data[] = command.split("\\|\\|");
		switch(data[0]) {
		case "REGISTER":
			User user = new User(data[1],PasswordHash.Hash(data[2]), new Timestamp(System.currentTimeMillis()));
			if(User.GetUser(user.Username)==null) {
				User.AddUser(user);
				return "OK";
			}
			return "Username Already Exists";
		case "LOGIN":
			user = User.GetUser(data[1]);
			if(user!=null) {
				if(PasswordHash.Compare(user.PasswordHash, data[2])) {
					user.Port = packet.getPort();
					user.Ip = packet.getAddress();
					users.add(user);
					return "OK||" + user.Port + "||";
				} else {
					return "Password Incorrect";
				}
			}
			return "Username Does Not Exist";
		case "GET_LIST":
			String onlinePlayers = "";
			for(User player : users) {
				if(!player.Username.equals(data[1])) {
					onlinePlayers += player.Username + "||";
				}
			}
			onlinePlayers += "Refresh||";
			return onlinePlayers;
		case "GET_USER":
			user = null;
			for(User player : users) {
				if(player.Username.equals(data[1])) {
					user = player;
				}
			}
			if(user!=null) {
				return user.Username + "||" + user.Port + "||" + user.Ip.toString().replace("/","") + "||";
			}
			return "No Valid User";
		case "LOGOUT":
			user = null;
			for(User player : users) {
				if(player.Username.equals(data[1])) {
					user = player;
				}
			}
			if(user!=null) {
				users.remove(user);
				return "OK";
			}
			return "Logout Failure";
		case "VERIFY":
			return "CONNECTION_PROCEED||";
		}
		return "Unknown Command Error";
	}
	
	
	public static String SendMessage(String data) {
		try {
			DatagramSocket socket = new DatagramSocket();
	        while (true) {
	            InetAddress destAddress = InetAddress.getByName(Account.Ip);
	            byte outBuffer[] = data.getBytes();
	            DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, destAddress, PORT);
	            socket.send(outPacket);
	            
	            
	            byte inBuffer[] = new byte[512];
	            DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
	            socket.receive(inPacket);
	            String back = bufferToString(inPacket.getData());
	            
	            socket.close();
	            return back;
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public static String bufferToString(byte[] buffer) {
		short i=0;
		String result = "";
		while(buffer[i]!=0) {
			result += (char)buffer[i];
			i++;
		}
		return new String(result);
	}
	
}
