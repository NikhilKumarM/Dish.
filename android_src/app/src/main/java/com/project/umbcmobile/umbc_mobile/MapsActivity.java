package com.project.umbcmobile.umbc_mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.Double.parseDouble;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, android.location.LocationListener {

    private GoogleMap mMap;
    String jsonString;
    Location myLocation;
    LocationManager locationManager;
    FloatingActionButton fab;
    Marker myMarker;
    int check=0;
    TextView username_,location_;
    Spinner quality_,price_,service_,rating_;
    String[] qualityfilter_ = {"Any","Good","Excellent"};
    String[] pricefilter_ = {"Any","high","low","affordable"};
    String[] ratingfilter_ = {"Any","1-5","6-10"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //all these are in the navigation drawer menu
        username_=(TextView)findViewById(R.id.fullName);
        //location_=(TextView)findViewById(R.id.location);
        SharedPreferences  sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPreferences.getString("name","");
        //username_.setText(name);
        Log.d("nik",name);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        quality_ = (Spinner)navigationView.getMenu().findItem(R.id.navbar_quality).getActionView();
        price_ = (Spinner)navigationView.getMenu().findItem(R.id.navbar_price).getActionView();
        service_ = (Spinner)navigationView.getMenu().findItem(R.id.navbar_service).getActionView();
        rating_ = (Spinner)navigationView.getMenu().findItem(R.id.navbar_rating).getActionView();

        /*to get username and location for navigation drawer
        Intent intent= getIntent();
        username_.setText(intent.getStringExtra("FullName"));
        location_.setText(intent.getStringExtra("Location"));*/

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.
                R.layout.simple_spinner_dropdown_item,qualityfilter_);
        quality_.setAdapter(adapter);
        quality_.setGravity(Gravity.RIGHT);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,android.
                R.layout.simple_spinner_dropdown_item,pricefilter_);
        price_.setAdapter(adapter1);
        price_.setGravity(Gravity.RIGHT);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,android.
                R.layout.simple_spinner_dropdown_item,ratingfilter_);
        service_.setAdapter(adapter2);
        service_.setGravity(Gravity.RIGHT);
        rating_.setAdapter(adapter2);
        rating_.setGravity(Gravity.RIGHT);

        //inserting map into the navigation
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //camera element
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent,0);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.login_header_logo);
        getSupportActionBar().setTitle("Find Your Dishes");
        /*
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        */
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,(android.location.LocationListener) this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                fab.setVisibility(View.VISIBLE);
            }

        });
        // Add a marker in Sydney and move the camera
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                pollingLocation();
                ha.postDelayed(this, 20000);
            }
        }, 20000);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                //for starting upload
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Intent uploadIntent = new Intent(MapsActivity.this, UploadPhotoActivity.class);
                    uploadIntent.putExtra("photo", photo);
                    startActivity(uploadIntent);
                }
                break;
            case 1234:
                super.onActivityResult(requestCode, resultCode, data);
                //for uploading from phone
                //when image is not selected try catch will solve it
                try {


                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap photo = (Bitmap) BitmapFactory.decodeFile(picturePath);
                    Intent uploadIntent = new Intent(MapsActivity.this, UploadPhotoActivity.class);
                    uploadIntent.putExtra("photo", photo);
                    startActivity(uploadIntent);
                }
                catch (Exception e)
                {

                }


                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, 0);
        }
//        } else if (id == R.id.nav_gallery) {
//            Intent i = new Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//            final int ACTIVITY_SELECT_IMAGE = 1234;
//            startActivityForResult(i, ACTIVITY_SELECT_IMAGE);}
//        } else if (id == R.id.nav_usercredentials) {
//
//        } else if (id == R.id.nav_favourite) {
//
//        }
        else if (id == R.id.nav_signout) {
            //Intent intent = new Intent(MapsActivity1.this, LoginActivity1.class);
            SharedPreferences  sharedPreferences_ = PreferenceManager.getDefaultSharedPreferences(this);
            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
            startActivity(intent);
            /*
            Gson gson = new Gson();
            String json = sharedPreferences_.getString("MyGoogleClient", "");
            GoogleApiClient obj = gson.fromJson(json, GoogleApiClient.class);

            Auth.GoogleSignInApi.signOut(obj).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });*/

        }
        else{

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        if(check==0) {
            pollingLocation();
            check++;
        }
    }

    public void  pollingLocation() {
        if (myLocation != null) {
            mMap.clear();
            synchronized (myLocation) {
                LatLng myloc = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                myMarker = mMap.addMarker(new MarkerOptions().position(myloc).title("Your are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myloc));
                CameraUpdate center = CameraUpdateFactory.newLatLng(myloc);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
                String input[] = new String[2];
                input[0] = String.valueOf(myLocation.getLatitude());
                input[1] = String.valueOf(myLocation.getLongitude());
                new InvokeWeService().execute(input);
            }
        }
    }
    public class InvokeWeService extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            String response = "";
            String requestUrl = "http://ec2-54-213-169-9.us-west-2.compute.amazonaws.com/getnearbyplaces.php";
            StringBuilder str = new StringBuilder();
            StringBuilder result = new StringBuilder();
            //str.append("test=" + "parameter&");
            str.append("?latitude=" + strings[0]).append("&longitude=" + strings[1]);
            String mystring = str.toString();
            requestUrl = requestUrl + mystring;
            try {
                url = new URL(requestUrl);
                HttpURLConnection myconnection = (HttpURLConnection) url.openConnection();
                myconnection.setReadTimeout(15000);
                myconnection.setConnectTimeout(15000);
                myconnection.setRequestMethod("GET");
                myconnection.setDoInput(true);
                myconnection.setDoOutput(true);
                /*
                OutputStream os = myconnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));


                writer.write(mystring);
                writer.flush();
                writer.close();
                */
                //String line;
                //int responseCode = myconnection.getResponseCode();
                if (200 == HttpURLConnection.HTTP_OK) ;
                {


                    InputStream in = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    //System.out.println(result.toString());

                    //  Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonString = s;
            try {
                populateOnMap(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void populateOnMap(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            String resId = jsonobject.getString("resID");
            String latitude = jsonobject.getString("latitude");
            String longitude = jsonobject.getString("longitude");
            String resname = jsonobject.getString("resname");
            LatLng loc = new LatLng(parseDouble(latitude), parseDouble(longitude));
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon_resource);
            Marker m = mMap.addMarker(new MarkerOptions().position(loc).title(resname).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            m.setTag(resId);
            myMarker.showInfoWindow();

        }

    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (!marker.getTitle().equals("Your are here!")) {
            String resname = marker.getTitle();
            String ID = String.valueOf(marker.getTag());
            Intent showImagesIntent = new Intent(MapsActivity.this, ShowPhotosActivity.class);
            showImagesIntent.putExtra("ID", ID);
            showImagesIntent.putExtra("resname",resname);
            startActivity(showImagesIntent);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        fab.setVisibility(View.INVISIBLE);
        return false;
    }


}
