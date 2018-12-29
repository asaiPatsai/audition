package lt.mano.audition.models;

public class Login {
    public String appId;
    public String token;
    public int userId;
    public boolean validated;
    public String validTill;

    public Login() {}

    public Login(String appId, String token, int userId, boolean validated, String validTill) {
        this.appId = appId;
        this.token = token;
        this.userId = userId;
        this.validated = validated;
        this.validTill = validTill;
    }
}
