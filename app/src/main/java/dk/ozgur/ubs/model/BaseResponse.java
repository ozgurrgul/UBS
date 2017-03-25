package dk.ozgur.ubs.model;

public class BaseResponse {

    private String message;
    private String status;

    public String getMessage() {
        return this.message;
    }

    public boolean hasError() {
        return !this.status.equals("200");
    }
}