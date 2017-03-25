package dk.ozgur.ubs.model;

/**
 * Created by ozgur on 3/24/17.
 */

public class UserLoginRequest {

    public String username;
    public String password;

    public UserLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
