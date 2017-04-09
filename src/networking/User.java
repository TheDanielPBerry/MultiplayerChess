package networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
	
	public String Username;
	public String PasswordHash;
	public Timestamp Created;

	public User(String username, String password, String datetime) {
		Username = username;
		PasswordHash = password;
		Created = Timestamp.valueOf(datetime);
	}
	public User(String username, String password, Timestamp datetime) {
		Username = username;
		PasswordHash = password;
		Created = datetime;
	}
	public User(String[] data) {
		Username = data[0];
		PasswordHash = data[1];
		Created = Timestamp.valueOf(data[2]);
	}
	
	
	public String toString() {
		return Username + "," + PasswordHash + "," + Created.toString() + "\n";
	}
	
	
	public static void AddUser(User user) {
		File file = new File("res\\User.csv");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
			bw.write(user.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static User GetUser(String username) {
		ArrayList<User> users = ReadUsers("res\\User.csv");
		for(User user : users) {
			if(user.Username.equals(username)) {
				return user;
			}
		}
		return null;
	}
	
	
    public static ArrayList<User> ReadUsers(String filePath) {
        String line = "";
        final String cvsSplitBy = ",";
        ArrayList<User> users = new ArrayList<User>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                users.add(new User(line.split(cvsSplitBy)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
    
}