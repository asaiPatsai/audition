package lt.mano.audition;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;

import java.util.List;

import lt.mano.audition.adapters.ScanListAdapter;
import lt.mano.audition.models.Coordinates;
import lt.mano.audition.models.Data;
import lt.mano.audition.models.GeoData;
import lt.mano.audition.tasks.GeoDataTask;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private App mApp;
    private RecyclerView mRecyclerView;
    private ScanListAdapter mAdapter;
    private int mSelectedPosition = 0;
    private ImageView mListIcon;
    private TextView mCurrentTitle;
    private Data mData;
    public View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mApp = (App) getApplication();

        if (mApp.mGoogleApiClient == null) {
            mApp.mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        String jsonObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonObject = extras.getString("Data");
        }
        mData = new Gson().fromJson(jsonObject, Data.class);

        rootView = findViewById(R.id.parent);
        mListIcon = findViewById(R.id.list_icon);
        mCurrentTitle = findViewById(R.id.selected_name);

        if (mData.scans != null && !mData.scans.isEmpty()) {
            mCurrentTitle.setText(mData.scans.get(mSelectedPosition).name);
            mListIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRecyclerView.getVisibility() == View.VISIBLE) {
                        mRecyclerView.setVisibility(View.GONE);
                        mListIcon.setColorFilter(
                                ContextCompat.getColor(MapActivity.this, R.color.black));
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mListIcon.setColorFilter(
                                ContextCompat.getColor(MapActivity.this, android.R.color.white));
                        mRecyclerView.bringToFront();
                    }
                }
            });

            prepareList();
        } else {
            Snackbar snackbar = Snackbar
                    .make(rootView, "No data received", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MapActivity.this, MainActivity.class));
                            finish();
                        }
                    });
            snackbar.show();
        }

        acquireMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mApp.googleMap = googleMap;
        mApp.hideLoadingDialog();
    }

    @Override
    public void onConnected(Bundle connectionHint) {}

    @Override
    public void onConnectionSuspended(int nr) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (result.getErrorCode() == ConnectionResult.NETWORK_ERROR) {
            new AlertDialog.Builder(MapActivity.this)
                    .setTitle("Warning")
                    .setMessage("Turn on wifi or mobile data to connect to google api")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Close
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    public void handleSelection(int position) {
        mSelectedPosition = position;
        updateUI();
        getGeoData();
    }

    public void plotAreaOnMap(List<GeoData.Feature> result) {
        updateUI();
        mApp.hideLoadingDialog();
        PolygonOptions polyOptions;
        Coordinates coordinates;
        LatLng camera = null;
        for (GeoData.Feature feature : result) {
            polyOptions = new PolygonOptions();
            for (List<Double> list : feature.geometry.coordinates.get(0)) {
                coordinates = new Coordinates(list);
                polyOptions.add(new LatLng(coordinates.latitude, coordinates.longitude));
                camera = new LatLng(coordinates.latitude, coordinates.longitude);
            }

            polyOptions = setPolyColor(polyOptions, feature.properties.depth);
            mApp.googleMap.addPolygon(polyOptions);

            mApp.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(camera, 16f));
        }
    }

    private void acquireMap() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (!mApp.mGoogleApiClient.isConnected()) {
            mApp.mGoogleApiClient.connect();
        }
    }

    private void getGeoData() {
        mApp.showLoadingDialog(this);
        GeoDataTask task = new GeoDataTask(this, mData.scans.get(mSelectedPosition).id,
                mData.login.token);
        task.execute();
    }

    private void prepareList() {
        mRecyclerView = findViewById(R.id.recycler_view);

        mAdapter = new ScanListAdapter(mData.scans, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.bringToFront();

        getGeoData();
    }

    private PolygonOptions setPolyColor(PolygonOptions polygonOptions, double depth) {
        if (depth >= 13) {
            polygonOptions.fillColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        } else if (depth >= 10) {
            polygonOptions.fillColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
        } else {
            polygonOptions.fillColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        }
        return polygonOptions;
    }

    private void updateUI() {
        if (!mData.scans.get(mSelectedPosition).name.isEmpty()) {
            mCurrentTitle.setText(mData.scans.get(mSelectedPosition).name);
        } else {
            mCurrentTitle.setText("Unknown name");
        }
        mRecyclerView.setVisibility(View.GONE);
    }
}
