package lt.mano.audition.models;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public List<ScanDataItem> scans = new ArrayList<>();
    public User user;
    public Login login;

    public Data(List<ScanDataItem> scans, User user, Login login) {
        this.login = login;
        this.user = user;
        this.scans = scans;
    }
}
