package com.example.gurvindersingh.worker_part;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class Register_Device extends AppCompatActivity {
    ImageView BackImage;
    Spinner CountryNames;
    LinearLayout PhoneLayout, CodeLayout, BackNull;
    ProgressBar progress_login;
    ImageView BackButton;
   String FieldCOde="1";
    Boolean BackButtonCommand=true;
    String PhoneNumber;
    int finishCode = 0;
    TextView ConfirmText;
    TextView CountryCodeText, ConfirmCode, ConfirmNumber, LoginType;
    EditText Phoneno, Code1, Code2, Code3, Code4;
    ArrayList<String> CountryName = new ArrayList<String>();
    ArrayList<String> CountryCode = new ArrayList<String>();
    String[] g;
    String[] rl;
   // String Field = "";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String shareprefrence_name = "Login_Details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intialize();
    }
    public void Intialize() {
        Intent bundleData = getIntent();
      //  Field = bundleData.getStringExtra("task");
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
        CountryNames = (Spinner) findViewById(R.id.spinnercountry);
        sharedpreferences = getSharedPreferences(shareprefrence_name, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
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
        LoginType.setText(getString(R.string.workerlogin));
      /*  if (Field.equals("worker")) {
            LoginType.setText(getString(R.string.workerlogin));

        } else {
            LoginType.setText(getString(R.string.customerlogin));
        }*/

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

                        LoginType.setText(getString(R.string.workerlogin));
                        FieldCOde="1";

                    LoginType.startAnimation(animation);
                }
            }
        });
        ConfirmNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkConnected()) {
                    if (Phoneno.getText().toString().length() > 0) {
                        PhoneNumber=Phoneno.getText().toString();
                        SendPhoneNumberServer();
                    }else {
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
                    if(Code1.getText().toString().length()>0&&Code2.getText().toString().length()>0&&Code3.getText().toString().length()>0&&Code4.getText().toString().length()>0)  {
                        nextPage();
                    }
                    else {
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
        BackButtonCommand=false;
        progress_login.setVisibility(View.VISIBLE);
        BackNull.setClickable(true);

        Call<ResponseBody> call = ServiceGenerator.requestApi().response(URLs.AddLogin+CountryCode.get(CountryNames.getSelectedItemPosition())+Phoneno.getText().toString()+"&type="+FieldCOde);
        Log.e("URl", "" + call.request().url());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String AllresponseData=response.body().string();
                    BackButtonCommand=true;
                    BackNull.setClickable(false);
                    progress_login.setVisibility(View.GONE);
                    JSONObject MainJSON=new JSONObject(AllresponseData);
                    String Message=MainJSON.getString("message");
                    String Res=MainJSON.getString("response");
                    if(Res.equals("1")){
                        JSONObject UserData=MainJSON.getJSONObject("User Data");
                        editor.putString("userid", UserData.getString("id"));
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
        BackButtonCommand=false;
        Log.e("nextPage: ",sharedpreferences.getString("userid", "")+"&otp="+Code1.getText().toString()+Code2.getText().toString()+Code3.getText().toString()+Code4.getText().toString() );
        Call<ResponseBody> call = ServiceGenerator.requestApi().response(URLs.VerifiedContact+sharedpreferences.getString("userid", "")+"&otp="+Code1.getText().toString()+Code2.getText().toString()+Code3.getText().toString()+Code4.getText().toString());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    BackButtonCommand=true;
                    BackNull.setClickable(false);
                    progress_login.setVisibility(View.GONE);
                    String AllresponseData=response.body().string();
                    JSONObject MainJSON=new JSONObject(AllresponseData);
                    String Message=MainJSON.getString("message");
                    if(Message.equals("Verified")){

                            Intent i = new Intent(Register_Device.this, Enter_Worker_Detail.class);
                            startActivity(i);

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
        if(BackButtonCommand){
            if (finishCode == 0) {
                finish();
            } else {
                finishCode=0;
                PhoneLayout.setVisibility(View.VISIBLE);
                CodeLayout.setVisibility(View.GONE);
                Phoneno.setText("");
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                PhoneLayout.startAnimation(animation);
                    LoginType.setText(getString(R.string.workerlogin));
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
            return true;
        } else {
            // display error
            return false;
        }
    }
}
