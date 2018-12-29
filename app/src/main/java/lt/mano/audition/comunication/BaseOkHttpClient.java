package lt.mano.audition.comunication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public abstract class BaseOkHttpClient {
    protected static OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json");

    public BaseOkHttpClient(/*Context context*/) throws IOException {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(25, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS)
                    .readTimeout(40, TimeUnit.SECONDS)
                    .build();
        }
    }

    protected abstract String getEndpoint();

    protected Request buildPostRequest(String jsonObject) {
        RequestBody requestBody = RequestBody.create(JSON, jsonObject);
        return new Request.Builder()
                .url(getEndpoint())
                .post(requestBody)
                .build();
    }

    protected Request buildGetRequest() {
        return new Request.Builder()
                .url(getEndpoint())
                .get()
                .build();
    }
}


