import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores the messages saved by the user to display in the browser.
 * Also serves as storage for passwords for authentication.
 */
public class User {
    ArrayList<Message> messages = new ArrayList<>();
    Integer index = 1;

    private HashMap<String, String> passwords = new HashMap<>();

    public String getName() {
        return name;
    }

    private String name;
    private String password;

    public User() {}

    public User(String name, String password){
        this.name = name;
        this.password = password;

        passwords.put(name, password);
    }

    public boolean checkPassword(String name, String password) {
        return password.equals(passwords.get(name));
    }
}
