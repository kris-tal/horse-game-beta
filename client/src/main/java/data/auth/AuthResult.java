package data.auth;
public class AuthResult {
    public AuthCode returnedCode;
    public String token;
    public AuthResult(AuthCode code, String token) {
        this.token = token;
        this.returnedCode = code;
    }
}
