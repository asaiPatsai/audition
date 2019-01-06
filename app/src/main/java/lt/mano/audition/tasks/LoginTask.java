package lt.mano.audition.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;

import lt.mano.audition.MainActivity;
import lt.mano.audition.MapActivity;
import lt.mano.audition.comunication.LoginHandler;
import lt.mano.audition.models.Data;

public class LoginTask extends AsyncTask<String, Void, Data> {
    private String mName;
    private String mPassword;
    private WeakReference<Context> mContextRef;

    public LoginTask(Context context, String name, String password) {
        mContextRef = new WeakReference<>(context);
        mName = name;
        mPassword = password;
    }

    @Override
    protected Data doInBackground(String... params) {
        Data result = null;
        try {
            LoginHandler handler = new LoginHandler(mName, mPassword);
            result = handler.login();
        } catch (IOException e) {
            Log.e("Test", "Error receiving login data", e);
            Snackbar snackbar = Snackbar
                    .make(((MainActivity) mContextRef.get()).rootView,
                            "Something went wrong " + e.getMessage(), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Data result) {
        if (result != null) {
            showDataActivity(result);
        } else {
            ((MainActivity)mContextRef.get()).hideLoading();
        }
    }

    private void showDataActivity(Data result) {
        Intent i = new Intent(mContextRef.get(), MapActivity.class);
        i.putExtra("Data", new Gson().toJson(result));
        mContextRef.get().startActivity(i);
    }
}
