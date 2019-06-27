package Chatserver;

import java.io.Serializable;

/**
 *
 * @author Asus
 */
public class Login implements Serializable {

    private final String username, password;
    private boolean valid;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
        this.valid = false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public boolean getValid() {
        return valid;
    }
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }
}