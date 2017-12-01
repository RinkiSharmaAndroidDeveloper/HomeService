package com.example.gurvindersingh.worker_part;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gurvindersingh.worker_part.Networking.ServiceGenerator;
import com.example.gurvindersingh.worker_part.Networking.URLs;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    ImageView BackImage;

    Spinner CountryNames;
    LinearLayout PhoneLayout, CodeLayout, BackNull;
    ProgressBar progress_login;
    ImageView BackButton;
    String FieldCOde = "0";
    Boolean BackButtonCommand = true;
    String PhoneNumber;
    int finishCode = 0;
    TextView ConfirmText;
    TextView CountryCodeText, ConfirmCode, ConfirmNumber, LoginType;
    EditText Phoneno, Code1, Code2, Code3, Code4;
    ArrayList<String> CountryName = new ArrayList<String>();
    ArrayList<String> CountryCode = new ArrayList<String>();
    String[] g;
    String[] rl;
    String PAss_user_id;
    String Field = "";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String shareprefrence_name = "Login_Details";
    protected LocationManager locationManager;
    Location location;
    protected LocationListener locationListener;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    public static final String Lat = "latKey";
    public static final String Lng = "lngKey";
    SharedPreferences sharedpreferences1;
    private int PERMISSION_REQUEST_CODE = 23;
    private GoogleApiClient mGoogleApiClient;
    private int LOCATION_SETTINGS_REQUEST_CODE = 0x1;
    public Location mLastLocation;
    LocationRequest mLocationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        askLocationPermission();

    }

    private void askLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            }
        }else{
            checkLocationSettings();
        }
    }
    public void Intialize() {
        Intent bundleData = getIntent();
        Field = bundleData.getStringExtra("task");
        CodeLayout = (LinearLayout) findViewById(R.id.codelayout);
        PhoneLayout = (LinearLayout) findViewById(R.id.phonelayout);
        LoginType = (TextView) findViewById(R.id.logintype);
        ConfirmCode = (TextView) findViewById(R.id.ConfirmCode);
        ConfirmNumber = (TextView) findViewById(R.id.ConfirmNumber);
        Phoneno = (EditText) findViewById(R.id.phoneno);
        Code1 = (EditText) findViewById(R.id.code1);
        Code2 = (EditText) findViewById(R.id.code2);
        Code3 = (EditText) findViewById(R.id.code3);
        Code4 = (EditText) findViewById(R.id.code4);
        ConfirmText = (TextView) findViewById(R.id.numbertext);
        BackButton = (ImageView) findViewById(R.id.BackClose);
        BackNull = (LinearLayout) findViewById(R.id.backNull);
        progress_login = (ProgressBar) findViewById(R.id.registerprogress);
        CountryCodeText = (TextView) findViewById(R.id.countrycode);
        BackImage = (ImageView) findViewById(R.id.bgoflogin);
        GetCountryZipCode();
        buildGoogleApiClient();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        CountryNames = (Spinner) findViewById(R.id.spinnercountry);
        sharedpreferences = getSharedPreferences(shareprefrence_name, Context.MODE_PRIVATE);
        sharedpreferences1 = getSharedPreferences(shareprefrence_name, Context.MODE_PRIVATE);
        editor = sharedpreferences1.edit();
        editor.putString(Lat, latitude);
        editor.putString(Lng, longitude);
        editor.commit();
        Glide.with(getApplicationContext()).load(R.drawable.bublebg).into(BackImage);
        CountryCodeText.setText("+" + CountryCode.get(0));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, CountryName);
        CountryNames.setAdapter(adapter);
        CountryNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int z = CountryNames.getSelectedItemPosition();
                CountryCodeText.setText("+" + CountryCode.get(z));

                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        Code1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 1) {
                    Code2.requestFocus();
                }
            }
        });
        Code2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 1) {
                    Code3.requestFocus();
                }
            }
        });
        Code3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 1) {
                    Code4.requestFocus();
                }
            }
        });
        Code4.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 1) {
                    //Code2.requestFocus();
                }
            }
        });
        PhoneLayout.setVisibility(View.VISIBLE);
        CodeLayout.setVisibility(View.GONE);
        if (Field.equals("worker")) {
            LoginType.setText(getString(R.string.workerlogin));

        } else {
            LoginType.setText(getString(R.string.customerlogin));
        }

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finishCode == 0) {
                    finish();
                } else {
                    finishCode = 0;
                    PhoneLayout.setVisibility(View.VISIBLE);
                    CodeLayout.setVisibility(View.GONE);
                    Phoneno.setText("");
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                    PhoneLayout.startAnimation(animation);
                    if (Field.equals("worker")) {
                        LoginType.setText(getString(R.string.workerlogin));
                        FieldCOde = "1";
                    } else {
                        LoginType.setText(getString(R.string.customerlogin));
                        FieldCOde = "0";
                    }
                    LoginType.startAnimation(animation);
                }
            }
        });
        ConfirmNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkConnected()) {
                    if (Phoneno.getText().toString().length() > 0) {
                        PhoneNumber = Phoneno.getText().toString();
                        SendPhoneNumberServer();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.entervalidContact), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.networknotresponding), Toast.LENGTH_LONG).show();
                }
            }
        });
        ConfirmCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkConnected()) {
                    if (Code1.getText().toString().length() > 0 && Code2.getText().toString().length() > 0 && Code3.getText().toString().length() > 0 && Code4.getText().toString().length() > 0) {
                        nextPage();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.fillallfield), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.networknotresponding), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void GetCountryZipCode() {
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            g = rl[i].split(",");
            CountryCode.add(g[0]);
            CountryName.add(g[1]);

        }
    /*    for(int i=0;i<rl.length;i++){
            CountryName=rl[i].split(",");


           if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
               Log.e( "GetCountryZipCode: ",CountryZipCode );
                break;
            }
        }*/
        //      return CountryZipCode;
    }

    public void SendPhoneNumberServer() {
        BackButtonCommand = false;
        progress_login.setVisibility(View.VISIBLE);
        BackNull.setClickable(true);

        Call<ResponseBody> call = ServiceGenerator.requestApi().response(URLs.AddLogin + CountryCode.get(CountryNames.getSelectedItemPosition()) + Phoneno.getText().toString() + "&type=" + FieldCOde);
        Log.e("URl", "" + call.request().url());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String AllresponseData = response.body().string();
                    BackButtonCommand = true;
                    BackNull.setClickable(false);
                    progress_login.setVisibility(View.GONE);
                    JSONObject MainJSON = new JSONObject(AllresponseData);
                    String Message = MainJSON.getString("message");
                    String Res = MainJSON.getString("response");
                    if (Res.equals("1")) {
                        JSONObject UserData = MainJSON.getJSONObject("User Data");
                        editor.putString("userid", UserData.getString("id"));
                        PAss_user_id = UserData.getString("id");
                        editor.apply();
                        finishCode = 1;
                        PhoneLayout.setVisibility(View.GONE);
                        CodeLayout.setVisibility(View.VISIBLE);
                        ConfirmText.setText(getString(R.string.verifiecodetext) + " " + Phoneno.getText().toString());
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                        CodeLayout.startAnimation(animation);
                        Code1.requestFocus();
                        LoginType.setText(getString(R.string.verifynumber));
                        LoginType.startAnimation(animation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress_login.setVisibility(View.GONE);
                BackNull.setClickable(false);
                Toast.makeText(getApplicationContext(), getString(R.string.networknotresponding), Toast.LENGTH_LONG).show();
            }
        });


    }

    public void nextPage() {
        progress_login.setVisibility(View.VISIBLE);
        BackNull.setClickable(true);
        BackButtonCommand = false;
        Log.e("nextPage: ", sharedpreferences.getString("userid", "") + "&otp=" + Code1.getText().toString() + Code2.getText().toString() + Code3.getText().toString() + Code4.getText().toString());
        Call<ResponseBody> call = ServiceGenerator.requestApi().response(URLs.VerifiedContact + sharedpreferences.getString("userid", "") + "&otp=" + Code1.getText().toString() + Code2.getText().toString() + Code3.getText().toString() + Code4.getText().toString());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    BackButtonCommand = true;
                    BackNull.setClickable(false);
                    progress_login.setVisibility(View.GONE);
                    String AllresponseData = response.body().string();
                    JSONObject MainJSON = new JSONObject(AllresponseData);
                    String Message = MainJSON.getString("message");
                    if (Message.equals("Verified")) {
                        if (Field.equals("worker")) {
                            Intent i = new Intent(LoginPage.this, Enter_Worker_Detail.class);
                            i.putExtra("userID", PAss_user_id);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(LoginPage.this, ServiceRequestActivity.class);
                            startActivity(i);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress_login.setVisibility(View.GONE);
                BackNull.setClickable(false);
                Toast.makeText(getApplicationContext(), getString(R.string.networknotresponding), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (BackButtonCommand) {
            if (finishCode == 0) {
                finish();
            } else {
                finishCode = 0;
                PhoneLayout.setVisibility(View.VISIBLE);
                CodeLayout.setVisibility(View.GONE);
                Phoneno.setText("");
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                PhoneLayout.startAnimation(animation);
                if (Field.equals("worker")) {
                    LoginType.setText(getString(R.string.workerlogin));
                } else {
                    LoginType.setText(getString(R.string.customerlogin));
                }
                LoginType.startAnimation(animation);

            }
        }
    }

    private boolean isNetworkConnected() {


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
           /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                }*/
            return true;
        } else {
            // display error
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationSettings();
            }
            checkLocationSettings();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        /*latitude= String.valueOf(location.getLatitude());
        longitude= String.valueOf(location.getLongitude());*/
        getLocation(location);
        // Toast.makeText(getApplicationContext(),latitude+","+longitude, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void basicVariablesInit() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void checkLocationSettings() {
        basicVariablesInit();
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        final PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }

    private void stopLocationUpdates() {

        if (mGoogleApiClient.isConnected()) {

            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient,
                    this
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                }
            });
            mGoogleApiClient.disconnect();
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @SuppressWarnings("MissingPermission")
    protected void startLocationUpdates() {

        if (mGoogleApiClient.isConnected()) {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {

                }
            });

        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLocation(Location location) {
        if (location != null) {
            stopLocationUpdates();
           latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());


            mLastLocation = location;
        }
        Intialize();
    }



    //2&contact_no=9878675445
}
