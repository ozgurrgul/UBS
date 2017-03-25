package dk.ozgur.ubs.model;

public class UserDataRequest {

    public String username;
    public String password;
    public int UsersGroupId;

    public UserDataRequest(String username, String password, int UsersGroupId) {
        this.username = username;
        this.password = password;
        this.UsersGroupId = UsersGroupId;
    }
}