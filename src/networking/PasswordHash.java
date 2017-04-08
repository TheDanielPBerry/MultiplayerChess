package networking;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class PasswordHash {
	
	public static String hash(String password) {
		String salt = generateSalt();
		String hashedPassword = generateHash(salt + password);
		return hashedPassword + ":" + salt;
	}
	
	
	
	public static String generateSalt() {
		final int saltLength = 100;
		Random r = new Random();
		String salt = "";
		for(byte i=0; i<saltLength; i++) {
			salt += (char)r.nextInt();
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