package com.project.umbcmobile.umbc_mobile;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name_, password_;
    Button login_, facebook_, gmail_;
    TextView signup_, forgot_;
    CheckBox remember_;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        //this is to randomly select from a group of images for the background of the login
        ImageView background = (ImageView) findViewById(R.id.imageViewBack);
        Resources res = getResources();
        final TypedArray myImages = res.obtainTypedArray(R.array.myImages);
        final Random random = new Random();
        int randomInt = random.nextInt(myImages.length());
        int drawableID = myImages.getResourceId(randomInt, -1);
        background.setBackgroundResource(drawableID);

        name_ = (EditText) findViewById(R.id.editText);
        password_ = (EditText) findViewById(R.id.editText2);

        login_ = (Button) findViewById(R.id.buttonLogin);
        login_.setOnClickListener(this);
        facebook_ = (Button) findViewById(R.id.facebook);
        facebook_.setOnClickListener(this);
        gmail_ = (Button) findViewById(R.id.gmail);
        gmail_.setOnClickListener(this);
        signup_ = (TextView) findViewById(R.id.textViewSignUp);
        signup_.setOnClickListener(this);
        forgot_ = (TextView) findViewById(R.id.textViewForgot);
        forgot_.setOnClickListener(this);
        remember_ = (CheckBox) findViewById(R.id.checkBoxRemember);
        remember_.setOnClickListener(this);

        //change the drawable in the xml to the app logo


    }

    @Override
    public void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            LoginActivity.this.finish();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                String email = name_.getText().toString();
                final String password = password_.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    LoginActivity.this.finish();

                                }
                            }
                        });
                break;
            case R.id.facebook:
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(
                                AuthUI.FACEBOOK_PROVIDER).build(), 1);
                break;
            case R.id.gmail:
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(
                                AuthUI.GOOGLE_PROVIDER).build(), 1);
                break;
            case R.id.textViewSignUp:
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(
                                AuthUI.EMAIL_PROVIDER).build(), 1);
                break;
            case R.id.textViewForgot:
                //what should we do on forgot password
                break;
            case R.id.checkBoxRemember:
                //to sign out on every app close or keep the user
                break;
        }

    }
}

