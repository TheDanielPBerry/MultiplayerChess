package networking;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class PasswordHash {
	
	public static void main(String[] args) {
		System.out.println(generateSalt());
	}
	
	public static String hash(String password) {
		String salt = generateSalt();
		String hashedPassword = generateHash(salt + password);
		return hashedPassword + ":" + salt;
	}
	
	
	
	public static String generateSalt() {
		final int saltLength = 50;
		Random r = new Random();
		String salt = "";
		for(short i=0; i<saltLength; i++) {
			byte id = (byte)Math.abs(r.nextInt()%36);
			id+=48;
			if(id>57) {
				id+=7;
			}
			salt += (char)id;
		}
		return salt;
	}
	
	public static String generateHash(String input) {
		StringBuilder hash = new StringBuilder();
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-512");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			// handle error here.
		}
		
		return hash.toString();
	}
	
}