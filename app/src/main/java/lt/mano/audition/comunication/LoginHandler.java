package lt.mano.audition.comunication;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import lt.mano.audition.models.Data;
import okhttp3.Request;
import okhttp3.Response;

public class LoginHandler extends BaseOkHttpClient {
    private String mName;
    private String mPassword;

    public LoginHandler(String name, String password) throws IOException {
        super();
        mName = name;
        mPassword = password;
    }

    public Data login() throws IOException {
        Data loginResponse;
        Gson gson = new Gson();
        Request request = buildGetRequest();
        String json = null;
        try {
            Response response = client.newCall(request).execute();
            String status = response.message() + "(" + response.code() + ")";
            Log.d("Test", "Status - "+status);
            json = response.body().string();
            if (!response.isSuccessful()) {
                Log.d("Test", "Response not successful");
                throw new IOException("Logging in failed with status: " + status);
            } else {
                Log.d("Test", "Response successful");
                loginResponse = gson.fromJson(json, Data.class);
            }
        } catch (IOException e) {
            throw new IOException("Login request failed", e);
        } catch (JsonSyntaxException e) {
            throw new IOException("Failed to parse JSON: " + json, e);
        }
        return loginResponse;
    }

    @Override
    protected String getEndpoint() {
        String host = "https://maps.deepersonar.com/api/login?username=%1$s&password=%2$s";
        return String.format(host, mName, mPassword);
    }
}
