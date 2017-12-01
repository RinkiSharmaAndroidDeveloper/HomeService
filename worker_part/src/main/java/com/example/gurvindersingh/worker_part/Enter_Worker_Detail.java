package com.example.gurvindersingh.worker_part;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gurvindersingh.worker_part.Networking.ServiceGenerator;
import com.example.gurvindersingh.worker_part.Networking.URLs;
import com.example.gurvindersingh.worker_part.Pojo_Clases.Services_Data;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.R.attr.data;
import static android.R.attr.name;

public class Enter_Worker_Detail extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,AdapterView.OnItemSelectedListener {

    ImageView BackGroundImage;
    ApplicationClass app_obj;
    Spinner Working;
    String item;
    LinearLayout AddImage;
    File fileImage;
    private static int RESULT_LOAD_IMG = 1;
    String [] NameP;
    File imgDecodableString;
    protected String latitude, longitude;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationManager locationManager;
    Location location;
    ProgressDialog dailog;
    EditText firstname,Lastname,number,city,qualification;
    TextView submt_btn;
    SharedPreferences sharedpreferences1;
    String shareprefrence_name = "Login_Details";
     String User_Id,User_Lat,User_Long,first_name,lastname,qualifictn,ph_num,cty;
    String first_name1,lastname1,qualifictn1,ph_num1,cty1;
    ArrayList<String> NameProfession=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_enter__worker__detail);
        getSupportActionBar().hide();
        dailog = new ProgressDialog(this);
        dailog.setIndeterminate(true);
        dailog.setCancelable(false);
        dailog.setMessage("Loading Please wait....");
        sharedpreferences1 = getSharedPreferences(shareprefrence_name, Context.MODE_PRIVATE);
        User_Lat=sharedpreferences1.getString("latKey", null);
        User_Long=sharedpreferences1.getString("lngKey", null);
        Intent i = getIntent();
        User_Id =i.getStringExtra("userID");
        buildGoogleApiClient();
        Intialize();
    }
    public void Intialize(){
        app_obj=(ApplicationClass)getApplicationContext();
        AddImage =(LinearLayout)findViewById(R.id.addImage);
        Working=(Spinner)findViewById(R.id.professionspinner);
        firstname=(EditText)findViewById(R.id.editTextName);
       Lastname=(EditText)findViewById(R.id.lastname);
        number=(EditText)findViewById(R.id.phone);
        city=(EditText)findViewById(R.id.city);
        qualification=(EditText)findViewById(R.id.qualification);
        submt_btn=(TextView)findViewById(R.id.submit);
        //  Glide.with(getApplicationContext()).load(R.drawable.bublebg).into(BackGroundImage);
        nextPage();
        Working.setOnItemSelectedListener(this);
        submt_btn.setOnClickListener(this);
        AddImage.setOnClickListener(this);
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    public void SendPhoneNumberServer(String first_name1,String lastname1,String qualifictn1,String ph_num1,String cty1,String item1) {
        dailog.show();
        Call<ResponseBody> call;
        /*String key = "" + i + "\"; filename=\"image" + i + ".jpg\" ";
        RequestBody file = RequestBody.create(MediaType.parse("multipart/form-data"), mFileArray.get(i));
        fileMap.put(key, file);
        Map<> fileMap = new Map<>();*/
        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
        call = ServiceGenerator.requestApi().requestAddItem(URLs.ADD_Info+User_Id+URLs.W_Name+first_name1+"%20"+lastname1+URLs.W_Phone+ph_num1+URLs.W_Profession+item1+URLs.W_Qualification+qualifictn1+URLs.W_City+cty1+URLs.W_Lng+User_Long+URLs.W_Lat+User_Lat, (Map<String, RequestBody>) image);
        Log.e("URl", "" + call.request().url());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {


                    String AllresponseData = response.body().string();
                    // BackButtonCommand=true;
                    // BackNull.setClickable(false);

                    JSONObject MainJSON = new JSONObject(AllresponseData);
                    String Message = MainJSON.getString("message");
                    String res = MainJSON.optString("response");
                    // JSONArray permArray = MainJSON.getJSONArray("data");
                    if (res.equals("1")) {
                        dailog.dismiss();
                        Toast.makeText(getApplicationContext(),Message,Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "In exp", Toast.LENGTH_LONG).show();
                    Log.e("Exp", "" + e.getMessage().toString());
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something", Toast.LENGTH_LONG).show();
            }

        });


    }
    public void nextPage() {
//http://webmantechnologies.com/home_service/index.php/webservice/add_info?id=3&name=Ram%20Bijalwan&phone=919887897877&profession=1,3,4&qualification=B.tech&city=Uttarkashi&longitude=76.7179&latitude=30.7046
        Call<ResponseBody> call = ServiceGenerator.requestApi().response(URLs.Service_List);
        //Call<ResponseBody> call = ServiceGenerator.requestApi().response(URLs.ADD_Info+User_Id+URLs.W_Name+URLs.W_Phone+URLs.W_Profession+URLs.W_Qualification+URLs.W_City+URLs.W_Lng+User_Long+URLs.W_Lat+User_Lat);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    app_obj.ServiceDataList.clear();
                    app_obj.ColorCode.clear();
                    String AllresponseData=response.body().string();
                    Log.e("onResponse: ",AllresponseData );
                    JSONObject MainJSON=new JSONObject(AllresponseData);
                    String MessageData=MainJSON.getString("message");
                    if(MessageData.equals("Services list is here")){
                        JSONArray AllArray=MainJSON.getJSONArray("data");
                        for (int i=0;i<AllArray.length();i++){
                            JSONObject DataInArray=AllArray.getJSONObject(i);
                            String Name=DataInArray.getString("service_name");
                            String ID=DataInArray.getString("id");
                            String Image=DataInArray.getString("image");
                            Services_Data services_data=new Services_Data(ID,Name,Image);
                            app_obj.ServiceDataList.add(services_data);
                        }
                        NameP=new String[app_obj.ServiceDataList.size()];
                        for(int z=0;z<app_obj.ServiceDataList.size();z++){
                            NameProfession.add(app_obj.ServiceDataList.get(z).getName());
                        NameP[z]=app_obj.ServiceDataList.get(z).getName();
                        }
                        if(app_obj.ServiceDataList.size()>0){
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Enter_Worker_Detail.this, android.R.layout.simple_spinner_item,NameP);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Working.setAdapter(adapter);

                        }

                    }

                } catch (Exception e) {
                    Log.e("onResponse: ",e.toString() );
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.networknotresponding), Toast.LENGTH_LONG).show();
            }
        });


    }
    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 25);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 25) {

                String realPath = "";
                try {
                    Uri chosenImageUri = data.getData();
                    realPath = ServiceGenerator.getRealPathFromURI(chosenImageUri, Enter_Worker_Detail.this);
                    assert realPath != null : "real path is null";

                    fileImage = new File(realPath);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("ImagePath", realPath);
            }

        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case(R.id.addImage):
                loadImagefromGallery();
               break;
            case (R.id.submit):
                first_name=firstname.getText().toString();
                lastname=Lastname.getText().toString();
                qualifictn=qualification.getText().toString();
                ph_num=number.getText().toString();
                cty=city.getText().toString();
                if(first_name.isEmpty()){
                    firstname.setError("Please enter first name");
                }else if(lastname.isEmpty()){
                    Lastname.setError("Please enter las tname");
                }
                else if(qualifictn.isEmpty()){
                    qualification.setError("Please enter qualification");
                }
                else if(ph_num.isEmpty()){
                    number.setError("Please enter Number");
                }
                else if(cty.isEmpty()){
                    city.setError("Please enter City");
                }
                else if(item.isEmpty()){
                   Toast.makeText(getApplicationContext(),"Please select profession",Toast.LENGTH_SHORT).show();
                }else {
                    dailog.show();
                    first_name1 = first_name.replace(" ", "");
                    lastname1= lastname.replace(" ", "");
                    qualifictn1= qualifictn.replace(" ", "");
                    ph_num1= ph_num.replace(" ", "");
                    cty1= cty.replace(" ", "");
                    String item1= item.replace(" ", "");
                    SendPhoneNumberServer(first_name1,lastname1,qualifictn1,ph_num1,cty1,item1);
                }

                break;
        }
    }
   /* public  void myImage() {
        Call<ResponseBody> call;
        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
        call = ServiceGenerator.requestApi().requestAddItem(, image);
        call.enqueue(new Callback<LoggedInUserPojo>() {
            @Override
            public void onResponse(Call<LoggedInUserPojo> call, Response<LoggedInUserPojo> response) {

                if (response.isSuccess()) {
                    LoggedInUserPojo result = response.body();
                    if (result.getData() != null) {
                        afterProfileCompletion(result);
                    } else {
                        try {
                            Utils.showToast(mContext, result.getMessage());
                        } catch (Exception e) {
                            Utils.showToast(mContext, getString(R.string.something_went_wrong));
                        }
                    }
                } else {
                    Utils.showToast(mContext, getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<LoggedInUserPojo> call, Throwable t) {

        });
    }*/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {

            latitude= String.valueOf(location.getLatitude());

            longitude= String.valueOf(location.getLongitude());
        } else {
            Toast.makeText(this,"No location detected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
