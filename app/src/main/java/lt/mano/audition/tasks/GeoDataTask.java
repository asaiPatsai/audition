package lt.mano.audition.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;

import lt.mano.audition.MapActivity;
import lt.mano.audition.comunication.GeoMapHandler;
import lt.mano.audition.models.GeoData;

public class GeoDataTask extends AsyncTask<String, Void, GeoData> {
    private int mScanId;
    private String mToken;
    private WeakReference<Context> mContextRef;

    public GeoDataTask(Context context, int scanId, String token) {
        mContextRef = new WeakReference<>(context);
        mScanId = scanId;
        mToken = token;
    }

    @Override
    protected GeoData doInBackground(String... params) {
        GeoData result = null;
        try {
            GeoMapHandler handler = new GeoMapHandler(mScanId, mToken);
            result = handler.getData();
        } catch (IOException e) {
            Log.e("Test", "Error receiving data", e);
            Snackbar snackbar = Snackbar
                    .make(((MapActivity) mContextRef.get()).rootView,
                            "Something went wrong " + e.getMessage(), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        return result;
    }

    @Override
    protected void onPostExecute(GeoData result) {
        if (result != null) {
            ((MapActivity)mContextRef.get()).plotAreaOnMap(result.features);
        }
    }
}
