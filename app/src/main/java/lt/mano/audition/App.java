package lt.mano.audition;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

public class App extends Application {
    public GoogleApiClient mGoogleApiClient;
    public GoogleMap googleMap;
    private AlertDialog mAlertDialog;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void showLoadingDialog(Context context) {
       mAlertDialog = new AlertDialog.Builder(context)
                .setTitle("Loading")
                .setView(R.layout.loading_layout)
                .show();
    }

    public void hideLoadingDialog() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }
}
