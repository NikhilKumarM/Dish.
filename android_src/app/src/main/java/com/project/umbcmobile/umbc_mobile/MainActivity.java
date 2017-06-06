package com.project.umbcmobile.umbc_mobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_REQUEST = 1888;

    Button loginButton, mapButton;
    FloatingActionButton showPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.login_header_logo);
        getSupportActionBar().setTitle("");
        loginButton = (Button) findViewById(R.id.loginButton);
        mapButton = (Button) findViewById(R.id.mapButton);
        showPhotos = (FloatingActionButton) findViewById(R.id.cameraButton);
        loginButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        showPhotos.setOnClickListener(this);
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            loginButton.setText("Sign Out");
            mapButton.setVisibility(View.VISIBLE);

        }else {
            loginButton.setText("Sign in to get started!");
            mapButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent loginIntent1 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent1);
                }else {
                    FirebaseAuth.getInstance().signOut();


                   // loginButton.setText("Sign in to get started!");
                    mapButton.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                    startActivity(intent);
                    MainActivity.this.finish();

                }
                break;
            case R.id.mapButton:
                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mapsIntent);
                break;
            case R.id.cameraButton:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            loginButton.setText("Sign Out");
            mapButton.setVisibility(View.VISIBLE);

        }else {
            loginButton.setText("Sign in to get started!");
            mapButton.setVisibility(View.INVISIBLE);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Intent uploadIntent = new Intent(MainActivity.this, UploadPhotoActivity.class);
            uploadIntent.putExtra("photo", photo);
            startActivity(uploadIntent);

        }
    }

}
