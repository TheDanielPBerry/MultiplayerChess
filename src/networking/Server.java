package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Base64;


public class Server {
	
	public static final short PORT = 9001;
	
	public static void main(String[] args) {
		try {
			DatagramSocket socket = new DatagramSocket(PORT);
	        while (true) {
	            byte[] buffer = new byte[512];
	            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
	            socket.receive(inPacket);
	            
	            InetAddress returnAddress = inPacket.getAddress();
	            String inputData = Server.bufferToString(buffer);
	            System.out.println("> " + inputData);
	            String outputData = parseCommand(inputData);
	            System.out.println("< " + outputData);
	            
	            buffer = outputData.getBytes();
	            DatagramPacket outPacket = new DatagramPacket(buffer, buffer.length, returnAddress, inPacket.getPort());
	            socket.send(outPacket);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static String parseCommand(String command) {
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
	}
	
	
	public static String SendMessage(String data) {
		try {
			DatagramSocket socket = new DatagramSocket();
	        while (true) {
	            InetAddress destAddress = InetAddress.getByName(Account.Ip);
	            byte[] buffer = data.getBytes();
	            DatagramPacket outPacket = new DatagramPacket(buffer, buffer.length, destAddress, PORT);
	            socket.send(outPacket);
	            
	            
	            buffer = new byte[256];
	            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
	            socket.receive(inPacket);
	            
	            socket.close();
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	private static String bufferToString(byte[] buffer) {
		short i=0;
		String result = "";
		while(buffer[i]!=0) {
			result += (char)buffer[i];
			i++;
		}
		return new String(result);
	}
	
}
