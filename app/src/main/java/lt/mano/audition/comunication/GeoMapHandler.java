package lt.mano.audition.comunication;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import lt.mano.audition.models.GeoData;
import okhttp3.Request;
import okhttp3.Response;

public class GeoMapHandler extends BaseOkHttpClient {
    private int mScanId;
    private String mToken;

    public GeoMapHandler(int scanId, String token) throws IOException {
        super();
        mScanId = scanId;
        mToken = token;
    }

    public GeoData getData() throws IOException {
        GeoData dataResponse;
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
                dataResponse = gson.fromJson(json, GeoData.class);
            }
        } catch (IOException e) {
            throw new IOException("Data request failed", e);
        } catch (JsonSyntaxException e) {
            throw new IOException("Failed to parse JSON: " + json, e);
        }
        return dataResponse;
    }

    @Override
    protected String getEndpoint() {
        String host = "https://maps.deepersonar.com/api/polygon?token=%1$s&scanIds=%2$s";
        return String.format(host, mToken, mScanId);
    }
}
