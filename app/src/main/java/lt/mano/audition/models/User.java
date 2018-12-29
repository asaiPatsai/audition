package lt.mano.audition.models;

public class User {
    public int userId;
    public String familyName;
    public String name;
    public String email;
    public String locale;
    public boolean subscribe;

    public User(){}

    public User(int userId, String familyName, String name, String email, String locale,
            boolean subscribe) {
        this.userId = userId;
        this.familyName = familyName;
        this.name = name;
        this.email = email;
        this.locale = locale;
        this.subscribe = subscribe;
    }
}
