package com.project.umbcmobile.umbc_mobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

public class UploadPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    EditText foodname_, location_, review_;
    Button buttonpost_, buttonBack;
    ImageView imageView_;
    private Bitmap photo;
    String[] rating = {"-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] price = {"-", "Cheap", "Average", "High"};
    String[] food_quality = {"-", "Terrible", "Bad", "Good", "Excellent"};
    Spinner act_service, act_rating, act_quality, act_price;
    String path, resname;
    Double lat, lon;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    int g = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Back");
        }

        foodname_ = (EditText) findViewById(R.id.editText3);
        //location_=(EditText)findViewById(R.id.editText4);
        review_ = (EditText) findViewById(R.id.editText5);
        buttonpost_ = (Button) findViewById(R.id.buttonPost);
        buttonBack = (Button) findViewById(R.id.buttonBack);

        imageView_ = (ImageView) findViewById(R.id.imageView);

        //Set the image to the one clicked by camera
        Intent recvIntent = getIntent();
        photo = recvIntent.getParcelableExtra("photo");
        imageView_.setImageBitmap(photo);

        buttonpost_.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, rating);
        //Getting the instance of AutoCompleteTextView
        act_service = (Spinner) findViewById(R.id.spinner);
        act_service.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        act_rating = (Spinner) findViewById(R.id.spinner1);
        act_rating.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, food_quality);
        act_quality = (Spinner) findViewById(R.id.spinner4);
        act_quality.setAdapter(adapter2);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, price);
        act_price = (Spinner) findViewById(R.id.spinner3);
        act_price.setAdapter(adapter1);

        // place finder to get lat and lon of location

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("100", "Place: " + place.getName());
                resname = place.getName().toString();
                LatLng latLng = place.getLatLng();
                lat = latLng.latitude;
                lon = latLng.longitude;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("100", "An error occurred: " + status);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //the back button on the title bar
        Intent myIntent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(myIntent);
        return true;

    }

    @Override
    public void onClick(View v) {
        //making food name mandatory
        switch (v.getId()) {
            case R.id.buttonPost:
                if (foodname_.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Food Name is mandatory!", Toast.LENGTH_LONG).show();
                } else {

                    buttonpost_.setEnabled(false);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte data[] = baos.toByteArray();
                    path = "photos/" + UUID.randomUUID() + ".png";
                    StorageReference photoRef = storage.getReference(path);
                    StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("text", "myphoto").build();

                    UploadTask uploadTask = photoRef.putBytes(data);
                    uploadTask.addOnSuccessListener(UploadPhotoActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("Success", "Success");
                            //progressBar.setVisibility(View.GONE);
                            @SuppressWarnings("VisibleForTests") Uri url = (Uri) taskSnapshot.getDownloadUrl();
                            //textUrl.setVisibility(View.VISIBLE);
                            //urlLink= null;

                            String urlLink = "";
                            try {
                                urlLink = URLEncoder.encode(url.toString(), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            g = 1;
                            String input[] = new String[10];
                            input[0] = urlLink;                                   // photopath
                            input[1] = foodname_.getText().toString();            // foodname;
                            input[2] = resname;                                   // restaurant
                            input[3] = lat.toString();                            // latitude
                            input[4] = lon.toString();                            // longitude
                            input[5] = review_.getText().toString();              // review
                            input[6] = act_quality.getSelectedItem().toString();  //quality
                            input[7] = act_price.getSelectedItem().toString();    // price
                            input[8] = act_service.getSelectedItem().toString();  // service
                            input[9] = act_rating.getSelectedItem().toString();   // rating
                            new UploadPhotoActivity.InvokeWeService().execute(input);

                        }

                    });
                }
                break;
            case R.id.buttonBack:
                UploadPhotoActivity.this.finish();
                break;
        }
    }

    public class InvokeWeService extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            Log.d("100", "in web service");
            String response = "";
            String requestUrl = "http://ec2-54-213-169-9.us-west-2.compute.amazonaws.com/insertdata.php?";
            StringBuilder str = new StringBuilder();
            StringBuilder result = new StringBuilder();
            //str.append("test=" + "parameter&");
            try {
                str.append("path=" + strings[0] + "&");
                str.append("foodname=" + URLEncoder.encode(strings[1], "UTF-8") + "&");
                str.append("resname=" + URLEncoder.encode(strings[2], "UTF-8") + "&");
                str.append("latitude=" + strings[3] + "&");
                str.append("longitude=" + strings[4] + "&");
                str.append("review=" + URLEncoder.encode(strings[5], "UTF-8") + "&");
                str.append("quality=" + strings[6] + "&");
                str.append("price=" + strings[7] + "&");
                str.append("service=" + strings[8] + "&");
                str.append("rating=" + strings[9]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String mystring = str.toString();
//            try {
//                requestUrl = requestUrl + URLEncoder.encode(mystring,"UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            requestUrl = requestUrl + mystring;
            try {
                url = new URL(requestUrl);
                HttpURLConnection myconnection = (HttpURLConnection) url.openConnection();
                myconnection.setReadTimeout(15000);
                myconnection.setConnectTimeout(15000);
                myconnection.setRequestMethod("GET");
                myconnection.setDoInput(true);
                myconnection.setDoOutput(true);
                //int responseCode = myconnection.getResponseCode();
                if (200 == HttpURLConnection.HTTP_OK) ;
                {


                    InputStream in = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("SUCCESS")) {
                Toast.makeText(getApplicationContext(), "Photo Successfully uploaded", Toast.LENGTH_LONG).show();

                Intent mapIntent = new Intent(UploadPhotoActivity.this, MapsActivity.class);
                startActivity(mapIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Photo upload failed", Toast.LENGTH_LONG).show();
            }
            buttonpost_.setEnabled(true);
        }

    }
}
