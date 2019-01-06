package lt.mano.audition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import lt.mano.audition.tasks.LoginTask;

/**
 * Test login data
 * user:  deeperangler@gmail.com
 * password: Deeper10899
 */

public class MainActivity extends AppCompatActivity {
    private EditText mUserName;
    private EditText mPassword;
    private Button mLogInButton;
    private App mApp;
    private NetworkInfo mActiveNetwork;
    public View rootView;

    private BroadcastReceiver mNetworkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            mActiveNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
            if (null == mActiveNetwork) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Warning")
                        .setMessage("Turn on wifi or mobile data to use this app")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Close
                                        dialog.dismiss();
                                    }
                                }).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApp = (App) getApplication();

        mUserName = findViewById(R.id.et_user_name);
        mPassword = findViewById(R.id.et_password);
        mLogInButton = findViewById(R.id.btn_login);
        rootView = findViewById(R.id.parent);

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(mPassword);
                }
            }
        });

        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserName.getText().length() > 0 && mPassword.getText().length() > 0) {
                    if (mActiveNetwork != null) {
                        mApp.showLoadingDialog(MainActivity.this);
                        LoginTask task = new LoginTask(MainActivity.this,
                                mUserName.getText().toString(), mPassword.getText().toString());
                        task.execute();
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(rootView, "No connection", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(rootView, "No info submitted", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkChangeReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideLoading();
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideLoading() {
        mApp.hideLoadingDialog();
    }
}
